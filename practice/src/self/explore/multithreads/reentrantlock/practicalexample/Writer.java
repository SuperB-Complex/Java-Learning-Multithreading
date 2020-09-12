package self.explore.multithreads.reentrantlock.practicalexample;

import java.util.Random;

public class Writer extends Thread {
	
	private ReadWriteList<Integer> sharedList;
	 
    public Writer(ReadWriteList<Integer> sharedList, String name) {
    	super(name);
        this.sharedList = sharedList;
    }
 
    public void run() {
        Random random = new Random();
        int number = random.nextInt(100);
        sharedList.add(number, 3);
 
        try {
            Thread.sleep(10);
            System.out.println(getName() + " -> put: " + number);
        } catch (InterruptedException ie ) { ie.printStackTrace(); }
    }
}
