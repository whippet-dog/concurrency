package course.concurrency.m3_shared.immutable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class OrderService {

    private Map<Long, Order> currentOrders = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong();

    private long nextId() {
        return nextId.incrementAndGet();
    }

    public long createOrder(List<Item> items) {
        long id = nextId();
        Order order = new Order(id, items);
        currentOrders.put(id, order);
        return id;
    }

    public void updatePaymentInfo(long orderId, PaymentInfo paymentInfo) {
        final Order orderWithPaymentInfo = currentOrders.compute(orderId, (id, order) -> {
            if (order == null) {
                return null;
            }

            return order.withPaymentInfo(paymentInfo);
        });
        if (orderWithPaymentInfo != null && orderWithPaymentInfo.checkStatus()) {
            deliver(orderWithPaymentInfo);
        }
    }

    public void setPacked(long orderId) {
        final Order packed = currentOrders.compute(orderId, (id, order) -> {
            if (order == null) {
                return null;
            }

            return order.pack();
        });
        if (packed != null && packed.checkStatus()) {
            deliver(currentOrders.get(orderId));
        }
    }

    private void deliver(Order order) {
        /* ... */
        currentOrders.compute(order.getId(), (id, o) -> {
            if (o == null) {
                return null;
            }

            return o.pack();
        });
    }

    public boolean isDelivered(long orderId) {
        return currentOrders.get(orderId).getStatus().equals(Order.Status.DELIVERED);
    }
}
