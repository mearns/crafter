package com.brianmearns.crafter;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Brian Mearns <bmearns@ieee.org>
 */
public abstract class MapBuilder<K, V> implements Builder<Map<K,V>> {

    @NotNull
    @Contract("-> !null")
    public static <K,V> MapBuilder<K,V> create() {
        return new DefaultMapBuilder<>();
    }

    @NotNull
    @Contract("_, _ -> !null")
    @SuppressWarnings("unused")
    public static <K,V> MapBuilder<K,V> create(@NotNull Class<K> keyCls, @NotNull Class<V> valueCls) {
        return new DefaultMapBuilder<>();
    }

    /**
     * This is the implementation specific method for putting a value in the map.
     *
     * @param key The key to add (or replace) in the map.
     * @param valueSupplier A {@link Supplier} for the value to put in the map.
     */
    @NotNull
    protected abstract void putSupplier(@Nullable K key, @NotNull Supplier<V> valueSupplier);

    /**
     * Put the given value in the builder's map.
     *
     * @param key The key at which to put the {@code value}.
     * @param value The value to put at the given {@code key}.
     *
     * @return This builder instance itself, for chaining convenience.
     */
    public MapBuilder<K,V> put(@Nullable K key, @Nullable V value) {
        putSupplier(key, Suppliers.ofInstance(value));
        return this;
    }

    /**
     * Put a value in the builder's map, represented by a Builder of that value.
     *
     * <p>
     * Note that the given Builder is not {@linkplain Builder#get() invoked} to build the value
     * right away, it is invoked once for each call to {@link #get()}.
     *
     * @param key The key at which to put the {@code value}.
     * @param value The value to put at the given {@code key}.
     *
     * @return This builder instance itself, for chaining convenience.
     */
    public MapBuilder<K,V> put(@Nullable K key, @NotNull Builder<V> value) {
        putSupplier(key, value);
        return this;
    }

    public MapBuilder<K,V> maybePut(@Nullable K key, @Nullable V value, boolean put) {
        if(put) {
            putSupplier(key, Suppliers.ofInstance(value));
        }
        return this;
    }




    protected static class DefaultMapBuilder<K, V> extends MapBuilder<K, V> {
        @NotNull
        List<Entry<K,V>> entries;

        public DefaultMapBuilder() {
            entries = new LinkedList<>();
        }

        @Override
        @NotNull
        @Contract("-> !null")
        public Map<K, V> get() {
            Map<K,V> map = new LinkedHashMap<>();
            for(Entry<K,V> entry : entries) {
                map.put(entry.getKey(), entry.getValue().get());
            }
            return map;
        }

        @Override
        protected void putSupplier(@Nullable K key, @NotNull Supplier<V> valueSupplier) {
            entries.add(new Entry<>(key, valueSupplier));
        }

        private static class Entry<K,V> {
            @Nullable
            private final K key;

            @NotNull
            private final Supplier<V> value;

            protected Entry(@Nullable K key, @NotNull Supplier<V> value) {
                this.key = key;
                this.value = value;
            }

            @Nullable
            @Contract(pure=true)
            public K getKey() {
                return key;
            }

            @NotNull
            @Contract(value="->!null", pure=true)
            public Supplier<V> getValue() {
                return value;
            }
        }
    }
}
