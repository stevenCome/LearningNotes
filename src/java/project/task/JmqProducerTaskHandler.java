package project.task;

import com.jd.common.util.StringUtils;
import com.jd.tms.tfc.dto.MessageDto;
import com.jd.tms.tfc.rpc.JmqProducerProxy;
import com.jd.tms.tfc.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Package:com.jd.tms.ecp.service.impl
 * Author: pengjie3@jd.com
 * Date: 2018/10/20 13:58
 */
public class JmqProducerTaskHandler implements TaskHandler {

    private static final Logger logger = LoggerFactory.getLogger(JmqProducerTaskHandler.class);

    @Resource
    private JmqProducerProxy jmqProducerProxy;

    @Override
    public void handle(String bizNo, String message) {
        logger.info("JmqProducerTaskHandler handle bizNo:{} message:{}", bizNo, message);
        MessageDto messageDto = JacksonUtil.parseJson(message, MessageDto.class);
        if (messageDto == null || StringUtils.isEmpty(messageDto.getTopic())
                || StringUtils.isEmpty(messageDto.getText())) {
            logger.info("JmqProducerTaskHandler handle : bizNo:{} , messageDto is empty, return false!", bizNo);
            return;
        }
        logger.info("JmqProducerTaskHandler handle topic[" + messageDto.getTopic() + "],text[" + messageDto.getText() + "],businessId[" + messageDto.getBusinessId() + "]");
        if (!jmqProducerProxy.sendMessageSync(messageDto.getTopic(), messageDto.getText(), messageDto.getBusinessId())) {
            logger.error("JmqProducerTaskHandler handle sendMessageSync error bizNo:{} message:{}", bizNo, message);
        }
    }
}
