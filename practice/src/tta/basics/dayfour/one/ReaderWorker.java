package tta.basics.dayfour.one;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ReaderWorker implements Runnable {
	
	private static ArrayBlockingQueue<String> buffer;
	private static BufferedReader bufferedReader;
	private static CountDownLatch barrier;
	private volatile static ReaderWorker readerWorker = null;
	public volatile static AtomicInteger count = new AtomicInteger(0);
	
	private static void setBuffer(ArrayBlockingQueue<String> bf) {
		buffer = bf;
		return;
	}
	
	private static void setBufferReader(BufferedReader bfr) {
		bufferedReader = bfr;
		return;
	}
	
	private static void setBarrier(CountDownLatch br) {
		barrier = br;
		return;
	}
	
	private ReaderWorker() {}
	
	public static ReaderWorker getInstance(ArrayBlockingQueue<String> bf, BufferedReader bfr, CountDownLatch br) {
		buffer = bf;
		bufferedReader = bfr;
		if (readerWorker == null) {
			synchronized(ReaderWorker.class) {
				if (readerWorker == null) {
					readerWorker = new ReaderWorker();
					setBuffer(bf);
					setBufferReader(bfr);
					setBarrier(br);
				}
			}
		}
		return readerWorker;
	}

	@Override
	public void run() {
		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				if (line.length() > 0) {
					buffer.put(line);
					count.getAndIncrement();
					System.out.println(Thread.currentThread().getName() + " " + " puts " + line + " to queue.");
				}
			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				System.out.println("Reader worker finishes.");
				barrier.countDown();
				bufferedReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return;
	}

}
