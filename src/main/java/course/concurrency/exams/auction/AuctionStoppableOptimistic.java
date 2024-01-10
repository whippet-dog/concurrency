package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;

public class AuctionStoppableOptimistic implements AuctionStoppable {
    private final Notifier notifier;
    private final AtomicMarkableReference<Bid> latestBid = new AtomicMarkableReference<>(
            new Bid(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE),
            false
    );

    public AuctionStoppableOptimistic(Notifier notifier)  {
        this.notifier = notifier;
    }

    public boolean propose(Bid bid) {
        Bid temp;
        do {
            temp = latestBid.getReference();
            if (temp.getPrice() >= bid.getPrice()) {
                return false;
            }
        } while(!latestBid.compareAndSet(temp, bid, false, false));

        if (latestBid.isMarked()) {
            return false;
        }

        notifier.sendOutdatedMessage(temp);
        return true;
    }

    public Bid getLatestBid() {
        return latestBid.getReference();
    }

    public Bid stopAuction() {
        if (latestBid.isMarked()) {
            return latestBid.getReference();
        }
        final Bid bid = latestBid.getReference();
        latestBid.set(bid, true);
        return bid;
    }
}
