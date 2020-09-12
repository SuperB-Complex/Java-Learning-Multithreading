package self.explore.multithreads.systemoutprintln.threadnotsafe;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/*
 * cause System.out.println will be a thread safe operation if you pass in a constant
 * if passing in an expression, one line java code will be converted into several lines 
 * java byte code which results in thread safe concerns
 * */

class MyRunnable implements Runnable {
	private final AtomicLong aLong = new AtomicLong(10000); // free to every thread
	private String name;
	private int data;
 
	public MyRunnable(String name, int data) {
		this.name = name;
		this.data = data;
	}
	
	@Override
	public void run() {
		Thread.yield();
		System.out.println(name + "executes " + data + ", current left " + aLong.addAndGet(data));
	}
}

public class Test {
		
	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(2);
		Runnable t1 = new MyRunnable("A1", 2000);
		Runnable t2 = new MyRunnable("A2", 3600);
		Runnable t3 = new MyRunnable("A3", 2700);
		Runnable t4 = new MyRunnable("A4", 600);
		Runnable t5 = new MyRunnable("A5", 1300);
		Runnable t6 = new MyRunnable("A6", 800);
		// execute
		pool.execute(t1);
		pool.execute(t2);
		pool.execute(t3);
		pool.execute(t4);
		pool.execute(t5);
		pool.execute(t6);
		// close pool
		pool.shutdown();
	}
	
	
}