package course.concurrency.m3_shared.collections;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RestaurantService {

    private Map<String, Restaurant> restaurantMap = new ConcurrentHashMap<>() {{
        put("A", new Restaurant("A"));
        put("B", new Restaurant("B"));
        put("C", new Restaurant("C"));
    }};

    private Map<String, AtomicInteger> stat = new ConcurrentHashMap<>();

    public Restaurant getByName(String restaurantName) {
        addToStat(restaurantName);
        return restaurantMap.get(restaurantName);
    }

    public void addToStat(String restaurantName) {
//        stat.merge();
        stat.computeIfAbsent(restaurantName, s -> new AtomicInteger()).incrementAndGet();
    }

    public Set<String> printStat() {
        final HashSet<String> result = new HashSet<>();
        for (Map.Entry<String, AtomicInteger> e : stat.entrySet()) {
            result.add(e.getKey() + " - " + e.getValue());
        }
        return result;
    }
}
