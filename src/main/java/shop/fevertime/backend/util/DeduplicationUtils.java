package shop.fevertime.backend.util;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DeduplicationUtils {
    public static <T> List<T> deduplication(final List<T> list, Function<? super T, ?> key){
        return list.stream()
                .filter(deduplication(key))
                .collect(Collectors.toList());
    }

    private static <T>Predicate<T> deduplication(Function<? super T, ?>  key){
        final Set<Object> set = ConcurrentHashMap.newKeySet();
        return predicate -> set.add(key.apply(predicate));
    }
}
