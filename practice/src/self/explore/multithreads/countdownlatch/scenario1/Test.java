package self.explore.multithreads.countdownlatch.scenario1;

import java.text.MessageFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch 的构造函数 count 表示其他线程的个数
 * 当前线程和其他线程共享一个 CountDownLatch 对象
 * 当前线程调用 await() 方法阻塞
 * 其他线程在执行完毕后调用 countDown() 方法进行倒计时(count --)
 *
 * 只有当前线程A从DB加载数据, 线程B进行处理分析, 线程C生成统计文件全部执行完毕后, 线程D才能将统计文件以邮件发送出去.
 */

public class Test {
	public static void main(String[] args) {

        // 这里的线程池中至少要有两个线程, 1个给当前Runner(在其他Runner执行完毕后才执行), 1个给其他Runner.
        ExecutorService executorService  = Executors.newFixedThreadPool(2);

        // CountDownLatch 构造参数表示 其他Runner 的个数, 不包含当前Runner
        CountDownLatch countDownLatch = new CountDownLatch(3);

        // 当前Runner 也需要 CountDownLatch 对象, 通过 await() 方法阻塞 当前Runner 的线程.
        executorService.submit(new MainRunner(countDownLatch));

        // 这里提交了3个Runner
        for (int i = 1; i <= 3; i++) {
            executorService.submit(new WorkRunner(String.valueOf(i), countDownLatch));
        }
        
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
