package self.explore.multithreads.countdownlatchwithinterrupt;

import java.text.MessageFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * await() ����ʹ��ǰ�߳�A ����, �����߳�c ���� �߳�A ����� interrupt() ����ʹ�������״̬�˳�, 
 * ��ʱ await() �����׳� java.lang.InterruptedException
 * �߳�B �ڵ��� countDown() �����������˳�
 */
public class Test {
	public static void main(String[] args) {
		
        // ������̳߳�������Ҫ��4���߳�, 3�������������߳�, 1 ������ִ�е��߳�.
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
                // await() ����ʹ��ǰ�߳�����,
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
