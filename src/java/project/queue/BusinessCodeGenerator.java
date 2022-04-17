package project.queue;

/**
 * @author hexushuai@jd.com
 * @version 1.0.0
 * @description
 * @date 2022/4/17 3:21 下午
 */
public class BusinessCodeGenerator {

    private static final Logger logger = LoggerFactory.getLogger(BusinessCodeGenerator.class);

    private static final String PREFIX_REDIS_KEY = BusinessCodeGenerator.class.getName();

    private SimpleDateFormat simpleDateFormat6 = new SimpleDateFormat("yyMMdd");

    @Resource
    private SysSequenceManager sysSequenceManager;

    @Resource
    private SysSequenceFeeManager sysSequenceFeeManager;

    @Resource
    private RedisClientProxy redisClientProxy;

    @Resource
    private LoadableMultiQueue<Long> longMemoryLoadableMultiQueue;

    @Resource
    private LoadableMultiQueue<Long> longJimdbLoadableMultiQueue;

    /**
     * 获取Sequence(MemoryQueue)
     * @param seqKey
     * @return
     */
    @JProfiler(jKey = "tfc.businessCodeGenerator.getSequenceFromJimQueue", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
    public long getSequenceFromJimQueue(String seqKey) {
        return this.longJimdbLoadableMultiQueue.poll(determineCurrentLookupKey(seqKey));
    }

    /**
     * 获取Sequence(MemoryQueue)
     * @param seqKey
     * @return
     */
    @JProfiler(jKey = "tfc.businessCodeGenerator.getSequenceFromMemoryQueue", jAppName = Constants.UMP_APP, mState = { JProEnum.TP, JProEnum.FunctionError })
    public long getSequenceFromMemoryQueue(String seqKey){
        return this.longMemoryLoadableMultiQueue.poll(determineCurrentLookupKey(seqKey));
    }

    /**
     * 获取序列号
     * @return
     */
    private String determineCurrentLookupKey(String seqKey){
        /*压测流量增加前缀*/
		/*if(TraceHolder.isForcebot()){
			logger.info("压测流量获取序列号");
			return "shadow_" + seqKey;
		}*/
        return seqKey;
    }

}
