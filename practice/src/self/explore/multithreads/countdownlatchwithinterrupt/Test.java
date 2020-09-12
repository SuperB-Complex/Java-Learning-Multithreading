package self.explore.multithreads.countdownlatchwithinterrupt;

import java.text.MessageFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * await() 方法使当前线程A 阻塞, 其他线程c 调用 线程A 对象的 interrupt() 方法使其从阻塞状态退出, 
 * 此时 await() 方法抛出 java.lang.InterruptedException
 * 线程B 在调用 countDown() 方法后正常退出
 */
public class Test {
	public static void main(String[] args) {
		
        // 这里的线程池中至少要有4个线程, 3个给被阻塞的线程, 1 给优先执行的线程.
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread workThreaad = new Thread(new WorkRunner("workThreaad", countDownLatch));
        Thread awaitThread = new Thread(new AwaitRunner("awaitThreaad", countDownLatch));
        Thread interruptThread = new Thread(new InterruptRunner(awaitThread));

        workThreaad.start();
        awaitThread.start();
        interruptThread.start();
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
                TimeUnit.MILLISECONDS.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(MessageFormat.format("{0} finish", new Object[]{RunnerName}));
            countDownLatch.countDown();
        }
    }

    public static class AwaitRunner implements Runnable {

        private String RunnerName;
        private CountDownLatch countDownLatch;

        public AwaitRunner(String runnerName, CountDownLatch countDownLatch) {
            RunnerName = runnerName;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                // await() 方法使当前线程阻塞,
                countDownLatch.await();
            } catch (InterruptedException e) {
                System.err.print(MessageFormat.format("AwaitRunner {0} has InterruptedException:\n", new Object[]{RunnerName}));
                e.printStackTrace();
            }
            System.out.println(MessageFormat.format("{0} finish", new Object[]{RunnerName}));
        }
    }

    public static class InterruptRunner implements Runnable {

        private Thread targetThread;

        public InterruptRunner(Thread targetThread) {
            this.targetThread = targetThread;
        }

        @Override
        public void run() {
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("interrupt targetThread");
            targetThread.interrupt();
        }
    }
}
