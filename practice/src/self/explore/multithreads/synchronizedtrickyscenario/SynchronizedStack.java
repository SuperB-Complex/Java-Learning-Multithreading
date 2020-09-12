package self.explore.multithreads.synchronizedtrickyscenario;

import java.util.LinkedList;

public class SynchronizedStack {
	
	LinkedList list = new LinkedList();
	
	public synchronized void push(Object x) {
		synchronized(list) {
			list.addLast( x );
			// can short as notify();
			this.notify();
			// whether you add the below line of code or not
			// it doesn't change the fact that 
			// if you pop out from an empty stack first
			// you will get deadlock anyway
			// because the current thread is probably waiting on 
			// this lock, so current thread didn't even go inside this method
			// list.notify();
		}
	}
	// wait method can release the lock it holds
	public synchronized Object pop() throws Exception {
		synchronized(list) {
			if(list.size() <= 0) {
				// it doesn't matter if you call list.wait();
				// assume that you call this.wait() first then list.wait()
				// because, once you called this.wait();
				// your thread goes to waiting it doesn't go down until notified
				// so always holds list lock
				// assume you call list.wait() first then this.wait()
				// it doesn't work because the first lock another thread need is this lock
				this.wait();
				
			}
			// can short as notify();
			// whether you add the below line of code or not
			// it doesn't change the fact that 
			// if you pop out from an empty stack first
			// you will get deadlock anyway
			// because you didn't get out from the if statement waiting status
			// this.notify();
		}
		// if you write list.notify(); here
		// you will get java.lang.IllegalMonitorStateException
		// because you are out of the synchronized scope
		return list.removeLast();
	}
}
