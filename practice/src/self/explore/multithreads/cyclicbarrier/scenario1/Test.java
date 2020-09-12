package self.explore.multithreads.cyclicbarrier.scenario1;

import org.apache.commons.lang3.RandomUtils;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.concurrent.*;

/*
 * ������һ��Excel�������û�����������ˮ��ÿ��Sheet����һ���ʻ���һ���ÿ��������ˮ��������Ҫͳ���û����վ�������ˮ��
 * ���ö��̴߳���ÿ��sheet���������ˮ����ִ����֮�󣬵õ�ÿ��sheet���վ�������ˮ���������barrierAction
 * ����Щ�̵߳ļ����������������Excel���վ�������ˮ��
 * */

// ֻ�������̵߳� await() ��������ʱ, totalRunner �̲߳ſ�ʼִ��
// �� await() �����������߳�, �� totalRunner �߳�ִ����Ϻ���ܼ���ִ��
// CyclicBarrier �еĵ�һ��������ʾ�� await() �����������̵߳���Ŀ
public class Test {

    public static void main(String[] args) {
        // 1. ���Ȼ�ȡexcel sheet �ĸ���
        int sheetSize = 3;
        // 2. ��ʼ������, �����ô�С, �� sheet ��Ŀһ��, ���ڴ洢ÿ���̻߳�ȡ��������
        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(sheetSize);

        TotalRunner totalRunner = new TotalRunner("total", arrayBlockingQueue);
        // 3. ���� CyclicBarrier ����, await �߳���Ŀ�� sheet ��Ŀһ��
        // totalRunner ���ڵ����� await �߳�ִ����Ϻ�����ִ�е��߼�
        CyclicBarrier cyclicBarrier = new CyclicBarrier(sheetSize, totalRunner);

        // 4. �����̳߳�, �����ô�С, �� sheet ��Ŀһ��, ���� await �߳�ִ��
        ExecutorService executorService = Executors.newFixedThreadPool(sheetSize);

        // 5. �ύ���̳߳���ִ��
        for (int i = 0; i < sheetSize; i++) {
            executorService.submit(new ExcelRunner(cyclicBarrier, arrayBlockingQueue, String.valueOf(i+1)));
        }
        executorService.shutdown();
    }

    /**
     * ����̲߳��м�������
     */
    public static class ExcelRunner implements Runnable {

        private CyclicBarrier cyclicBarrier;
        private ArrayBlockingQueue arrayBlockingQueue;
        private String name;

        public ExcelRunner(CyclicBarrier cyclicBarrier, ArrayBlockingQueue arrayBlockingQueue, String name) {
            this.cyclicBarrier = cyclicBarrier;
            this.arrayBlockingQueue = arrayBlockingQueue;
            this.name = name;
        }

        @Override
        public void run() {
            Integer data = RandomUtils.nextInt(100, 5000);
            try {
                arrayBlockingQueue.put(data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                // 
                cyclicBarrier.await();
                System.out.println(MessageFormat.format("{0} produce data is {1}", new Object[]{name, data}));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * �������߳�ִ����Ϻ�, ����߳�ͳ����������
     */
    public static class TotalRunner implements Runnable {

        private String name;
        private ArrayBlockingQueue arrayBlockingQueue;

        public TotalRunner(String name, ArrayBlockingQueue arrayBlockingQueue) {
            this.name = name;
            this.arrayBlockingQueue = arrayBlockingQueue;
        }

        @Override
        public void run() {
            System.out.println(MessageFormat.format("{0} start ...", new Object[]{name}));
            Integer t = 0;
            for (Iterator it = arrayBlockingQueue.iterator(); it.hasNext();) {
                Object object = it.next();
                t += Integer.parseInt(object.toString());
            }
            System.out.println(MessageFormat.format("{0} sum is {1}", new Object[]{name, t}));
        }
    }
}
