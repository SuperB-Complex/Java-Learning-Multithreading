package self.explore.multithreads.reentrantlock.practicalexample;

public class Main {
	
	static final int READER_SIZE = 100;
    static final int WRITER_SIZE = 100;
 
    public static void main(String[] args) {
        Integer[] initialElements = {33, 28, 86, 99};
 
        ReadWriteList<Integer> sharedList = new ReadWriteList<>(initialElements);
 
        for (int i = 0; i < WRITER_SIZE; i++) {
            new Writer(sharedList, "writer" + i).start();
            new Reader(sharedList, "reader" + i).start();
        }
 
        for (int i = 0; i < READER_SIZE; i++) {
            
        }
 
    }
}
