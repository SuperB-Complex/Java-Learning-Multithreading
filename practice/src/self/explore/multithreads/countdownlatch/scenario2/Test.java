package self.explore.multithreads.countdownlatch.scenario2;

import java.text.MessageFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import self.explore.multithreads.countdownlatch.scenario1.RandomUtils;

/**
 * CountDownLatch 的构造函数 count 表示其他线程的个数
 * 当前线程和其他线程共享一个 CountDownLatch 对象
 * 当前线程调用 await() 方法阻塞
 * 其他线程在执行完毕后调用 countDown() 方法进行倒计时(count --)
 *
 * 只有当前线程D执行完毕后，线程A从DB加载数据+线程B进行处理分析+线程C生成统计文件才能同时执行.
 */

public class Test {
	public static void main(String[] args) {

        // 这里的线程池中至少要有4个线程, 3个给被阻塞的线程, 1 给优先执行的线程.
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        CountDownLatch countDownLatch = new CountDownLatch(1);

        executorService.submit(new Test.MainRunner(countDownLatch));
        executorService.submit(new Test.MainRunner(countDownLatch));
        executorService.submit(new Test.MainRunner(countDownLatch));

        executorService.submit(new Test.WorkRunner(String.valueOf(1), countDownLatch));
    }

    public static class WorkRunner implements Runnable {

        private String RunnerName;
        private CountDownLatch countDownLatch;

        public WorkRunner(String runnerName, CountDownLatch countDownLatch) {
            RunnerName = runnerName;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                int time = RandomUtils.nextInt(1, 5);
                TimeUnit.SECONDS.sleep(time);

                System.out.println(MessageFormat.format("WorkRunner {0} work {1} s", new Object[]{RunnerName, time}));
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class MainRunner implements Runnable {

        private CountDownLatch countDownLatch;

        public MainRunner(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
            	// mainrunner thread waiting until cnt reaches 0
                countDownLatch.await();

                TimeUnit.SECONDS.sleep(2);

                System.out.println("MainRunner finish");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
