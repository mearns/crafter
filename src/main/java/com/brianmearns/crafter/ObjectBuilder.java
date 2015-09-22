package com.brianmearns.crafter;

import com.google.common.base.Supplier;
import com.google.common.base.Function;

import java.util.concurrent.Callable;

/**
 * An {@code ObjectBuilder} is a {@link Supplier} which implements the builder
 * design pattern. This is primarily meant as an intention tagging interface to
 * indicate that a particular {@code Supplier} implementation is meant as a
 * builder.
 */
public interface ObjectBuilder<T> extends Supplier<T> {

    /**
     * Build and return an instance of type T based on the current
     * state of this {@code ObjectBuilder}.
     *
     * @return The new built instance.
     */
    @Override
    public T get();

    /**
     * {@linkpain Function#apply(F) Applies} the given callback function to
     * this builder.
     *
     * @returns This instance itself, for chaining convenience.
     */
    @NotNull
    public ObjectBuilder<T> apply(@NotNull Function<ObjectBuilder<T>, ?> callback);

    /**
     * Returns a {@link ConditionalBuilder} which either does or does not change
     * the state of the parent builder based on the given boolean.
     *
     * @param apply If {@code true}, then the methods of the returned {@code ConditionalBuilder}
     *  will actually be applied to this builder. Otherwise, they will not affect this
     *  builder.
     *
     *  @see #maybe(Callable)
     *  @see #maybe(Function)
     */
    @NotNull
    public ConditionalBuilder<T> maybe(boolean apply);

    /**
     * Returns a {@link ConditionalBuilder} which either does or does not change
     * the state of the parent builder based on the value returned by the given callable.
     *
     * @param applyCallback This callback is {@linkplain Callable#call() called} for every
     * state changing method invoked in the returned builder. If it returns {@code true},
     * then the method will actually apply modifications to this builder. Otherwise, it
     * will not affect this builder.
     */
    @NotNull
    public ConditionalBuilder<T> maybe(@NotNull Callable<Boolean> applyCallback);

    /**
     * Returns a {@link ConditionalBuilder} which either does or does not change
     * the state of the parent builder based on the value returned by the given callable.
     *
     * @param applyCallback This callback is {@linkplain Function#apply(F) applied} to this
     * builder for every state changing method invoked in the returned builder. If it returns {@code true},
     * then the method will actually apply modifications to this builder. Otherwise, it
     * will not affect this builder.
     */
    @NotNull
    public ConditionalBuilder<T> maybe(@NotNull Function<ObjectBuilder<T>, Boolean> applyCallback);

}

