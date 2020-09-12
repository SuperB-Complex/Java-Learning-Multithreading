package self.explore.multithreads.cyclicbarrier.scenario1;

import org.apache.commons.lang3.RandomUtils;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.concurrent.*;

/*
 * 我们用一个Excel保存了用户所有银行流水，每个Sheet保存一个帐户近一年的每笔银行流水，现在需要统计用户的日均银行流水，
 * 先用多线程处理每个sheet里的银行流水，都执行完之后，得到每个sheet的日均银行流水，最后，再用barrierAction
 * 用这些线程的计算结果，计算出整个Excel的日均银行流水。
 * */

// 只有所有线程的 await() 方法阻塞时, totalRunner 线程才开始执行
// 被 await() 方法阻塞的线程, 在 totalRunner 线程执行完毕后才能继续执行
// CyclicBarrier 中的第一个参数表示被 await() 方法阻塞的线程的数目
public class Test {

    public static void main(String[] args) {
        // 1. 首先获取excel sheet 的个数
        int sheetSize = 3;
        // 2. 初始化队列, 并设置大小, 和 sheet 数目一致, 用于存储每个线程获取到的数据
        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(sheetSize);

        TotalRunner totalRunner = new TotalRunner("total", arrayBlockingQueue);
        // 3. 创建 CyclicBarrier 对象, await 线程数目和 sheet 数目一致
        // totalRunner 用于等所有 await 线程执行完毕后优先执行的逻辑
        CyclicBarrier cyclicBarrier = new CyclicBarrier(sheetSize, totalRunner);

        // 4. 创建线程池, 并设置大小, 和 sheet 数目一致, 用于 await 线程执行
        ExecutorService executorService = Executors.newFixedThreadPool(sheetSize);

        // 5. 提交到线程池中执行
        for (int i = 0; i < sheetSize; i++) {
            executorService.submit(new ExcelRunner(cyclicBarrier, arrayBlockingQueue, String.valueOf(i+1)));
        }
        executorService.shutdown();
    }

    /**
     * 多个线程并行计算数据
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
     * 等所有线程执行完毕后, 这个线程统计最终数据
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
