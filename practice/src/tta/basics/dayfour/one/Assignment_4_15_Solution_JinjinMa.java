package tta.basics.dayfour.one;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Assignment_4_15_Solution_JinjinMa {
	static BufferedReader bufferedReader;
	static BufferedWriter bufferedWriter;
	static {
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/Eclippse_Workspace/practice/src/tta/basics/dayfour/one/input.txt")), "UTF-8"));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:/Eclippse_Workspace/practice/src/tta/basics/dayfour/one/output.txt")), "UTF-8"));
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static final ArrayBlockingQueue<String> buffer = new ArrayBlockingQueue<> (100000, true);
	static final CountDownLatch barrier = new CountDownLatch(1);
		
	public static void main(String[] args) {
		Thread mainThread = Thread.currentThread();
		Thread readerWorker = new Thread(ReaderWorker.getInstance(Assignment_4_15_Solution_JinjinMa.buffer, Assignment_4_15_Solution_JinjinMa.bufferedReader, Assignment_4_15_Solution_JinjinMa.barrier), "readerWorker");
		Thread writerWorker = new Thread(WriterWorker.getInstance(Assignment_4_15_Solution_JinjinMa.buffer, Assignment_4_15_Solution_JinjinMa.bufferedWriter), "writerWorker");
		Thread stopWorker = new Thread(StopWorker.getInstance(mainThread, readerWorker, writerWorker, Assignment_4_15_Solution_JinjinMa.barrier), "stopWorker");
		System.out.println("Reader worker and writer worker are going to start. ");
		readerWorker.start();
		writerWorker.start();
		stopWorker.start();
		try {
			readerWorker.join();
			writerWorker.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			writerWorker.interrupt();
		}
		System.out.println("Reader worker and writer worker finish. ");

	}
}
