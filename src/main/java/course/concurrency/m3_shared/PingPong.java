package course.concurrency.m3_shared;

public class PingPong {

    private static volatile boolean b;
    public static void ping() {
        while (!Thread.interrupted()) {
            boolean localb = b;
            if (!localb) {
                System.out.println("Ping");
                b = true;
            }
        }
    }

    public static void pong() {
        while (!Thread.interrupted()) {
            boolean localb = b;
            if (localb) {
                System.out.println("Pong");
                b = false;
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(PingPong::ping);
        Thread t2 = new Thread(PingPong::pong);
        t1.start();
        t2.start();

        Thread.sleep(3000L);
        System.out.println("============================================");
        t1.interrupt();
        t2.interrupt();
    }
}
