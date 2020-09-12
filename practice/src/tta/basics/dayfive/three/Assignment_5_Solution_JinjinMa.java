package tta.basics.dayfive.three;

public class Assignment_5_Solution_JinjinMa {
	
	/*
	 *  My understanding of this question is the we have two lines, 
	 *  one is going left and one is going right. And we have one bridge
	 *  which only allows one car to pass through at any time.
	 *  
	 *  In reality, the queue is increasing infinitely.
	 *  To simulate a simplified version of it, either we set a limit on the 
	 *  total number of cars will pass or the time limit. We can have two 
	 *  thread pools representing going left or going right. When the limit
	 *  is reached, two thread pools will be terminated.
	 * 
	 *  To avoid deadlock, we use one semaphore to simulate one available
	 *  car allowed to pass the bridge;
	 *  To avoid starvation, we switch the executing right between two sides
	 *  after random number of one side cars passing through. And there are 
	 *  many ways to implement it. 
	 *  1. Condition to wait and signify the other side when one car passed;
	 *  2. Use one semaphore to simulate one available car allowed to pass 
	 *     the bridge. I don't know if I can control the execution right between
	 *     two thread pools. Let's see then.
	 * 
	 * */
	public static void main(String[] args) {
		
	}
}
