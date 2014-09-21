package com.karelherink.flickrsearch.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruSizeLimitedOrderedMap<K, V> extends LinkedHashMap<K, V> {

    private int maxEntries;

    public LruSizeLimitedOrderedMap(int maxEntries, boolean accessOrder) {
        super(maxEntries, 0.75f, accessOrder);
        this.maxEntries = maxEntries;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> kvEntry) {
        return size() > maxEntries;
    }
}
