package self.explore.multithreads.systemoutprintln.threadsafe;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class MyRunnable implements Runnable {
	private Lock lock;
	private final AtomicLong aLong = new AtomicLong(10000); // free to every thread
	private String name;
	private int data; 
 
	public MyRunnable(String name, int data, Lock lock) {
		this.name = name;
		this.data = data;
		this.lock = lock;
	}
	
	@Override
	public void run() {
		lock.lock();
		System.out.println(name + "executes " + data + ", current left " + aLong.addAndGet(data));
		lock.unlock();
	}

}

public class Test {
		
	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(2);
		Lock lock = new ReentrantLock(false);
		Runnable t1 = new MyRunnable("A1", 2000, lock);
		Runnable t2 = new MyRunnable("A2", 3600, lock);
		Runnable t3 = new MyRunnable("A3", 2700, lock);
		Runnable t4 = new MyRunnable("A4", 600, lock);
		Runnable t5 = new MyRunnable("A5", 1300, lock);
		Runnable t6 = new MyRunnable("A6", 800, lock);
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

