package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicReference;

public class AuctionOptimistic implements Auction {
    private final Notifier notifier;
    private final AtomicReference<Bid> latestBid = new AtomicReference<>(
            new Bid(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE)
    );

    public AuctionOptimistic(Notifier notifier)  {
        this.notifier = notifier;
    }

    public boolean propose(Bid bid) {
        Bid temp;
        do {
            temp = latestBid.get();
            if (temp.getPrice() >= bid.getPrice()) {
                return false;
            }
        } while(!latestBid.compareAndSet(temp, bid));

        notifier.sendOutdatedMessage(temp);
        return true;
    }

    public Bid getLatestBid() {
        return latestBid.get();
    }
}
