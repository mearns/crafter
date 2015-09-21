package com.brianmearns.crafter;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

/**
 * A simple {@link Builder} for a single value. This is generally used as a utility inside another builder.
 *
 * @author Brian Mearns <bmearns@ieee.org>
 */
public class ValueBuilder<T> implements Builder<T> {

    /**
     * A supplier for the value. Suppliers are used to encapsulate the fact that you can
     * set the value using either a value directly, or a builder for the value.
     */
    private Supplier<T> value;

    /**
     * Create a new instance and {@linkplain #set(Object)} the value to the given {@code value}.
     */
    public ValueBuilder(T value) {
        set(value);
    }

    /**
     * Create a new instance and {@linkplain #set(Builder)} the value using the given {@code builder}.
     */
    public ValueBuilder(Builder<T> builder) {
        set(builder);
    }

    /**
     * Sets the builder to use the given instance. The {@link #get()} method will return this instance as is.
     *
     * @return This {@code ValueBuilder} itself, for chaining convenience.
     */
    public ValueBuilder<T> set(T value) {
        return set(Suppliers.ofInstance(value));
    }

    /**
     * Use the given builder to get the value of the instance. The {@link #get()} method will invoke the {@link Builder#get()}
     * method on this builder in order to build the value.
     *
     * @return This {@code ValueBuilder} itself, for chaining convenience.
     */
    public ValueBuilder<T> set(Builder<T> valueBuilder) {
        return set((Supplier<T>)valueBuilder);
    }

    /**
     * Helper method to set the value that will be used by {@link #get()} to return a new instance.
     *
     * This is a helper method which {@link #set(Builder)} and {@link #set(Object)} delegate to. The public interface
     * does not allow generic {@link Supplier} objects to be used to set the value.
     *
     * @return This {@code ValueBuilder} itself, for chaining convenience.
     */
    protected ValueBuilder<T> set(Supplier<T> value) {
        this.value = value;
        return this;
    }

    @Override
    public T get() {
        return value == null ? null : value.get();
    }
}
