package course.concurrency.stamped;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StampedLockTest {
    @Test
    public void counterTest() throws InterruptedException {
        final var counter = new Counter();

        int countOfWriters = 5;
        final var writers = Executors.newFixedThreadPool(countOfWriters);
        final var readers = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        final var lacth = new CountDownLatch(countOfWriters);


        for (int i = 0; i < countOfWriters; i++) {
            writers.submit(() -> {
                for (int j = 0; j < 1_000_000; j++) {
                    counter.increment();
                }

                lacth.countDown();
            });
        }

        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            readers.submit(() -> {
                while (true) {
                    var val = counter.get();
                    if (val % 1_000 == 0) {
                        System.out.println(val);
                    }
                }
            });
        }

        lacth.await();
        assertEquals(5_000_000, counter.get());
    }

    private final static class Counter {
        private long value;
        private final StampedLock lock = new StampedLock();

        public void increment() {
            final var stamp = lock.writeLock();
            try {
                value++;
            } finally {
                lock.unlock(stamp);
            }
        }

        public long get() {
            var stamp = lock.tryOptimisticRead();
            final var l = value;
            if (lock.validate(stamp)) {
                return l;
            } else {
                stamp = lock.readLock();
                try {
                    return value;
                } finally {
                    lock.unlock(stamp);
                }
            }
        }
    }
}
