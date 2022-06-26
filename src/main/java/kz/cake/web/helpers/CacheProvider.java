package kz.cake.web.helpers;

import kz.cake.web.model.CacheItemDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CacheProvider {
    private static final Map<String, CacheItemDto> cache = new HashMap<>();

    public static <T> void add(String key, T object) {
        cache.put(key, new CacheItemDto(object, LocalDateTime.now().plusHours(12)));
    }

    public static void remove(String key) {
        cache.remove(key);
    }

    public static boolean contains(String key) {
        if (cache.containsKey(key)) {
            if (cache.get(key).isExpires()) {
                cache.remove(key);
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static void clear() {
        cache.clear();
    }

    public static <T> T get(String key) {
        return cache.containsKey(key) ? (T) cache.get(key).getData() : null;
    }

    public static <T> T get(String key, Supplier<T> supplier) {
        if (supplier == null) throw new IllegalArgumentException();

        if (cache.containsKey(key)) return (T) cache.get(key).getData();

        T result = supplier.get();
        cache.put(key, new CacheItemDto(result, LocalDateTime.now().plusHours(12)));
        return result;
    }
}
