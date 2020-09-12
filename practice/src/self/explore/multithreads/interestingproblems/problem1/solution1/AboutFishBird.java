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
        Thread.sleep(100); // 等待两个线程启动完毕
 
        while (world.fishNum >= world.birdNum) {
            synchronized (world) { // 在子线程进入wait()前，世界时间不会开始流逝
                world.token = new CountDownLatch(2);
                world.time += 10; // 世界时间向前流逝
                world.notifyAll(); // 通知两个线程可以工作了
            }
            world.token.await(); // 跳动后，等待两个线程完成本次流逝时间的计算过程
            System.out.println("[" + world.time + "] fish:" + world.fishNum + ", bird:" + world.birdNum);
        }
        System.exit(0);
    }
}
 
/**
 * 世界环境
 */
class World {
    long time;
    int fishNum;
    int birdNum;
    CountDownLatch token;
}
 
/**
 * 鱼群管理器
 */
class FishKeeper extends Thread {
    private World world;
 
    FishKeeper(World world) {
        super("FishKeeper");
        this.world = world;
    }
 
    public void run() {
        synchronized (world) { // 防止并发修改冲突
            while (true) {
                try {
                    world.wait(); // 等待主线程通知时间已经流逝
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                	if (world.time % 30 == 0) { // 每30秒生小鱼
	                    world.fishNum += 2 * world.fishNum;
	                }
	                world.token.countDown(); // 记录工作计算已经完成
                }
                
            }
        }
    }
}
 
/**
 * 鸟群管理器
 */
class BirdKeeper extends Thread {
    private World world;
 
    BirdKeeper(World world) {
        super("BirdKeeper");
        this.world = world;
    }
 
    public void run() {
        synchronized (world) { // 防止并发修改冲突
            while (true) {
                try {
                    world.wait(); // 等待主线程通知时间已经流逝
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                	if (world.time % 10 == 0) {
	                    world.fishNum -= world.birdNum; // 每10秒吃一次鱼
	                    if (world.time % 60 == 0) {
	                        world.birdNum += world.birdNum; // 每60秒生小鸟
	                    }
	                }
	                world.token.countDown(); // 记录工作计算已经完成
                }
                
            }
        }
    }
}