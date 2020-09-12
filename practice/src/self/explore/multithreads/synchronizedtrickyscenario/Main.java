package self.explore.multithreads.synchronizedtrickyscenario;

/*
 * in a nutshell, you shouldn't have two nested synchronized block;
 * Not recommend to use non-final object as lock, cause if you see heap dump of HotSpot
 * you will know every time JVM checking the lock is to see if it is this object, it comparing
 * the memory location of your lock object. If you use an Integer, an immutable type, and you didn't
 * mark it as final and you change it during running, you will get wrong.
 * This case using LinkedList is working, because the variable list is just a reference, the memory 
 * location is in the heap, so you remove, add to list, it doesn't change the memory location of list variable.
 * */
public class Main {

	public static void main(String[] args) {
		SynchronizedStack stacks = new SynchronizedStack();
		int[] data = {1,2};
		// it is okay 
		// if you push first then pop
//		for (int i : data) {
//			new Thread(() -> stacks.push(i)).start();
//			new Thread(() -> {
//				try {
//					stacks.pop();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}).start();
//		}
		
//		for (int i : data) {
//			new Thread(() -> {
//				try {
//					stacks.pop();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}).start();
//			new Thread(() -> stacks.push(i)).start();
//		}
		
		// with thread name easy to use jstack -l
		for (int i : data) {
			// in anonymous method variable should be final
			// here variable i is not recognized
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						stacks.pop();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}, "thread" + i).start();
			Runnable workerPop = new Runnable() {
				@Override
				public void run() {
					try {
						stacks.pop();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			new Thread(workerPop, "threadPop" + i).start();
			// adding this sleep snippet aims to give time 
			// to threadPop1 to release lock self.explore.multithreads.synchronizedtricky.scenario1.SynchronizedStack
			// so that threadPush1 can get lock self.explore.multithreads.synchronizedtricky.scenario1.SynchronizedStack
			try {
				Thread.sleep(8000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Runnable workerPush = new Runnable() {
				@Override
				public void run() {
					try {
						stacks.push(i);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			new Thread(workerPush, "threadPush" + i).start();
		}
	}
}
