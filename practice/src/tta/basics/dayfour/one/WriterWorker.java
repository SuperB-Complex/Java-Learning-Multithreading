package tta.basics.dayfour.one;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class WriterWorker implements Runnable {
	private static final String END = "\t\n";
	
	private static ArrayBlockingQueue<String> buffer;
	private static BufferedWriter bufferedWriter;
	private volatile static WriterWorker writerWorker = null;
	public volatile static AtomicInteger count = new AtomicInteger(0);
	
	private void setBuffer(ArrayBlockingQueue<String> bf) {
		buffer = bf;
		return;
	}
	
	private void setBufferWriter(BufferedWriter bfw) {
		bufferedWriter = bfw;
		return;
	}
	
	private WriterWorker() {}
	
	public static WriterWorker getInstance(ArrayBlockingQueue<String> bf, BufferedWriter bfw) {
		buffer = bf;
		bufferedWriter = bfw;
		if (writerWorker == null) {
			synchronized(ReaderWorker.class) {
				if (writerWorker == null) {
					writerWorker = new WriterWorker();
					writerWorker.setBuffer(bf);
					writerWorker.setBufferWriter(bfw);
				}
			}
		}
		return writerWorker;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted() && true) {
				String equation = buffer.take();
				equation += " " + "= " + String.valueOf(Calculator.compute(equation));
				String result = equation + END;
				bufferedWriter.write(result);
				bufferedWriter.write(END);
				count.getAndIncrement();
				System.out.println(Thread.currentThread().getName() + " " + " outputs " + equation + " to file.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("Writer worker finishes.");
		} finally {
			try {
				bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return;
	}

}
