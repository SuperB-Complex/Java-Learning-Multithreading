package self.explore.multithreads.countdownlatch.scenario1;

import java.text.MessageFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch �Ĺ��캯�� count ��ʾ�����̵߳ĸ���
 * ��ǰ�̺߳������̹߳���һ�� CountDownLatch ����
 * ��ǰ�̵߳��� await() ��������
 * �����߳���ִ����Ϻ���� countDown() �������е���ʱ(count --)
 *
 * ֻ�е�ǰ�߳�A��DB��������, �߳�B���д������, �߳�C����ͳ���ļ�ȫ��ִ����Ϻ�, �߳�D���ܽ�ͳ���ļ����ʼ����ͳ�ȥ.
 */

public class Test {
	public static void main(String[] args) {

        // ������̳߳�������Ҫ�������߳�, 1������ǰRunner(������Runnerִ����Ϻ��ִ��), 1��������Runner.
        ExecutorService executorService  = Executors.newFixedThreadPool(2);

        // CountDownLatch ���������ʾ ����Runner �ĸ���, ��������ǰRunner
        CountDownLatch countDownLatch = new CountDownLatch(3);

        // ��ǰRunner Ҳ��Ҫ CountDownLatch ����, ͨ�� await() �������� ��ǰRunner ���߳�.
        executorService.submit(new MainRunner(countDownLatch));

        // �����ύ��3��Runner
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
