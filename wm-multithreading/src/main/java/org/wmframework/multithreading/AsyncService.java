package org.wmframework.multithreading;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AsyncService {

    /**
     * 默认超时时间5000毫秒
     */
    private static final long DEFAULT_TIMEOUT_MILLISECONDS = 5000L;


    private ThreadPoolExecutor threadPoolExecutor;

    private AsyncService() {
        this.threadPoolExecutor = new ThreadPoolExecutor(10, 20, 2L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
    }

    /**
     * 异步执行一个任务
     *
     * @param runnable 异步执行任务
     */
    public void exec(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

    /**
     * 批量异步执行任务
     *
     * @param runnableList 批量任务
     */
    public void batchExec(List<Runnable> runnableList) {
        runnableList.forEach(this::exec);
    }

    /**
     * 异步执行并接收返回值
     * 默认5000毫秒超时
     *
     * @param callable 异步执行任务
     * @param <T>      返回值类型
     * @return 异步任务返回值
     * @throws InterruptedException 异常
     * @throws ExecutionException   异常
     * @throws TimeoutException     异常
     */
    public <T> T execWithRes(Callable<T> callable) throws InterruptedException, ExecutionException, TimeoutException {
        return execWithRes(callable, DEFAULT_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS);
    }

    /**
     * 批量异步执行并接收返回值
     *
     * @param callableList 批量异步执行任务
     * @return 异步任务返回值
     */
    public <T> List<T> batchExecWithRes(List<Callable<T>> callableList) throws InterruptedException {
        List<FutureTask<T>> futureTaskList = new ArrayList<>();
        callableList.forEach(callable -> {
            FutureTask<T> futureTask = new FutureTask<>(callable);
            futureTaskList.add(futureTask);
        });
        futureTaskList.forEach(f -> {
            threadPoolExecutor.submit(f);
        });
        return futureTaskList.stream().map(m -> {
            try {
                return m.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }

    /**
     * 异步执行并接收返回值
     * 自定义超时时间及单位
     *
     * @param callable 异步执行任务
     * @param timeout  超时时间；如1秒未返回则超时
     * @param timeUnit 超时时间单位
     * @param <T>      返回值类型
     * @return 异步任务返回值
     * @throws InterruptedException 异常
     * @throws ExecutionException   异常
     * @throws TimeoutException     异常
     */
    public <T> T execWithRes(Callable<T> callable, long timeout, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        Future<T> submit = threadPoolExecutor.submit(callable);
        return submit.get(timeout, timeUnit);
    }

//    public static void main(String[] args) {
//        asyncService.exec(() -> testService.test1(666));
//        List<Runnable> list = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            int finalI = i;
//            list.add(() -> log.info(finalI + "-runnable"));
//        }
//        asyncService.batchExec(list);
//        List<Callable<Integer>> list2 = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            int finalI = i;
//            list2.add(() -> {
//                Thread.sleep(new Random().nextInt(10000));
//                int num = finalI + 1;
//                log.info("返回值：" + num);
//                return num;
//            });
//        }
//        List<Integer> rrr = asyncService.batchExecWithRes(list2);
//        IntSummaryStatistics intSummaryStatistics = rrr.stream().collect(Collectors.summarizingInt(v -> v));
//        log.info("====count:{}", intSummaryStatistics);
//    }
}
