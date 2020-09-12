package self.explore.multithreads.mergesort.solution1;


import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * generate random number
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

public class MergeSortMultithreads {
	
	public static void main (String[] args) throws InterruptedException {
        int length = 1000;
        int[] data = (new Data(length)).getData();
        printArr(data);
        System.out.println();
        // mergeSort(data);
        //在这里修改
        int center = data.length/2;

        int[] tmp = new int[data.length];
        // every time main thread should wait for 2 child thread to finish
        CountDownLatch latch = new CountDownLatch(2);
        new Thread(new Runnable(){
        
            @Override
            public void run() {
                mergeSort(data,tmp,0,center);
                latch.countDown();
            }
        }).start();

        new Thread(new Runnable(){
        
            @Override
            public void run() {
                mergeSort(data,tmp,center+1,data.length-1);
                latch.countDown();
            }
        }).start();

        latch.await();

        merge(data, tmp, 0, center+1, data.length-1);

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
}
