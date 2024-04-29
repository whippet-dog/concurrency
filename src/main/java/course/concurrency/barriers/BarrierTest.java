package course.concurrency.barriers;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BarrierTest {
    public void cyclicBarrier() throws BrokenBarrierException, InterruptedException {
        final var cb = new CyclicBarrier(3,
                () -> {
                    System.out.println(Thread.currentThread().getId() +  ": barrier broken");
                }
        );

        final ExecutorService e = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 2; i++) {
            e.submit(() -> {
                System.out.println(Thread.currentThread().getId() +  "> job done: " + doSomeWork());
                try {
                    cb.await();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (BrokenBarrierException ex) {
                    System.out.println("Broken barrier!");
                }
            });
        }

        cb.await();
        System.out.println(Thread.currentThread().getId() +  ": all job done");
        e.shutdown();
    }

    private long doSomeWork() {
        long l = 1;
        for (int i = 0; i < 20; i++) {
            l += l * i;
        }
        return l;
    }

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        new BarrierTest().cyclicBarrier();
    }
}
