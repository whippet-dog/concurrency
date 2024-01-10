package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicReference;

public class StoppableOptimisticVolatileBoolean implements AuctionStoppable {
    private final Notifier notifier;
    private final AtomicReference<Bid> latestBid = new AtomicReference<>(
            new Bid(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE)
    );
    private volatile boolean isRan = true;

    public StoppableOptimisticVolatileBoolean(Notifier notifier)  {
        this.notifier = notifier;
    }

    public boolean propose(Bid bid) {
        Bid temp;
        do {
            temp = latestBid.get();
            if (temp.getPrice() >= bid.getPrice()) {
                return false;
            }
        } while(isRan && !latestBid.compareAndSet(temp, bid));

        if (!isRan) {
            return false;
        }

        notifier.sendOutdatedMessage(temp);
        return true;
    }

    public Bid getLatestBid() {
        return latestBid.get();
    }

    public Bid stopAuction() {
        final Bid bid = latestBid.get();
        isRan = false;
        return bid;
    }
}
