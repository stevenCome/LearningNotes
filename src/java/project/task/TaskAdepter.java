package project.task;

import com.jd.common.annotation.LogTrace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Package:com.jd.tms.ecp.task
 * Author: pengjie3@jd.com
 * Date: 2018/10/20 15:48
 */
public class TaskAdepter {

    private static final Logger logger = LoggerFactory.getLogger(TaskAdepter.class);

    private Map<String, TaskHandler> taskHandlers;

    public void setTaskHandlers(Map<String, TaskHandler> taskHandlers) {
        this.taskHandlers = taskHandlers;
    }

    @LogTrace(key = "handle",input = true,output = true,entry = true)
    public void handle(String bizType, String bizNo, String message){
        if(taskHandlers == null){
            logger.error("TaskAdepter handle 没有配置任何任务处理类，请检查！bizType:[{}]", bizType);
            return;
        }

        TaskHandler taskHandler = taskHandlers.get(bizType);
        if (null != taskHandler) {
            taskHandler.handle(bizNo, message);
        } else {
            logger.error("TaskAdepter handle 需要执行的业务类型没有对应的处理类 bizType:[{}]", bizType);
        }
    }
}
