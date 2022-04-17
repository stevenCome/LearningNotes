package project.multiThread;

/**
 * @author hexushuai@jd.com
 * @version 1.0.0
 * @description 多线程实现任务批量执行
 * @date 2022/4/17 3:07 下午
 */
public class MultiThread {

    private ResultDemo callSumClerkSaleTask() {
        ClerkSaleAnalysisTotalVO finalResult = new ClerkSaleAnalysisTotalVO();
        //线程池执行
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<ClerkSaleAnalysisTotalVO>> futureList = new ArrayList<>();
        while (conditon) {
            TaskDemoCallable clerkSaleAnalysisCallable = new TaskDemoCallable(param);
            //提交任务
            Future<ResultDemo> future = executorService.submit(clerkSaleAnalysisCallable);
            futureList.add(future);
        }

        int count = 0;
        for (Future<ResultDemo> futureObj : futureList) {
            try {
                //获取执行结果,发生异常时要把异常抛出,否则会影响计费的重量体积不准确
                ResultDemo resultDemo = futureObj.get();
                if (Objects.nonNull(resultDemo)) {
                    log.info("第" + (count + 1) + "个任务已执行完毕.返回结果[{}]", JsonUtils.writeToString(clerkSaleAnalysis));
                } else {
                    log.info("第" + (count + 1) + "个任务查询任务未完成.");
                }
                count ++;
            } catch (Exception e) {
                log.error("future.get sum execution", e);
                //异常时关闭线程池
                executorService.shutdown();
                throw new RuntimeException(e.getMessage());
            }
        }
        return finalResult;
    }
}
