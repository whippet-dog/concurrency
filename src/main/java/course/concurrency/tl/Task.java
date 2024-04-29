package course.concurrency.tl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//public class Task implements Runnable {
//    private static final ThreadLocal<Integer> value =
//            ThreadLocal.withInitial(() -> 0);
//
//    @Override
//    public void run() {
//        Integer currentValue = value.get();
//        value.set(currentValue + 1);
//        System.out.print(value.get());
//    }
//
//    public static void main(String[] args) {
//        ExecutorService executor = Executors.newFixedThreadPool(5);
//        for (int i = 0; i < 10; i++) {
//            executor.submit(new Task());
//        }
//    }
//}

//public class Task implements Runnable {
//    private final ThreadLocal<Integer> value =
//            ThreadLocal.withInitial(() -> 0);
//
//    @Override
//    public void run() {
//        Integer currentValue = value.get();
//        value.set(currentValue + 1);
//        System.out.print(value.get());
//    }
//
//    public static void main(String[] args) {
//        ExecutorService executor = Executors.newFixedThreadPool(5);
//        for (int i = 0; i < 10; i++) {
//            executor.submit(new Task());
//        }
//    }
//}

public class Task implements Runnable {
    private ThreadLocal<Integer> value =
            ThreadLocal.withInitial(() -> 0);

    @Override
    public void run() {
        Integer currentValue = value.get();
        value.set(currentValue + 1);
        System.out.print(value.get());
        System.out.println(value);
    }

    public static void main(String[] args) {
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//
//        for (int i = 0; i < 5; i++) {
//            executor.submit(new Task());
//        }
        GridThreadSerialNumber number = new GridThreadSerialNumber();

        for (int i = 0; i < 5; i++) {
            Integer value = number.get();
            System.out.print(value);
        }
    }

    public static class GridThreadSerialNumber {
        private int nextSerialNum = 0;

        private ThreadLocal<Integer> serialNum = new ThreadLocal<>() {
            @Override protected synchronized Integer initialValue() {
                nextSerialNum++;
                return nextSerialNum;
            }
        };

        /**
         * @return Serial number value.
         */
        public int get() {
            return serialNum.get();
        }
    }
}