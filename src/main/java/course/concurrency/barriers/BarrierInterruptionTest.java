package course.concurrency.barriers;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BarrierInterruptionTest {
    public void cyclicBarrier() throws BrokenBarrierException, InterruptedException {
        final var cb = new CyclicBarrier(3,
                () -> {
                    System.out.println(Thread.currentThread().getId() +  ": barrier broken");
                }
        );

        final Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getId() +  "> job done: " + doSomeWork());
            try {
                cb.await();
            } catch (InterruptedException ex) {
                System.out.println(Thread.currentThread().getId() + " Interrupted!");
            } catch (BrokenBarrierException ex) {
                System.out.println(Thread.currentThread().getId() + " Broken barrier!");
            }
        });
        final Thread t2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getId() +  "> job done: " + doSomeWork());
            try {
                cb.await();
            } catch (InterruptedException ex) {
                System.out.println(Thread.currentThread().getId() + " Interrupted!");
            } catch (BrokenBarrierException ex) {
                System.out.println(Thread.currentThread().getId() + " Broken barrier!");
            }
        });

        t1.start();
        t2.start();

        t1.interrupt();

        cb.await();
        System.out.println(Thread.currentThread().getId() +  ": all job done");
    }

    private long doSomeWork() {
        long l = 1;
        for (int i = 0; i < 20; i++) {
            l += l * i;
        }
        return l;
    }

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        new BarrierInterruptionTest().cyclicBarrier();
    }
}
