package self.explore.multithreads.cyclicbarrierwithinterrupt;


import org.apache.commons.lang3.RandomUtils;

import java.text.MessageFormat;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 这里假设 startRow 是从数据库中读取数据的起始行数 mysql 中相当于 limit 中的第一个参数
 * 使用 AtomicInteger 确保每个并行线程从数据库中可以读取到自己唯一的100万数据
 * do...while 循环可以确保 10个线程同时到达 await() 方法后可以循环执行,直到达到某个条件
 * 这里才能最能体现 CyclicBarrier 对象的含义:循环栅栏, 所有线程都到达  await() 方法后(到达栅栏后)又可以回过头继续重复执行
 */
public class Test {

    private static final int threadNumber = 10;
    private static AtomicInteger startRow = new AtomicInteger();
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(threadNumber, new SendEmailRunner("total"));

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);

        for (int i = 0; i < threadNumber; i++) {
            executorService.submit(new DataProcessRunner(String.valueOf(i + 1), cyclicBarrier));
        }
        executorService.shutdown();

        // 通过 ExecutorService 对象的 shutdown() 方法 和 isTerminated() 方法组合判断 1 亿数据是否全部处理完毕
        while (true) {
            if (executorService.isTerminated()) {
                System.out.println(MessageFormat.format("{0} finish, total {1}", new Object[]{"main", startRow.get()}));
                break;
            }
        }
    }

    /**
     * 多个线程并行计算数据
     */
    public static class DataProcessRunner implements Runnable {

        private String name;
        private CyclicBarrier cyclicBarrier;

        public DataProcessRunner(String name, CyclicBarrier cyclicBarrier) {
            this.name = name;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            // do while 循环可以确保循环执行,直到达到某个条件
            do {
                // 获取读取的起始记录和记录数
                Integer rows = 100;
                Integer start = startRow.getAndAdd(rows);

                // 模拟读取和处理的过程
                try {
                    TimeUnit.SECONDS.sleep(RandomUtils.nextInt(1, 5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    System.out.println(MessageFormat.format("{0} process data limit {1},{2}", new Object[]{name, start, rows}));
                    // 等所有10个线程全部处理完各自的 100 万数据后再会进入下一次循环
                    // 这里才能最能体现 CyclicBarrier 对象的含义:循环栅栏, 所有线程都到达  await() 方法后(到达栅栏后)又可以回过头继续重复执行
                    cyclicBarrier.await(1, TimeUnit.SECONDS);
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    System.out.println("BrokenBarrierException");
                }

                if (cyclicBarrier.isBroken()) {
                    // do....
                }
            } while (startRow.get() < 10000 && !cyclicBarrier.isBroken()); // 循环处理到达了某个条件, 这里是处理完了 1 亿数据
        }
    }

    /**
     * 等所有线程到达 await() 方法后, 这个线程统计最终数据
     */
    public static class SendEmailRunner implements Runnable {

        private String name;

        public SendEmailRunner(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println(MessageFormat.format("{0} process finish, total {1}, sendEmail success", new Object[]{name, startRow.get()}));
        }
    }
}