package self.explore.multithreads.interestingproblems.problem1.solution1;


import java.util.concurrent.CountDownLatch;


/*
 * Q: a water pool has birds and fishes
 * each bird produces a child every 60s, each fish produces 2 children every 30s
 * bird can eat a fish every 10s
 * initiate some fishes and birds, how long takes birds eating all fishes?
 * */
public class AboutFishBird {
 
    public static void main(String[] args) throws Exception {
        simulate(290, 50);
    }
 
    private static void simulate(int initFish, int initBird) throws Exception {
        World world = new World();
        world.fishNum = initFish;
        world.birdNum = initBird;
 
        FishKeeper fk = new FishKeeper(world);
        BirdKeeper bk = new BirdKeeper(world);
        fk.start();
        bk.start();
        Thread.sleep(100); // �ȴ������߳��������
 
        while (world.fishNum >= world.birdNum) {
            synchronized (world) { // �����߳̽���wait()ǰ������ʱ�䲻�Ὺʼ����
                world.token = new CountDownLatch(2);
                world.time += 10; // ����ʱ����ǰ����
                world.notifyAll(); // ֪ͨ�����߳̿��Թ�����
            }
            world.token.await(); // �����󣬵ȴ������߳���ɱ�������ʱ��ļ������
            System.out.println("[" + world.time + "] fish:" + world.fishNum + ", bird:" + world.birdNum);
        }
        System.exit(0);
    }
}
 
/**
 * ���绷��
 */
class World {
    long time;
    int fishNum;
    int birdNum;
    CountDownLatch token;
}
 
/**
 * ��Ⱥ������
 */
class FishKeeper extends Thread {
    private World world;
 
    FishKeeper(World world) {
        super("FishKeeper");
        this.world = world;
    }
 
    public void run() {
        synchronized (world) { // ��ֹ�����޸ĳ�ͻ
            while (true) {
                try {
                    world.wait(); // �ȴ����߳�֪ͨʱ���Ѿ�����
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                	if (world.time % 30 == 0) { // ÿ30����С��
	                    world.fishNum += 2 * world.fishNum;
	                }
	                world.token.countDown(); // ��¼���������Ѿ����
                }
                
            }
        }
    }
}
 
/**
 * ��Ⱥ������
 */
class BirdKeeper extends Thread {
    private World world;
 
    BirdKeeper(World world) {
        super("BirdKeeper");
        this.world = world;
    }
 
    public void run() {
        synchronized (world) { // ��ֹ�����޸ĳ�ͻ
            while (true) {
                try {
                    world.wait(); // �ȴ����߳�֪ͨʱ���Ѿ�����
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                	if (world.time % 10 == 0) {
	                    world.fishNum -= world.birdNum; // ÿ10���һ����
	                    if (world.time % 60 == 0) {
	                        world.birdNum += world.birdNum; // ÿ60����С��
	                    }
	                }
	                world.token.countDown(); // ��¼���������Ѿ����
                }
                
            }
        }
    }
}