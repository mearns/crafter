package com.brianmearns.crafter;

import com.google.common.base.Function;
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
 * A {@link Builder} of {@linkplain Map map} objects.
 *
 * <p>
 * Map elements are {@linkplain #put(Object, Object) put} into the builder as a key and its value. Putting the same
 * key again will effectively replace previous value associated with that key.
 * </p>
 *
 * @author Brian Mearns <bmearns@ieee.org>
 */
@SuppressWarnings("unused")
public abstract class MapBuilder<K, V> implements Builder<Map<K,V>> {

    /**
     * Create a new builder instance which will build {@link Map Map<K,V>} instances.
     *
     * @param <K> The type for the keys that will be put into the built maps.
     * @param <V> The type for the value that will be put into the built maps.
     *
     * @return The newly constructed {@link MapBuilder}.
     */
    @NotNull
    @Contract("-> !null")
    public static <K,V> MapBuilder<K,V> create() {
        return new DefaultMapBuilder<>();
    }

    /**
     * Create a new builder instance which will build {@link Map Map<K,V>} instances.
     *
     * <p>
     *     This is an alternative convenience interface over {@link #create()} with no args. Instead of specifying
     *     type parameters on the method call, you can just pass in the classes you want to use and the compiler will
     *     determine the correct type parameters automatically.
     * </p>
     *
     * @param keyCls The class of keys that will be put into the build maps.
     * @param valueCls The class of values that will be put into the build maps.
     *
     * @param <K> The type for the keys that will be put into the built maps.
     * @param <V> The type for the value that will be put into the built maps.
     *
     * @return The newly constructed {@link MapBuilder}.
     */
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
    protected abstract void putSupplier(@Nullable K key, @NotNull Supplier<? extends V> valueSupplier);

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
    public MapBuilder<K,V> put(@Nullable K key, @NotNull Builder<? extends V> value) {
        putSupplier(key, value);
        return this;
    }

    /**
     * Place the given key-value pair in the builder, if and only if the
     * given boolean is {@code true}. Otherwise has no effect on the state of the builder.
     *
     * @param key The key at which to put the {@code value}.
     * @param value The value to put at the given {@code key}.
     * @param put Whether or not to actually update the builder.
     *
     * @return This builder instance itself, for chaining convenience.
     */
    public MapBuilder<K,V> maybePut(@Nullable K key, @Nullable V value, boolean put) {
        if(put) {
            putSupplier(key, Suppliers.ofInstance(value));
        }
        return this;
    }

    /**
     * Place the given key-value pair in the builder, if and only if the
     * given boolean is {@code true}. Otherwise has no effect on the state of the builder.
     *
     * @param key The key at which to put the {@code value}.
     * @param value Builder of the value to put at the given {@code key}.
     * @param put Whether or not to actually update the builder.
     *
     * @return This builder instance itself, for chaining convenience.
     */
    public MapBuilder<K,V> maybePut(@Nullable K key, @NotNull Builder<? extends V> value, boolean put) {
        if(put) {
            putSupplier(key, value);
        }
        return this;
    }

    /**
     * Apply the given function to {@code this} object, and return {@code this} object again.
     *
     * <p>
     * This is simply a way to add some arbitrary code in the middle of a chain of method invocations.
     * You could use the {@link Function} to perform some complex logic to configure the builder, for instance.
     *
     * <p>
     * The return value of the function is ignored, but for safety and clarity, it should be a void return.
     *
     * @param function The {@link Function} to be invoked on {@code this} object.
     *
     * @return This {@code MapBuilder} itself, for chaining convenience.
     */
    public abstract MapBuilder<K,V> apply(Function<MapBuilder<K,V>, Void> function);


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
                map.put(entry.getKey(), entry.getValue());
            }
            return map;
        }

        @Override
        protected void putSupplier(@Nullable K key, @NotNull Supplier<? extends V> valueSupplier) {
            entries.add(new Entry<>(key, valueSupplier));
        }

        @Override
        public MapBuilder<K,V> apply(Function<MapBuilder<K,V>, Void> function) {
            function.apply(this);
            return this;
        }

        private static class Entry<K,V> {
            @Nullable
            private final K key;

            @NotNull
            private final Supplier<? extends V> value;

            protected Entry(@Nullable K key, @NotNull Supplier<? extends V> value) {
                this.key = key;
                this.value = value;
            }

            @Nullable
            @Contract(pure=true)
            public K getKey() {
                return key;
            }

            @Contract(value="->!null")
            public V getValue() {
                return value.get();
            }
        }
    }
}
