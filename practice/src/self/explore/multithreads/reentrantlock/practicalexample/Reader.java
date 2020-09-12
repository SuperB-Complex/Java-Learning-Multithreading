package self.explore.multithreads.reentrantlock.practicalexample;

import java.util.Random;

public class Reader extends Thread {
	
	private ReadWriteList<Integer> sharedList;
	 
    public Reader(ReadWriteList<Integer> sharedList, String name) {
    	super(name);
        this.sharedList = sharedList;
    }
 
    public void run() {
        Random random = new Random();
        int index = random.nextInt(sharedList.size());
        Integer number = sharedList.get(index);
 
        System.out.println(getName() + " -> get: " + number);
 
        try {
            Thread.sleep(20);
        } catch (InterruptedException ie ) { ie.printStackTrace(); }
 
    }
}
