package com.brianmearns.crafter;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * A simple {@link BuilderInterface} for a single value. This is generally used as a utility inside another builder.
 *
 * @author Brian Mearns <bmearns@ieee.org>
 */
@SuppressWarnings("unused")
public abstract class ValueBuilder<T> implements BuilderInterface<T> {

    /**
     * Instantiate a new {@link ValueBuilder} initialized with the given <code>value</code>. I.e., when
     * the {@link #get()} method is invoked, it will return <code>value</code> as the built object.
     *
     * @param value The value to which the returned builder is initialized.
     * @param <T> The type which is built by the returned builder.
     */
    @Nonnull
    public static <T> ValueBuilder<T> create(@Nullable T value) {
        return new DefaultValueBuilder<>(value);
    }

    /**
     * Instantiate a new {@link ValueBuilder} initialized with the given <code>value</code>. I.e., when
     * the {@link #get()} method is invoked, it will delegate to the given object.
     */
    @Nonnull
    public static <T> ValueBuilder<T> create(BuilderInterface<T> value) {
        return new DefaultValueBuilder<>(value);
    }

    /**
     * Create a new instance that does not yet have the value set.
     */
    @Nonnull
    public static <T> ValueBuilder<T> create() {
        return new DefaultValueBuilder<>();
    }

    /**
     * Create a new instance that does not yet have the value set.
     */
    @Nonnull
    public static <T> ValueBuilder<T> create(Class<T> cls) {
        return new DefaultValueBuilder<>();
    }

    /**
     * Returns a function which maps Builders of objects to ValueBuilders, using the {@link #create(BuilderInterface)}
     * method.
     */
    @Nonnull
    public static <T> Function<BuilderInterface<T>, ValueBuilder<T>> ofBuilderFunction() {
        return new ValueBuilderOfBuilderFunction<>();
    }

    /**
     * Returns a function which maps values to ValueBuilders of that value, using the {@link #create(Object)}
     * method.
     */
    @Nonnull
    public static <T> Function<T, ValueBuilder<T>> ofInstanceFunction() {
        return new ValueBuilderOfInstanceFunction<>();
    }

    /**
     * Sets the builder to use the given instance. The {@link #get()} method will return this instance as is.
     *
     * @return This {@code ValueBuilder} itself, for chaining convenience.
     */
    @Nonnull
    public ValueBuilder<T> set(@Nullable T value) {
        return set(Suppliers.ofInstance(value));
    }

    /**
     * Use the given builder to get the value of the instance. The builder is not invoked to build the value immediately,
     * it will be invoked when <em>this</em> builder's {@link #get()} method is invoked.
     *
     * @return This {@code ValueBuilder} itself, for chaining convenience.
     */
    @Nonnull
    public ValueBuilder<T> set(@Nonnull BuilderInterface<T> valueBuilder) {
        return set((Supplier<T>)valueBuilder);
    }

    /**
     * Helper method to set the value that will be used by {@link #get()} to return a new instance.
     *
     * This is a helper method which {@link #set(BuilderInterface)} and {@link #set(Object)} delegate to. The public interface
     * does not allow generic {@link Supplier} objects to be used to set the value.
     *
     * @return This {@code ValueBuilder} itself, for chaining convenience.
     */
    @Nonnull
    protected abstract ValueBuilder<T> set(@Nonnull Supplier<T> value);

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
    @Nonnull
    protected abstract ValueBuilder<T> apply(@Nonnull Function<ValueBuilder<T>, Void> function);

    @Nonnull
    protected abstract ValueBuilder<T> maybeSet(@Nonnull Supplier<T> value, boolean doSet);

    @Nonnull
    public ValueBuilder<T> maybeSet(@Nullable T value, boolean doSet) {
        this.maybeSet(Suppliers.ofInstance(value), doSet);
        return this;
    }

    /**
     * Build the value (if needed) and return it. If a value was given (as with {@link #set(Object)}), then it is simply
     * returned. If a builder for a value was given (as with {@link #set(BuilderInterface)}), then this method delegates to
     * that's builder's {@link BuilderInterface#get()} method.
     *
     * @return The built value.
     * @throws IncompleteBuilderException if a value has not yet been set for the builder.
     */
    @Nullable
    @Override
    public abstract T get() throws IncompleteBuilderException ;

    /**
     * Returns the top-level non-conditional builder.
     */
    @Nonnull
    public abstract ValueBuilder<T> always();

    /**
     * Returns a builder which either does or doesn't delegate to this builder based on the given boolean.
     * @param yes If {@code true}, then methods invoked on the returned builder will modify the state of {@code this}
     *            builder. Otherwise, methods invoked on the returned builder will not modify state.
     */
    @Nonnull
    public abstract ValueBuilder<T> maybe(boolean yes);

    /**
     * Returns the parent builder of a conditional builder. This is not necessarily the originating top level
     * builder if you have nested (or rather chained) calls to {@link #maybe(boolean)}.
     */
    @Nonnull
    public abstract ValueBuilder<T> endMaybe();


    protected static class DefaultValueBuilder<T> extends ValueBuilder<T> {

        /**
         * A supplier for the value. Suppliers are used to encapsulate the fact that you can
         * set the value using either a value directly, or a builder for the value.
         */
        @Nonnull
        private Optional<Supplier<T>> value = Optional.absent();

        /**
         * Create a new instance and {@linkplain #set(Object) set} the value to the given {@code value}.
         */
        protected DefaultValueBuilder(@Nullable T value) {
            set(value);
        }

        protected DefaultValueBuilder() {

        }

        /**
         * Create a new instance and {@linkplain #set(BuilderInterface) set} the value using the given {@code builder}.
         * The builder is not invoked to build the value immediately, it will be invoked when <em>this</em> builder's
         * {@link #get()} method is invoked.
         */
        protected DefaultValueBuilder(BuilderInterface<T> builder) {
            set(builder);
        }

        @Nonnull
        @Override
        protected ValueBuilder<T> set(@Nonnull Supplier<T> value) {
            this.value = Optional.of(value);
            return this;
        }

        @Nonnull
        @Override
        protected ValueBuilder<T> maybeSet(@Nonnull Supplier<T> value, boolean doSet) {
            if(doSet) {
                set(value);
            }
            return this;
        }

        @Nullable
        @Override
        public T get() throws IncompleteBuilderException {
            if(value.isPresent()) {
                return value.get().get();
            }
            throw new IncompleteBuilderException("Builder value has not yet been set.");
        }

        @Override
        @Nonnull
        protected ValueBuilder<T> apply(@Nonnull Function<ValueBuilder<T>, Void> function) {
            function.apply(this);
            return this;
        }


        /**
         * Returns itself.
         */
        @Override
        @Nonnull
        public ValueBuilder<T> always() {
            return this;
        }

        /**
         * Returns either {@code this} object itself, or a new {@link NeverValueBuilder} if {@code yes} is {@code false}.
         */
        @Override
        @Nonnull
        public ValueBuilder<T> maybe(boolean yes) {
            if(yes) {
                return this;
            } else {
                return new NeverValueBuilder<>(this, this);
            }
        }

        @Nonnull
        @Override
        public ValueBuilder<T> endMaybe() {
            return this;
        }
    }

    /**
     * A {@link ValueBuilder} which doesn't actually do anything.
     */
    protected static class NeverValueBuilder<T> extends ValueBuilder<T> {

        @Nonnull
        private final ValueBuilder<T> alwaysBuilder;

        @Nonnull
        private final ValueBuilder<T> parent;

        protected NeverValueBuilder(@Nonnull ValueBuilder<T> alwaysBuilder, @Nonnull ValueBuilder<T> parent) {
            this.alwaysBuilder = alwaysBuilder;
            this.parent = parent;
        }

        @Nonnull
        @Override
        protected ValueBuilder<T> set(@Nonnull Supplier<T> value) {
            return this;
        }

        @Nonnull
        @Override
        protected ValueBuilder<T> apply(@Nonnull Function<ValueBuilder<T>, Void> function) {
            return this;
        }

        @Nonnull
        @Override
        protected ValueBuilder<T> maybeSet(@Nonnull Supplier<T> value, boolean doSet) {
            return this;
        }

        @Nullable
        @Override
        public T get() throws IncompleteBuilderException  {
            return alwaysBuilder.get();
        }

        @Nonnull
        @Override
        public ValueBuilder<T> always() {
            return alwaysBuilder;
        }

        @Nonnull
        @Override
        public ValueBuilder<T> maybe(boolean yes) {
            return new NeverValueBuilder<>(alwaysBuilder, this);
        }

        @Nonnull
        @Override
        public ValueBuilder<T> endMaybe() {
            return parent;
        }
    }

    /**
     * A function that maps any value to a {@link ValueBuilder} {@linkplain #create(Object) of that instance}.
     */
    protected static class ValueBuilderOfInstanceFunction<T> implements Function<T, ValueBuilder<T>> {
        @Nullable
        @Override
        public ValueBuilder<T> apply(@Nullable T input) {
            return create(input);
        }
    }

    /**
     * A function that maps any builder of a value to a {@link ValueBuilder} {@linkplain #create(BuilderInterface) of that builder}.
     */
    protected static class ValueBuilderOfBuilderFunction<T> implements Function<BuilderInterface<T>, ValueBuilder<T>> {
        @Nullable
        @Override
        public ValueBuilder<T> apply(BuilderInterface<T> input) {
            if(input == null) {
                throw new NullPointerException("Builder cannot be null. To create an instance with a null value, supply a null value instead of a null builder.");
            }
            return create(input);
        }
    }

}
