package course.concurrency.tl;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {
    private final static Semaphore sem = new Semaphore(3);
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            try {
                sem.acquire(3);
                Thread.sleep(5L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        while (sem.availablePermits() > 0) {}

        System.out.println(sem.tryAcquire());
        sem.release();
        System.out.println(sem.tryAcquire());
    }
}
