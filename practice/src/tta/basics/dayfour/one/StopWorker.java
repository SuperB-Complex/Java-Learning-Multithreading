package tta.basics.dayfour.one;

import java.util.concurrent.CountDownLatch;

public class StopWorker implements Runnable {
	
	private static Thread target;
	private static Thread reader;
	private static Thread writer;
	private static CountDownLatch barrier;
	private static volatile StopWorker stopWorker = null;
	
	private static void setTarget(Thread tg) {
		target = tg;
		return;
	}
	
	private static void setReader(Thread rd) {
		reader = rd;
		return;
	}
	
	private static void setWriter(Thread wt) {
		writer = wt;
		return;
	}
	
	private static void setBarrier(CountDownLatch br) {
		barrier = br;
		return;
	}
	
	private StopWorker() {}
	
	public static StopWorker getInstance(Thread target, Thread reader, Thread writer, CountDownLatch barrier) {
		if (stopWorker == null) {
			synchronized(StopWorker.class) {
				if (stopWorker == null) {
					stopWorker = new StopWorker();
					setTarget(target);
					setReader(reader);
					setWriter(writer);
					setBarrier(barrier);
				}
			}
		}
		return stopWorker;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			barrier.await();
			while (ReaderWorker.count.get() != WriterWorker.count.get()) {}
			target.interrupt();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

}
