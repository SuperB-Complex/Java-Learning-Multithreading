package self.explore.multithreads.interestingproblems.problem1.solution2;


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
 
/*
 * Q: a water pool has birds and fishes
 * each bird produces a child every 60s, each fish produces 2 children every 30s
 * bird can eat a fish every 10s
 * initiate some fishes and birds, how long takes birds eating all fishes?
 * */
public class AboutFishBird {
 
	long time ;
	long birdNum ;
	long fishNum ;
	Object lock = new Object() ;
	CyclicBarrier barrier  ;
	
	public AboutFishBird(long birdNum , long fishNum) {
		this.birdNum = birdNum ;
		this.fishNum = fishNum ;
	}
 
	public static void main(String[] args) {
 
		AboutFishBird bf = new AboutFishBird(5 , 20) ;
		bf.start(); 
 
	}
 
	public void start(){
 
		FishThread ft = new FishThread() ;
		BirdThread bt = new BirdThread() ;
		TimeLine tl = new TimeLine() ;
 
		barrier = new CyclicBarrier(2, tl) ;
 
		ft.start();
		bt.start();
 
	}
 
	public void printInfo(){
		System.out.printf("time[%d]:birdNum[%d] ,fishNum[%d]\n" ,time , birdNum , fishNum);
	}
 
	private class TimeLine implements Runnable {
		@Override
		public void run() {	
			if(fishNum <= 0){
				System.exit(-1);   
			}
			time += 10 ;
		}
	}
 
	private class FishThread extends Thread {
		@Override
		public void run() {
			while(true){
				try {
					barrier.await() ;	
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
				synchronized (lock) {
					if(time % 30 == 0){
						fishNum += fishNum * 2;
						printInfo();
					}
					// barrier await can't be put here
					// in a synchronized block you call await
					// then this synchronized block will never end
					// and another thread will not get this lock
					// so deadlock eventually
				}
				// barrier await can't be put here
				// because after synchronized block the execution right
				// could be obtained by another thread
				// so your original aim to start the time line when two 
				// entities have processed will be not achieved
				// cause you didn't await in time
			}
		}
	}
 
	private class BirdThread extends Thread{
 
		@Override
		public void run() {
 
			while(true){
				try {
					barrier.await() ; 
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}  
				synchronized (lock) {
					if(time % 10 == 0){
						fishNum = fishNum >= birdNum ? fishNum - birdNum : 0 ;  
						if(time % 60 == 0){
							birdNum += birdNum ;
						}
						printInfo();
					}

				}
 
			}
 
		}
 
	}
 
}
 