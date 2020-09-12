package self.explore.multithreads.mergesort.solution2;


import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 *  generate random number
 */
class Data{
    int length;
    int[] data;

    public Data(int length){
        this.length = length;
        data = new int[length];
    }

    public int[] getData(){

        Random random = new Random(System.currentTimeMillis());
        for(int i=0;i<length;i++){
            data[i]=random.nextInt(2*length);
        }
        return data;
    }
}

/*
 * ForkJoinPool和线程池有两点非常大的不同之处。
 * 第一点它非常适合执行可以产生子任务的任务。
 * 第二点它内部除了有一个共用的任务队列之外，每个线程还有一个对应的双端队列 deque，这时一旦线程中的任务被 Fork 分裂了，
 * 分裂出来的子任务放入线程自己的 deque 里，而不是放入公共的任务队列中。如果此时有三个子任务放入线程 t1 的 deque 队列中，
 * 对于线程 t1 而言获取任务的成本就降低了，可以直接在自己的任务队列中获取而不必去公共队列中争抢也不会发生阻塞（除了后面会讲到的 steal 情况外），
 * 减少了线程间的竞争和切换，是非常高效的。
 * 
 * 我们再考虑一种情况，此时线程有多个，而线程 t1 的任务特别繁重，分裂了数十个子任务，但是 t0 此时却无事可做，它自己的 deque 队列为空，
 * 这时为了提高效率，t0 就会想办法帮助 t1 执行任务，这就是“work-stealing”的含义。
 * 
 * 双端队列 deque 中，线程 t1 获取任务的逻辑是后进先出，也就是LIFO（Last In Frist Out），
 * 而线程 t0 在“steal”偷线程 t1 的 deque 中的任务的逻辑是先进先出，也就是FIFO（Fast In Frist Out），
 * 使用 “work-stealing” 算法和双端队列很好地平衡了各线程的负载。
 * */

/*
 * 使用Fork/Join 我们需要知道两个类：
 *
 *	ForkJoinTask：我们要使用ForkJoin框架，必须首先创建一个ForkJoin任务。
 *	它提供在任务中执行fork()和join()操作的机制，通常情况下我们不需要直接继承ForkJoinTask类，
 *	而只需要继承它的子类，Fork/Join框架提供了以下两个子类：
 *	RecursiveAction：用于没有返回结果的任务。
 *	RecursiveTask ：用于有返回结果的任务。
 *	ForkJoinPool ：ForkJoinTask需要通过ForkJoinPool来执行，
 *	任务分割出的子任务会添加到当前工作线程所维护的双端队列中，进入队列的头部。
 *	当一个工作线程的队列里暂时没有任务时，它会随机从其他工作线程的队列的尾部获取一个任务。
 * */

public class MergeSortForkJoin {
	public static void main (String[] args) throws InterruptedException {
        int length = 1000;
        int[] data = (new Data(length)).getData();
        printArr(data);
        System.out.println();
        // mergeSort(data);
        //在这里修改
        // int center = data.length/2;

        int[] tmp = new int[data.length];
        // every time main thread should wait for 2 child thread to finish
        // CountDownLatch latch = new CountDownLatch(2);
        // new Thread(new Runnable(){
        
        //     @Override
        //     public void run() {
        //         mergeSort(data,tmp,0,center);
        //         latch.countDown();
        //     }
        // }).start();

        // new Thread(new Runnable(){
        
        //     @Override
        //     public void run() {
        //         mergeSort(data,tmp,center+1,data.length-1);
        //         latch.countDown();
        //     }
        // }).start();

        // latch.await();

        // merge(data, tmp, 0, center+1, data.length-1);

        //Fork/Join 从这里开始
        // ForkJoinPool(): 
        // creates a default pool that creates threads equal to the number of processors available in the system.
        // ForkJoinPool(int parallelism): 
        // creates a pool with a greater than 0 and not more than the actual number of processors available.
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        MergeSortForkJoin.mergeTask task = new MergeSortForkJoin.mergeTask(data, tmp, 0, data.length-1);//创建任务
        forkJoinPool.execute(task);//执行任务
        forkJoinPool.awaitTermination(2, TimeUnit.SECONDS);//阻塞当前线程直到pool中的任务都完成了

        printArr(data);
        System.out.println();
        verify(data);

    }

    //递归
    private static void mergeSort(int[] nums,int[] tmp,int left,int right){
        if(left<right){
            int center = (left+right)/2;
            mergeSort(nums,tmp,left,center);
            mergeSort(nums,tmp,center+1,right);
            merge(nums,tmp,left,center+1,right);
        }
    }

    //合并
    private static void merge(int[] nums,int[] tmp,int leftPos, int rightPos, int rightEnd){
        int leftEnd = rightPos-1;
        int tmpPos = leftPos;
        int numElements = rightEnd - leftPos + 1;
    
        while(leftPos<=leftEnd&&rightPos<=rightEnd){
            if(nums[leftPos]<nums[rightPos])
                tmp[tmpPos++]=nums[leftPos++];
            else 
                tmp[tmpPos++]=nums[rightPos++];
        }
        while(leftPos<=leftEnd)
            tmp[tmpPos++]=nums[leftPos++];
        
        while(rightPos<=rightEnd)
            tmp[tmpPos++]=nums[rightPos++];
    
        for(int i = 0;i<numElements;i++,rightEnd--)
            nums[rightEnd]=tmp[rightEnd];
    }
    
    public static void mergeSort(int[] nums){
        int[] tmp = new int[nums.length];
        mergeSort(nums,tmp,0,nums.length-1);
    }
    
    //打印
    public static void printArr(int[] arr) {
        for(int i : arr){
            System.out.print(i+" ");
        }
    }
    
    public static void verify(int[] nums) {
        for(int i=0;i<nums.length-1;i++){
            if(nums[i]>nums[i+1]){
                System.out.println("排序失败");
                return;
            } 

        }
        System.out.println("排序成功");
    }
    

    static class mergeTask extends RecursiveAction {
        private static final int THRESHOLD = 2;//设置任务大小阈值
        private int start;
        private int end;
        private int[] data;
        private int[] tmp;
    
        public mergeTask(int[] data, int[] tmp, int start, int end){
            this.data = data;
            this.tmp = tmp;
            this.start = start;
            this.end = end;
        }
    
        @Override
        protected void compute(){
            if((end - start)<=THRESHOLD){
                mergeSort(data,tmp,start,end);
            }else{
            	// divide task into smaller task until reaches THRESHOLD
                int center = (start + end)/2;
                MergeSortForkJoin.mergeTask leftTask = new MergeSortForkJoin.mergeTask(data, tmp, start, center);
                MergeSortForkJoin.mergeTask rightTask = new MergeSortForkJoin.mergeTask(data, tmp, center+1, end);
                
                // basically push this left task to the work queue maintained current thread
                leftTask.fork();
                // similar to right task 
                rightTask.fork();

                // join() will call ForkJoinTask.exec()[RecursiveAction.exec() will call compute()]
                leftTask.join();
                rightTask.join();

                merge(data, tmp, start, center+1, end);

            }
        }
    }
}

/*
 * ForkJoinPool 的每个工作线程都维护着一个工作队列（WorkQueue），这是一个双端队列（Deque），里面存放的对象是任务（ForkJoinTask）。
每个工作线程在运行中产生新的任务（通常是因为调用了 fork()）时，会放入工作队列的队尾，并且工作线程在处理自己的工作队列时，使用的是 LIFO 方式，
也就是说每次从队尾取出任务来执行。
每个工作线程在处理自己的工作队列同时，会尝试窃取一个任务（或是来自于刚刚提交到 pool 的任务，或是来自于其他工作线程的工作队列），
窃取的任务位于其他线程的工作队列的队首，也就是说工作线程在窃取其他工作线程的任务时，使用的是 FIFO 方式。
在遇到 join() 时，如果需要 join 的任务尚未完成，则会先处理其他任务，并等待其完成。
在既没有自己的任务，也没有可以窃取的任务时，进入休眠。


fork() 做的工作只有一件事，既是把任务推入当前工作线程的工作队列里。

join() 的工作则复杂得多，也是 join() 可以使得线程免于被阻塞的原因——不像同名的 Thread.join()。

检查调用 join() 的线程是否是 ForkJoinThread 线程。如果不是（例如 main 线程），则阻塞当前线程，等待任务完成。如果是，则不阻塞。
查看任务的完成状态，如果已经完成，直接返回结果。
如果任务尚未完成，但处于自己的工作队列内，则完成它。
如果任务已经被其他的工作线程偷走，则窃取这个小偷的工作队列内的任务（以 FIFO 方式），执行，以期帮助它早日完成欲 join 的任务。
如果偷走任务的小偷也已经把自己的任务全部做完，正在等待需要 join 的任务时，则找到小偷的小偷，帮助它完成它的任务。
递归地执行第5步。



在Fork-Join中，比如一个拥有4个线程的ForkJoinPool线程池，有一个任务队列，一个大的任务切分出的子任务会提交到线程池的任务队列中，
4个线程从任务队列中获取任务执行，哪个线程执行的任务快，哪个线程执行的任务就多，只有队列中没有任务线程才是空闲的，这就是工作窃取。
可以这样理解工作窃取，比如有4个人干8件事情，理应每个人干2件，但干活快的干完自己的事情后可以去帮别人干。

正如图中所示，一个任务可以fork中很多个子任务，当然不只是图中看到的只有左右两个。假设，每个任务都只fork出两个子任务，
如果负责fork子任务的当前任务不做任何事情，那么最终将只有叶子节点真正做事情，其它节点都只是负责fork子任务与合并结果
（假设是有返回值的任务）。

如果是没有返回值的任务，是没有图中“合并结果”这个流程的；而且，也不是必须要等待子任务执行完成。这些都是根据自己的需求来自定义使用的。
要灵活去使用。


假设，分布式服务中（rpx框架：dubbo），有一个接口，用于批量处理数据，如果每次消费者调用都用了批量处理1000条记录的过滤，
假设一条记录的过滤逻辑需要耗时4ms（ 涉及到redis缓存的读），如果有40个请求并发过滤，那就是40000条记录交给2个线程去处理(cpu核心线程数)，
你猜下结果是什么？结果是，服务消费端报错，一堆的接口调用超时异常，导致服务雪崩。后果很严重。原因你猜到了吗？

40个请求开启40个并行流parallerStream，40个并行流parallerStream使用同一个只有2个线程的Fork-Join线程池(2核8g机器)，
意味着40个请求争抢着执行任务。

假设一条记录的过滤耗时为4ms，在串行的情况下1000条记录应该只是4000ms。但如果是400000条记录争抢2个线程执行，我们转变一下，
假设每线程每200000记录执行，由于是无序的，但可以想象对请求来说任务是被交替执行完成的。什么意思呢，比如当前执行1号请求的第一个任务，
执行完后切换到2号个请求的第一个任务，接着3号请求的第一个任务，一轮完成后接着是1号请求的第二个任务...所以，最坏的情况下，
一个请求需要200000*4ms才能执行完成。就会导致接口调用超时。

总之，不要在高并发的接口中使用并行流，直接使用处理请求的线程执行就行，如果有需要，那就全局创建一个Fork-Join线程池自己切分任务来执行。

刚刚说的例子只是40个并发，实现项目中都是上千上万的并发请求，如果这样使用并行流，服务直接崩掉。

假设用的dubbo默认配置200个工作线程，那么是200个线程处理业务逻辑快呢，还是将200个线程的请求都交给只有2个线程的线程池处理快呢？毫无疑问。

总结:
那些耗时很长的任务，请不要使用parallerStream。假设原本一个任务执行需要1分钟时间，有10个任务并行执行，如果你偷懒，
只是使用parallerStream来将这10个任务并行执行，那你这个jvm进程中，其它同样使用parallerStream的地方也会因此被阻塞住，
严重的将会导致整个服务瘫痪。
 * 
 * */
