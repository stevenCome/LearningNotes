package project.task;

import com.jd.lsb.task.domain.Request;
import com.jd.lsb.task.domain.Response;
import com.jd.lsb.task.domain.Task;
import com.jd.lsb.task.handler.Handler;
import com.jd.tms.tfc.constant.Constants;
import com.jd.ump.annotation.JProEnum;
import com.jd.ump.annotation.JProfiler;
import com.jd.ump.x.ProfilerX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Package:com.pj
 * Author: pengjie3@jd.com
 * Date: 2018/10/20 10:21
 */
public class LsbTaskHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(LsbTaskHandler.class);

    @Resource
    private TaskAdepter taskAdepter;

    @JProfiler(jKey = "tfc.LsbTaskHandler.handle", jAppName = Constants.UMP_APP, mState = {JProEnum.TP, JProEnum.FunctionError})
    @Override
    public void handle(Request request, Response response) throws Throwable {
        if(request == null || request.getTask() == null){
            logger.info("LsbTaskHandler handle task is null");
            return;
        }
        Task task = request.getTask();
        ProfilerX.registerInfo("tfc.LsbTaskHandler.handle." + task.getBizType(), Constants.UMP_APP, false, true);
        try {
            if (null == task.getBody()) {
                taskAdepter.handle(task.getBizType(), task.getBizNo(), null);
            } else {
                taskAdepter.handle(task.getBizType(), task.getBizNo(), task.getBody().getString());
            }
        } catch (Exception e) {
            ProfilerX.functionError(e);
            logger.error("LsbTaskHandler handle error: bizType["+task.getBizType()+"],bizNo["+task.getBizNo()+"]", e);
            throw e;
        } finally {
            ProfilerX.registerInfoEnd();
        }
    }
}
