package project.queue;

import com.jd.common.queue.loader.AbstractTimedQueueLoader;
import com.jd.common.util.StringUtils;
import com.jd.tms.tfc.constant.Constants;
import com.jd.tms.tfc.manager.SysSequenceManager;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class SimpleQueueLoader extends AbstractTimedQueueLoader<Long> {

    @Resource
    private SysSequenceManager sysSequenceManager;

    @Override
    public List<Long> loadData(String key, int count) {
        if(StringUtils.isNotBlank(key) && key.indexOf(Constants.Shadow.SHADOW_SEQ_KEY_PREFIX) > -1){
            //设置影子库
            //TraceHolder.setForcebot();
        }
        List<Long> result = new ArrayList<Long>(count);
        long v = sysSequenceManager.selectNextValueByStep(key, count);
        for (int i = count - 1; i >= 0; i--) {
            result.add(v - i);
        }
        return result;
    }
}
