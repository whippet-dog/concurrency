package course.concurrency.m2_async.cf.min_price;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class PriceAggregator {

    private PriceRetriever priceRetriever = new PriceRetriever();

    public void setPriceRetriever(PriceRetriever priceRetriever) {
        this.priceRetriever = priceRetriever;
    }

    private Collection<Long> shopIds = Set.of(10l, 45l, 66l, 345l, 234l, 333l, 67l, 123l, 768l);

    public void setShops(Collection<Long> shopIds) {
        this.shopIds = shopIds;
    }

    public double getMinPrice(long itemId) {
        final Set<Double> prices = ConcurrentHashMap.newKeySet();
        final ExecutorService executor = Executors.newFixedThreadPool(shopIds.size());
        final List<CompletableFuture<Double>> futures = shopIds.stream()
                .map(shopId -> CompletableFuture
                        .supplyAsync(() -> priceRetriever.getPrice(itemId, shopId), executor)
                        .whenComplete((price, t) -> {
                            if (price > 0) {
                                prices.add(price);
                            }
                        })
                )
                .collect(Collectors.toList());

        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(2800L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ignore) {
        }

        return prices.isEmpty() ? Double.NaN : prices.stream().sorted().collect(Collectors.toList()).get(0);
    }

//    public double getMinPrice(long itemId) throws ExecutionException, InterruptedException {
//        CompletableFuture<Double> chain = null;
//        for (Long shopId : shopIds) {
//            final CompletableFuture<Double> getPriceFuture = CompletableFuture.supplyAsync(() -> {
//                System.out.println(Thread.currentThread().getName());
//                return priceRetriever.getPrice(itemId, shopId);
//            });
//            if (chain != null) {
//                chain = getPriceFuture.thenCombine(chain, Math::min);
//            } else {
//                chain = getPriceFuture;
//            }
//        }
//
//        return chain.get();
//    }
}
