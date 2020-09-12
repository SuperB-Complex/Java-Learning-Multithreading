package self.explore.multithreads.countdownlatch.scenario2;

import java.text.MessageFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import self.explore.multithreads.countdownlatch.scenario1.RandomUtils;

/**
 * CountDownLatch �Ĺ��캯�� count ��ʾ�����̵߳ĸ���
 * ��ǰ�̺߳������̹߳���һ�� CountDownLatch ����
 * ��ǰ�̵߳��� await() ��������
 * �����߳���ִ����Ϻ���� countDown() �������е���ʱ(count --)
 *
 * ֻ�е�ǰ�߳�Dִ����Ϻ��߳�A��DB��������+�߳�B���д������+�߳�C����ͳ���ļ�����ͬʱִ��.
 */

public class Test {
	public static void main(String[] args) {

        // ������̳߳�������Ҫ��4���߳�, 3�������������߳�, 1 ������ִ�е��߳�.
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
