package project.multiThread;

/**
 * @author hexushuai@jd.com
 * @version 1.0.0
 * @description
 * @date 2022/4/17 3:11 下午
 */
public class TaskDemoCallable implements Callable<TaskDemoResult> {
    private static final Logger logger = LoggerFactory.getLogger(TaskDemoCallable.class);

    private TaskDemoMapper taskDemoMapper;

    private TaskDemoParamVO taskDemoMapper;

    public ClerkSaleAnalysisCallable(TaskDemoMapper taskDemoMapper, TaskDemoParamVO taskDemoParamVO) {
        this.taskDemoMapper = taskDemoMapper;
        this.taskDemoMapper = taskDemoMapper;
    }
    @Override
    public ClerkSaleAnalysisTotalVO call() throws Exception {
        logger.info(String.format("ClerkSaleAnalysisCallable.currentThread[%s],clerkSaleAnalysisParamVO[%s]",Thread.currentThread().getName(), JSONUtil.toJsonStr(clerkSaleAnalysisParamVO)));
        ClerkSaleAnalysisTotalVO clerkSaleAnalysisTotalVO = taskDemoMapper.sumClerkSaleAnalysis(clerkSaleAnalysisParamVO);
        return clerkSaleAnalysisTotalVO;
    }
}
