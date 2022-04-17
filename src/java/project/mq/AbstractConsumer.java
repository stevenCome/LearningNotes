package project.mq;

import com.jd.common.exception.AccessRefuseException;
import com.jd.common.util.Identities;
import com.jd.jmq.client.consumer.MessageListener;
import com.jd.jmq.common.message.Message;
import com.jd.jspliter.jmq.consumer.EnvMessageListener;
import com.jd.tms.tfc.constant.Constants;
import com.jd.ump.annotation.JProEnum;
import com.jd.ump.annotation.JProfiler;
import com.jd.ump.x.ProfilerX;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * 抽象MQ消费者
 * 
 * @author suihonghua
 *
 */
public abstract class AbstractConsumer extends EnvMessageListener implements MessageListener {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${jmq.consumer.app}")
	private String jmqConsumerApp;

	/**
	 * JMQ消费方法。注意: 消费不成功请抛出异常，MQ会自动重试
	 *
	 * @param messages
	 * @
	 */
	@Override
	@JProfiler(jKey = "tfc.AbstractConsumer.onMessage", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
	public void onMessages(List<Message> messages) {
		if (messages == null || messages.isEmpty()) {
			return;
		}
		for (Message message : messages) {
			this.consume(message);
		}
	}

	protected void consume(Message message) {
		if (message == null) {
			logger.warn("consume: 消息对象为空，无法消费！");
			throw new RuntimeException("消息对象为空，无法消费！");
		}
		ProfilerX.registerInfo("jmq." + this.jmqConsumerApp + "." + message.getTopic(), Constants.UMP_APP, false, true);
		try {
			ThreadContext.put("traceKey", Identities.uuid32());
			this.consume(message.getTopic(), message.getText());
		}catch(AccessRefuseException e){
			logger.warn("accessRefuse-error:重复数据不消费！ topic[" + message.getTopic() + "] message[" + message.getText() + "]", e);
		}catch (Throwable t) {
			ProfilerX.functionError();
			StringBuilder sb = new StringBuilder();
			sb.append("MQ消费失败[").append(t.getMessage()).append("],");
			sb.append("topic[").append(message.getTopic()).append("],");
			sb.append("flag[").append(message.getFlag()).append("],");
			sb.append("app[").append(message.getApp()).append("],");
			sb.append("businessId[").append(message.getBusinessId()).append("],");
			sb.append("sendTime[").append(message.getSendTime()).append("],");
			sb.append("text[").append(message.getText()).append("],");
			logger.error("consume-error:" + sb.toString(), t);
			throw new RuntimeException(t.getMessage());
		} finally {
			ThreadContext.remove("traceKey");
			ProfilerX.registerInfoEnd();
		}
	}

	/**
	 * 消费MQ消息
	 * 
	 * @author suihonghua
	 * @param topic
	 * @param content
	 * @throws Throwable
	 */
	public abstract void consume(String topic, String content);

}
