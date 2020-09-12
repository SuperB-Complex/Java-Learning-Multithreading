package self.explore.multithreads.cyclicbarrierwithinterrupt;


import org.apache.commons.lang3.RandomUtils;

import java.text.MessageFormat;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ������� startRow �Ǵ����ݿ��ж�ȡ���ݵ���ʼ���� mysql ���൱�� limit �еĵ�һ������
 * ʹ�� AtomicInteger ȷ��ÿ�������̴߳����ݿ��п��Զ�ȡ���Լ�Ψһ��100������
 * do...while ѭ������ȷ�� 10���߳�ͬʱ���� await() ���������ѭ��ִ��,ֱ���ﵽĳ������
 * ��������������� CyclicBarrier ����ĺ���:ѭ��դ��, �����̶߳�����  await() ������(����դ����)�ֿ��Իع�ͷ�����ظ�ִ��
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

        // ͨ�� ExecutorService ����� shutdown() ���� �� isTerminated() ��������ж� 1 �������Ƿ�ȫ���������
        while (true) {
            if (executorService.isTerminated()) {
                System.out.println(MessageFormat.format("{0} finish, total {1}", new Object[]{"main", startRow.get()}));
                break;
            }
        }
    }

    /**
     * ����̲߳��м�������
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
            // do while ѭ������ȷ��ѭ��ִ��,ֱ���ﵽĳ������
            do {
                // ��ȡ��ȡ����ʼ��¼�ͼ�¼��
                Integer rows = 100;
                Integer start = startRow.getAndAdd(rows);

                // ģ���ȡ�ʹ���Ĺ���
                try {
                    TimeUnit.SECONDS.sleep(RandomUtils.nextInt(1, 5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    System.out.println(MessageFormat.format("{0} process data limit {1},{2}", new Object[]{name, start, rows}));
                    // ������10���߳�ȫ����������Ե� 100 �����ݺ��ٻ������һ��ѭ��
                    // ��������������� CyclicBarrier ����ĺ���:ѭ��դ��, �����̶߳�����  await() ������(����դ����)�ֿ��Իع�ͷ�����ظ�ִ��
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
            } while (startRow.get() < 10000 && !cyclicBarrier.isBroken()); // ѭ����������ĳ������, �����Ǵ������� 1 ������
        }
    }

    /**
     * �������̵߳��� await() ������, ����߳�ͳ����������
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