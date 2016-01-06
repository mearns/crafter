package com.brianmearns.crafter;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * A simple {@link Builder} for a single value. This is generally used as a utility inside another builder.
 *
 * @author Brian Mearns <bmearns@ieee.org>
 */
@SuppressWarnings("unused")
public abstract class ValueBuilder<T> implements Builder<T> {

    /**
     * Instantiate a new {@link ValueBuilder} initialized with the given <code>value</code>. I.e., when
     * the {@link #get()} method is invoked, it will return <code>value</code> as the built object.
     *
     * @param value The value to which the returned builder is initialized.
     * @param <T> The type which is built by the returned builder.
     */
    @NotNull
    @Contract("_ -> !null")
    public static <T> ValueBuilder<T> ofInstance(T value) {
        return new DefaultValueBuilder<>(value);
    }

    /**
     * Instantiate a new {@link ValueBuilder} initialized with the given <code>value</code>. I.e., when
     * the {@link #get()} method is invoked, it will delegate to the given object.
     */
    @NotNull
    @Contract("_ -> !null")
    public static <T> ValueBuilder<T> ofBuilder(Builder<T> value) {
        return new DefaultValueBuilder<>(value);
    }

    /**
     * Returns a function which maps Builders of objects to ValueBuilders, using the {@link #ofBuilder(Builder)}
     * method.
     */
    @NotNull
    @Contract(" -> !null")
    public static <T> Function<Builder<T>, ValueBuilder<T>> ofBuilderFunction() {
        return new ValueBuilderOfBuilderFunction<>();
    }

    /**
     * Returns a function which maps values to ValueBuilders of that value, using the {@link #ofInstance(Object)}
     * method.
     */
    @NotNull
    @Contract(" -> !null")
    public static <T> Function<T, ValueBuilder<T>> ofInstanceFunction() {
        return new ValueBuilderOfInstanceFunction<>();
    }

    /**
     * Sets the builder to use the given instance. The {@link #get()} method will return this instance as is.
     *
     * @return This {@code ValueBuilder} itself, for chaining convenience.
     */
    @NotNull
    @Contract("_ -> !null")
    public ValueBuilder<T> set(@Nullable T value) {
        return set(Suppliers.ofInstance(value));
    }

    /**
     * Use the given builder to get the value of the instance. The builder is not invoked to build the value immediately,
     * it will be invoked when <em>this</em> builder's {@link #get()} method is invoked.
     *
     * @return This {@code ValueBuilder} itself, for chaining convenience.
     */
    @NotNull
    public ValueBuilder<T> set(@NotNull Builder<T> valueBuilder) {
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
    @NotNull
    protected abstract ValueBuilder<T> set(@NotNull Supplier<T> value);

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
     * @return This {@code ValueBuilder} itself, for chaining convenience.
     */
    @NotNull
    @Contract("_ -> !null")
    protected ValueBuilder<T> apply(Function<ValueBuilder<T>, Void> function) {
        function.apply(this);
        return this;
    }

    /**
     * Build the value (if needed) and return it. If a value was given (as with {@link #set(Object)}), then it is simply
     * returned. If a builder for a value was given (as with {@link #set(Builder)}), then this method delegates to
     * that's builder's {@link Builder#get()} method.
     *
     * @return The built value.
     */
    @Nullable
    @Override
    public abstract T get();

    /**
     * A function that maps any value to a {@link ValueBuilder} {@linkplain #ofInstance(Object) of that instance}.
     */
    protected static class ValueBuilderOfInstanceFunction<T> implements Function<T, ValueBuilder<T>> {
        @Nullable
        @Override
        public ValueBuilder<T> apply(@Nullable T input) {
            return ofInstance(input);
        }
    }

    /**
     * A function that maps any builder of a value to a {@link ValueBuilder} {@linkplain #ofBuilder(Builder) of that builder}.
     */
    protected static class ValueBuilderOfBuilderFunction<T> implements Function<Builder<T>, ValueBuilder<T>> {
        @Nullable
        @Override
        public ValueBuilder<T> apply(@Nullable Builder<T> input) {
            return ofBuilder(input);
        }
    }

    protected static class DefaultValueBuilder<T> extends ValueBuilder<T> {

        /**
         * A supplier for the value. Suppliers are used to encapsulate the fact that you can
         * set the value using either a value directly, or a builder for the value.
         */
        @NotNull
        private Supplier<T> value = Suppliers.ofInstance(null);

        /**
         * Create a new instance and {@linkplain #set(Object) set} the value to the given {@code value}.
         */
        protected DefaultValueBuilder(@Nullable T value) {
            set(value);
        }

        /**
         * Create a new instance and {@linkplain #set(Builder) set} the value using the given {@code builder}.
         * The builder is not invoked to build the value immediately, it will be invoked when <em>this</em> builder's
         * {@link #get()} method is invoked.
         */
        protected DefaultValueBuilder(Builder<T> builder) {
            set(builder);
        }

        @NotNull
        @Override
        protected ValueBuilder<T> set(@NotNull Supplier<T> value) {
            this.value = value;
            return this;
        }

        @Nullable
        @Override
        public T get() {
            return value.get();
        }
    }
}
