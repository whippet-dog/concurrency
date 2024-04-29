package course.concurrency.m3_shared.immutable;

import java.util.Collections;
import java.util.List;

import static course.concurrency.m3_shared.immutable.Order.Status.NEW;

public final class Order {

    public enum Status { NEW, IN_PROGRESS, DELIVERED }

    private final Long id;
    private final List<Item> items;
    private final PaymentInfo paymentInfo;
    private final boolean isPacked;
    private final Status status;

    public Order withPaymentInfo(PaymentInfo info) {
        return new Order(
                this.id,
                Collections.unmodifiableList(items),
                this.status,
                this.isPacked,
                info
        );
    }

    public Order pack() {
        return new Order(
                this.id,
                Collections.unmodifiableList(this.items),
                this.status,
                true,
                this.paymentInfo
        );
    }

    public Order deliver() {
        return new Order(
                this.id,
                Collections.unmodifiableList(this.items),
                Status.DELIVERED,
                this.isPacked,
                this.paymentInfo
        );
    }

    public Order(Long id, List<Item> items) {
        this.id = id;
        this.items = Collections.unmodifiableList(items);
        this.status = NEW;
        this.isPacked = false;
        this.paymentInfo = null;
    }

    private Order(long id, List<Item> items, Status status, boolean isPacked, PaymentInfo paymentInfo) {
        this.id = id;
        this.items = Collections.unmodifiableList(items);
        this.status = NEW;
        this.isPacked = false;
        this.paymentInfo = null;
    }

    public synchronized boolean checkStatus() {
        if (items != null && !items.isEmpty() && paymentInfo != null && isPacked) {
            return true;
        }
        return false;
    }

    public Long getId() {
        return id;
    }

    public List<Item> getItems() {
        return items;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

//    public void setPaymentInfo(PaymentInfo paymentInfo) {
//        this.paymentInfo = paymentInfo;
//        this.status = Status.IN_PROGRESS;
//    }

    public boolean isPacked() {
        return isPacked;
    }

//    public void setPacked(boolean packed) {
//        isPacked = packed;
//        this.status = Status.IN_PROGRESS;
//    }

    public Status getStatus() {
        return status;
    }

//    public void setStatus(Status status) {
//        this.status = status;
//    }
}
