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
 * ForkJoinPool���̳߳�������ǳ���Ĳ�֮ͬ����
 * ��һ�����ǳ��ʺ�ִ�п��Բ��������������
 * �ڶ������ڲ�������һ�����õ��������֮�⣬ÿ���̻߳���һ����Ӧ��˫�˶��� deque����ʱһ���߳��е����� Fork �����ˣ�
 * ���ѳ���������������߳��Լ��� deque ������Ƿ��빫������������С������ʱ����������������߳� t1 �� deque �����У�
 * �����߳� t1 ���Ի�ȡ����ĳɱ��ͽ����ˣ�����ֱ�����Լ�����������л�ȡ������ȥ��������������Ҳ���ᷢ�����������˺���ὲ���� steal ����⣩��
 * �������̼߳�ľ������л����Ƿǳ���Ч�ġ�
 * 
 * �����ٿ���һ���������ʱ�߳��ж�������߳� t1 �������ر��أ���������ʮ�������񣬵��� t0 ��ʱȴ���¿��������Լ��� deque ����Ϊ�գ�
 * ��ʱΪ�����Ч�ʣ�t0 �ͻ���취���� t1 ִ����������ǡ�work-stealing���ĺ��塣
 * 
 * ˫�˶��� deque �У��߳� t1 ��ȡ������߼��Ǻ���ȳ���Ҳ����LIFO��Last In Frist Out����
 * ���߳� t0 �ڡ�steal��͵�߳� t1 �� deque �е�������߼����Ƚ��ȳ���Ҳ����FIFO��Fast In Frist Out����
 * ʹ�� ��work-stealing�� �㷨��˫�˶��кܺõ�ƽ���˸��̵߳ĸ��ء�
 * */

/*
 * ʹ��Fork/Join ������Ҫ֪�������ࣺ
 *
 *	ForkJoinTask������Ҫʹ��ForkJoin��ܣ��������ȴ���һ��ForkJoin����
 *	���ṩ��������ִ��fork()��join()�����Ļ��ƣ�ͨ����������ǲ���Ҫֱ�Ӽ̳�ForkJoinTask�࣬
 *	��ֻ��Ҫ�̳��������࣬Fork/Join����ṩ�������������ࣺ
 *	RecursiveAction������û�з��ؽ��������
 *	RecursiveTask �������з��ؽ��������
 *	ForkJoinPool ��ForkJoinTask��Ҫͨ��ForkJoinPool��ִ�У�
 *	����ָ�������������ӵ���ǰ�����߳���ά����˫�˶����У�������е�ͷ����
 *	��һ�������̵߳Ķ�������ʱû������ʱ��������������������̵߳Ķ��е�β����ȡһ������
 * */

public class MergeSortForkJoin {
	public static void main (String[] args) throws InterruptedException {
        int length = 1000;
        int[] data = (new Data(length)).getData();
        printArr(data);
        System.out.println();
        // mergeSort(data);
        //�������޸�
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

        //Fork/Join �����￪ʼ
        // ForkJoinPool(): 
        // creates a default pool that creates threads equal to the number of processors available in the system.
        // ForkJoinPool(int parallelism): 
        // creates a pool with a greater than 0 and not more than the actual number of processors available.
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        MergeSortForkJoin.mergeTask task = new MergeSortForkJoin.mergeTask(data, tmp, 0, data.length-1);//��������
        forkJoinPool.execute(task);//ִ������
        forkJoinPool.awaitTermination(2, TimeUnit.SECONDS);//������ǰ�߳�ֱ��pool�е����������

        printArr(data);
        System.out.println();
        verify(data);

    }

    //�ݹ�
    private static void mergeSort(int[] nums,int[] tmp,int left,int right){
        if(left<right){
            int center = (left+right)/2;
            mergeSort(nums,tmp,left,center);
            mergeSort(nums,tmp,center+1,right);
            merge(nums,tmp,left,center+1,right);
        }
    }

    //�ϲ�
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
    
    //��ӡ
    public static void printArr(int[] arr) {
        for(int i : arr){
            System.out.print(i+" ");
        }
    }
    
    public static void verify(int[] nums) {
        for(int i=0;i<nums.length-1;i++){
            if(nums[i]>nums[i+1]){
                System.out.println("����ʧ��");
                return;
            } 

        }
        System.out.println("����ɹ�");
    }
    

    static class mergeTask extends RecursiveAction {
        private static final int THRESHOLD = 2;//���������С��ֵ
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
 * ForkJoinPool ��ÿ�������̶߳�ά����һ���������У�WorkQueue��������һ��˫�˶��У�Deque���������ŵĶ���������ForkJoinTask����
ÿ�������߳��������в����µ�����ͨ������Ϊ������ fork()��ʱ������빤�����еĶ�β�����ҹ����߳��ڴ����Լ��Ĺ�������ʱ��ʹ�õ��� LIFO ��ʽ��
Ҳ����˵ÿ�δӶ�βȡ��������ִ�С�
ÿ�������߳��ڴ����Լ��Ĺ�������ͬʱ���᳢����ȡһ�����񣨻��������ڸո��ύ�� pool �����񣬻������������������̵߳Ĺ������У���
��ȡ������λ�������̵߳Ĺ������еĶ��ף�Ҳ����˵�����߳�����ȡ���������̵߳�����ʱ��ʹ�õ��� FIFO ��ʽ��
������ join() ʱ�������Ҫ join ��������δ��ɣ�����ȴ����������񣬲��ȴ�����ɡ�
�ڼ�û���Լ�������Ҳû�п�����ȡ������ʱ���������ߡ�


fork() ���Ĺ���ֻ��һ���£����ǰ��������뵱ǰ�����̵߳Ĺ��������

join() �Ĺ������ӵö࣬Ҳ�� join() ����ʹ���߳����ڱ�������ԭ�򡪡�����ͬ���� Thread.join()��

������ join() ���߳��Ƿ��� ForkJoinThread �̡߳�������ǣ����� main �̣߳�����������ǰ�̣߳��ȴ�������ɡ�����ǣ���������
�鿴��������״̬������Ѿ���ɣ�ֱ�ӷ��ؽ����
���������δ��ɣ��������Լ��Ĺ��������ڣ����������
��������Ѿ��������Ĺ����߳�͵�ߣ�����ȡ���С͵�Ĺ��������ڵ������� FIFO ��ʽ����ִ�У����ڰ�������������� join ������
���͵�������С͵Ҳ�Ѿ����Լ�������ȫ�����꣬���ڵȴ���Ҫ join ������ʱ�����ҵ�С͵��С͵�������������������
�ݹ��ִ�е�5����



��Fork-Join�У�����һ��ӵ��4���̵߳�ForkJoinPool�̳߳أ���һ��������У�һ����������зֳ�����������ύ���̳߳ص���������У�
4���̴߳���������л�ȡ����ִ�У��ĸ��߳�ִ�е�����죬�ĸ��߳�ִ�е�����Ͷֻ࣬�ж�����û�������̲߳��ǿ��еģ�����ǹ�����ȡ��
����������⹤����ȡ��������4���˸�8�����飬��Ӧÿ���˸�2�������ɻ��ĸ����Լ�����������ȥ����˸ɡ�

����ͼ����ʾ��һ���������fork�кܶ�������񣬵�Ȼ��ֻ��ͼ�п�����ֻ���������������裬ÿ������ֻfork������������
�������fork������ĵ�ǰ�������κ����飬��ô���ս�ֻ��Ҷ�ӽڵ����������飬�����ڵ㶼ֻ�Ǹ���fork��������ϲ����
���������з���ֵ�����񣩡�

�����û�з���ֵ��������û��ͼ�С��ϲ������������̵ģ����ң�Ҳ���Ǳ���Ҫ�ȴ�������ִ����ɡ���Щ���Ǹ����Լ����������Զ���ʹ�õġ�
Ҫ���ȥʹ�á�


���裬�ֲ�ʽ�����У�rpx��ܣ�dubbo������һ���ӿڣ����������������ݣ����ÿ�������ߵ��ö�������������1000����¼�Ĺ��ˣ�
����һ����¼�Ĺ����߼���Ҫ��ʱ4ms�� �漰��redis����Ķ����������40�����󲢷����ˣ��Ǿ���40000����¼����2���߳�ȥ����(cpu�����߳���)��
����½����ʲô������ǣ��������Ѷ˱���һ�ѵĽӿڵ��ó�ʱ�쳣�����·���ѩ������������ء�ԭ����µ�����

40��������40��������parallerStream��40��������parallerStreamʹ��ͬһ��ֻ��2���̵߳�Fork-Join�̳߳�(2��8g����)��
��ζ��40������������ִ������

����һ����¼�Ĺ��˺�ʱΪ4ms���ڴ��е������1000����¼Ӧ��ֻ��4000ms���������400000����¼����2���߳�ִ�У�����ת��һ�£�
����ÿ�߳�ÿ200000��¼ִ�У�����������ģ������������������˵�����Ǳ�����ִ����ɵġ�ʲô��˼�أ����統ǰִ��1������ĵ�һ������
ִ������л���2�Ÿ�����ĵ�һ�����񣬽���3������ĵ�һ������һ����ɺ������1������ĵڶ�������...���ԣ��������£�
һ��������Ҫ200000*4ms����ִ����ɡ��ͻᵼ�½ӿڵ��ó�ʱ��

��֮����Ҫ�ڸ߲����Ľӿ���ʹ�ò�������ֱ��ʹ�ô���������߳�ִ�о��У��������Ҫ���Ǿ�ȫ�ִ���һ��Fork-Join�̳߳��Լ��з�������ִ�С�

�ո�˵������ֻ��40��������ʵ����Ŀ�ж�����ǧ����Ĳ��������������ʹ�ò�����������ֱ�ӱ�����

�����õ�dubboĬ������200�������̣߳���ô��200���̴߳���ҵ���߼����أ����ǽ�200���̵߳����󶼽���ֻ��2���̵߳��̳߳ش�����أ��������ʡ�

�ܽ�:
��Щ��ʱ�ܳ��������벻Ҫʹ��parallerStream������ԭ��һ������ִ����Ҫ1����ʱ�䣬��10��������ִ�У������͵����
ֻ��ʹ��parallerStream������10��������ִ�У��������jvm�����У�����ͬ��ʹ��parallerStream�ĵط�Ҳ����˱�����ס��
���صĽ��ᵼ����������̱����
 * 
 * */
