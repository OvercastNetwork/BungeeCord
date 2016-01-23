package net.md_5.bungee.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class ConcurrentAggregateMap<K, V> implements Map<K, V> {

    private final Set<Map<K, V>> maps = new HashSet<>();

    synchronized public boolean addMap(Map<K, V> map) {
        return maps.add(map);
    }

    synchronized public boolean removeMap(Map<K, V> map) {
        return maps.remove(map);
    }

    @Override
    synchronized public int size() {
        int n = 0;
        for(Map<K, V> map : maps) {
            synchronized(map) {
                n += map.size();
            }
        }
        return n;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    synchronized public boolean containsKey(Object key) {
        for(Map<K, V> map : maps) {
            synchronized(map) {
                if(map.containsKey(key)) return true;
            }
        }
        return false;
    }

    @Override
    synchronized public boolean containsValue(Object value) {
        for(Map<K, V> map : maps) {
            synchronized(map) {
                if(map.containsValue(value)) return true;
            }
        }
        return false;
    }

    @Override
    synchronized public V get(Object key) {
        for(Map<K, V> map : maps) {
            synchronized(map) {
                if(map.containsKey(key)) return map.get(key);
            }
        }
        return null;
    }

    @Override
    synchronized public Set<K> keySet() {
        final ImmutableSet.Builder<K> builder = ImmutableSet.builder();
        for(Map<K, V> map : maps) {
            synchronized(map) {
                builder.addAll(map.keySet());
            }
        }
        return builder.build();
    }

    @Override
    synchronized public Collection<V> values() {
        final ImmutableList.Builder<V> builder = ImmutableList.builder();
        for(Map<K, V> map : maps) {
            synchronized(map) {
                builder.addAll(map.values());
            }
        }
        return builder.build();
    }

    @Override
    synchronized public Set<Entry<K, V>> entrySet() {
        final ImmutableSet.Builder<Map.Entry<K, V>> builder = ImmutableSet.builder();
        for(Map<K, V> map : maps) {
            synchronized(map) {
                builder.addAll(map.entrySet());
            }
        }
        return builder.build();
    }

    @Override
    public V put(K key, V value) {
        throw new UnsupportedOperationException("This container cannot be directly modified");
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException("This container cannot be directly modified");
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("This container cannot be directly modified");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("This container cannot be directly modified");
    }
}
