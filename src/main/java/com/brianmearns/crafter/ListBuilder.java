package com.brianmearns.crafter;

import com.brianmearns.crafter.util.SupplierFunctions;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * A {@link Builder} for a list of values.
 *
 * @author Brian Mearns <bmearns@ieee.org>
 */
@SuppressWarnings("unused")
public abstract class ListBuilder<T> implements Builder<List<T>> {

    /**
     * Helper method for adding an element as a supplier of that element.
     * @param element Supplier of the element to add.
     *
     * @return {@code this} object itself, for chaining convenience.
     */
    @NotNull
    @Contract("_ -> !null")
    protected abstract ListBuilder<T> add(@NotNull Supplier<T> element);

    /**
     * Helper method for adding an iterable of suppliers of elements.
     *
     * @param elements Suppliers for all the elements to add.
     *
     * @return {@code this} object itself, for chaining convenience.
     */
    @NotNull
    @Contract("_ -> !null")
    protected abstract ListBuilder<T> addSuppliers(@NotNull Iterable<Supplier<T>> elements);

    /**
     * Helper method for adding an iterator of suppliers of elements.
     *
     * @param elements Suppliers for all the elements to add.
     *
     * @return {@code this} object itself, for chaining convenience.
     */
    @NotNull
    @Contract("_ -> !null")
    protected abstract ListBuilder<T> addSuppliers(@NotNull Iterator<Supplier<T>> elements);

    /**
     * Helper method for adding an array of suppliers of elements.
     *
     * @param elements Suppliers for all the elements to add.
     *
     * @return {@code this} object itself, for chaining convenience.
     */
    @NotNull
    protected abstract ListBuilder<T> addSuppliers(@NotNull Supplier<T>[] elements);

    /**
     * Helper method to add the given element, if and only if the given boolean is {@code true}. Otherwise
     * has no effect on the state of the builder.
     *
     * @param element A supplier for the element to {@link #add(Supplier)}.
     * @param add Whether or not to add the element.
     *
     * @return {@code this} object itself, for chaining convenience.
     */
    @NotNull
    @Contract("_, _ -> !null")
    protected abstract ListBuilder<T> maybeAdd(@NotNull Supplier<T> element, boolean add);

    /**
     * Add all of the given elements, in order, to the list of values.
     *
     * @param elements An {@link Iterable} of the elements to add.
     *
     * @return {@code this} object itself, for chaining convenience.
     *
     * @see #addAll(Iterator)
     * @see #addAll(Object[])
     * @see #addBuilders(Iterable)
     */
    @NotNull
    @Contract("_ -> !null")
    public ListBuilder<T> addAll(@NotNull Iterable<T> elements) {
        return addSuppliers(Iterables.transform(elements, SupplierFunctions.<T>supplierOfInstanceFunction()));
    }

    /**
     * Add all of the given {@link Builder Builders}, in order, as builders of element values to the end of the list.
     *
     * <p>
     * As with {@link #add(Builder)}, this does not invoke any of the builders immediately to build the element value.
     * Each builder will be invoked once during a call to {@link #get()}.
     *
     * @param elements An {@link Iterable} of the builders to add.
     *
     * @return {@code this} object itself, for chaining convenience.
     *
     * @see #addBuilders(Iterator)
     * @see #addBuilders(Builder[])
     * @see #addAll(Iterable)
     */
    @NotNull
    @Contract("_ -> !null")
    public ListBuilder<T> addBuilders(@NotNull Iterable<Builder<T>> elements) {
        return addSuppliers(Iterables.transform(elements, SupplierFunctions.<T>builderToSupplierFunction()));
    }

    /**
     * Add all of the given elements, in order, to the list of values.
     *
     * @param elements An {@link Iterator} over the elements to add.
     *
     * @return {@code this} object itself, for chaining convenience.
     *
     * @see #addAll(Iterable)
     * @see #addAll(Object[])
     * @see #addBuilders(Iterator)
     */
    @NotNull
    @Contract("_ -> !null")
    public ListBuilder<T> addAll(@NotNull Iterator<T> elements) {
        return addSuppliers(Iterators.transform(elements, SupplierFunctions.<T>supplierOfInstanceFunction()));
    }

    /**
     * Add all of the given {@link Builder Builders}, in order, as builders of element values to the end of the list.
     *
     * <p>
     * As with {@link #add(Builder)}, this does not invoke any of the builders immediately to build the element value.
     * Each builder will be invoked once during a call to {@link #get()}.
     *
     * @param elements An {@link Iterator} over the builders to add.
     *
     * @return {@code this} object itself, for chaining convenience.
     *
     * @see #addBuilders(Iterable)
     * @see #addBuilders(Builder[])
     * @see #addAll(Iterator)
     */
    @NotNull
    @Contract("_ -> !null")
    public ListBuilder<T> addBuilders(@NotNull Iterator<Builder<T>> elements) {
        return addSuppliers(Iterators.transform(elements, SupplierFunctions.<T>builderToSupplierFunction()));
    }

    /**
     * Add all of the given elements, in order, to the list of values.
     *
     * @param elements An array of the elements to add.
     *
     * @return {@code this} object itself, for chaining convenience.
     *
     * @see #addAll(Iterable)
     * @see #addAll(Iterator)
     * @see #addBuilders(Builder[])
     */
    @NotNull
    @Contract("_ -> !null")
    public ListBuilder<T> addAll(@NotNull T[] elements) {
        return addSuppliers(Iterables.transform(Arrays.asList(elements), SupplierFunctions.<T>supplierOfInstanceFunction()));
    }

    /**
     * Add all of the given {@link Builder Builders}, in order, as builders of element values to the end of the list.
     *
     * <p>
     * As with {@link #add(Builder)}, this does not invoke any of the builders immediately to build the element value.
     * Each builder will be invoked once during a call to {@link #get()}.
     *
     * @param elements An array of the builders to add.
     *
     * @return {@code this} object itself, for chaining convenience.
     *
     * @see #addBuilders(Iterable)
     * @see #addBuilders(Iterator)
     * @see #addAll(Object[])
     */
    @NotNull
    @Contract("_ -> !null")
    public ListBuilder<T> addBuilders(@NotNull Builder<T>[] elements) {
        return addSuppliers(Iterables.transform(Arrays.asList(elements), SupplierFunctions.<T>builderToSupplierFunction()));
    }

    /**
     * Add the given item as the next element in the list.
     *
     * @param element The element to append to the list.
     *
     * @return {@code this} object itself, for chaining convenience.
     */
    @NotNull
    @Contract("_ -> !null")
    public ListBuilder<T> add(@Nullable T element) {
        return add(Suppliers.ofInstance(element));
    }

    /**
     * Add the given {@link Builder} as a builder for the next item in the list. The builder is not invoked to build
     * the value immediately, its {@link Builder#get()} method is invoked to build the element value only when <em>this</em>
     * object's {@link #get()} method is invoked to build a list of values.
     *
     * @param elementBuilder Builder for the element to put at the end of the current list of elements.
     *
     * @return {@code this} object itself, for chaining convenience.
     */
    @NotNull
    @Contract("_ -> !null")
    public ListBuilder<T> add(@NotNull Builder<T> elementBuilder) {
        return add((Supplier<T>) elementBuilder);
    }

    /**
     * Adds the given element, if and only if the given boolean is {@code true}. Otherwise
     * has no effect on the state of the builder.
     *
     * @param element The element value to {@link #add(Object)}.
     * @param add Whether or not to add the element.
     *
     * @return {@code this} object itself, for chaining convenience.
     */
    @NotNull
    @Contract("_, _ -> !null")
    public ListBuilder<T> maybeAdd(@Nullable T element, boolean add) {
        return maybeAdd(Suppliers.ofInstance(element), add);
    }

    /**
     * Build a new list using the elements specified for this builder.
     *
     * @return The built list of elements.
     */
    @NotNull
    @Contract("-> !null")
    @Override
    public abstract List<T> get();

    /**
     * Adds the given element builder for the next item in the list, if and only if the
     * given boolean is {@code true}. Otherwise has no effect on the state of the builder.
     *
     * @param element The element value to {@link #add(Builder)}.
     * @param add Whether or not to add the element.
     *
     * @return {@code this} object itself, for chaining convenience.
     */
    @NotNull
    @Contract("_, _ -> !null")
    public ListBuilder<T> maybeAdd(@NotNull Builder<T> element, boolean add) {
        return maybeAdd((Supplier<T>) element, add);
    }

    public abstract ListBuilder<T> apply(@NotNull Function<? super ListBuilder<T>, Void> function);

    /**
     * Returns the top-level non-conditional builder.
     */
    @NotNull
    @Contract("-> !null")
    public abstract ListBuilder<T> always();

    /**
     * Returns a builder which either does or doesn't delegate to this builder based on the given boolean.
     * @param yes If {@code true}, then methods invoked on the returned builder will modify the state of {@code this}
     *            builder. Otherwise, methods invoked on the returned builder will not modify state.
     */
    @NotNull
    public abstract ListBuilder<T> maybe(boolean yes);

    /**
     * Returns the alwaysBuilder list builder of a conditional list builder. This is not necessarily the originating top level
     * builder if you have nested (or rather chained) calls to {@link #maybe(boolean)}.
     */
    @NotNull
    public abstract ListBuilder<T> endMaybe();

    @SuppressWarnings("unused")
    protected static class DefaultListBuilder<T> extends ListBuilder<T> {

        @NotNull
        private final List<Supplier<T>> elements;

        @NotNull
        private final ListBuilder<T> parent;

        {
            //Linked list is good because we're mostly just appending to it, and then iterating through it.
            // Linked lists are pretty good at both.
            elements = new LinkedList<>();
        }

        public DefaultListBuilder() {
            this.parent = this;
        }

        public DefaultListBuilder(ListBuilder<T> parent) {
            this.parent = parent;
        }

        /**
         * Helper method to actually build the list returned by {@link #get()}.
         *
         * <p>
         * This can be overridden if you need to create a different type of {@link List}. The
         * default implementation produces an {@link ArrayList}.
         *
         * <p>
         * Be careful how you implement this. The given {@link Supplier suppliers} should
         * be {@linkplain Supplier#get() invoked} exactly once per call to this method, and
         * they should be invoked from within this method. For instance, things like
         * {@link Lists#transform(List, Function)} do "lazy" evaluation which invokes the
         * supplier everytime the element is accessed in the returned list. This is <em>not</em>
         * suitable for use with this method.
         *
         * @param suppliers An immutable list of the suppliers for the element values.
         *
         * @return A list that will be returned by this object's {@link #get()} method.
         */
        @NotNull
        @Contract("_ -> !null")
        protected List<T> get(@NotNull ImmutableList<Supplier<T>> suppliers) {
            ArrayList<T> list = new ArrayList<>(suppliers.size());
            for (Supplier<T> supplier : suppliers) {
                list.add(supplier.get());
            }
            return list;
        }

        @Override
        @NotNull
        @Contract("_ -> !null")
        protected ListBuilder<T> add(@NotNull Supplier<T> element) {
            elements.add(element);
            return this;
        }

        @Override
        @NotNull
        @Contract("_ -> !null")
        protected ListBuilder<T> addSuppliers(@NotNull Iterable<Supplier<T>> elements) {
            Iterables.addAll(this.elements, elements);
            return this;
        }

        @Override
        @NotNull
        @Contract("_ -> !null")
        protected ListBuilder<T> addSuppliers(@NotNull Iterator<Supplier<T>> elements) {
            while(elements.hasNext()) {
                this.elements.add(elements.next());
            }
            return this;
        }

        @Override
        @NotNull
        protected ListBuilder<T> addSuppliers(@NotNull Supplier<T>[] elements) {
            return addSuppliers(Arrays.asList(elements));
        }

        @Override
        @NotNull
        @Contract("_, _ -> !null")
        protected ListBuilder<T> maybeAdd(@NotNull Supplier<T> element, boolean add) {
            if(add) {
                this.add(element);
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
         * @return This {@code ValueBuilder} itself, for chaining convenience.
         */
        @Override
        @NotNull
        @Contract("_ -> !null")
        public ListBuilder<T> apply(@NotNull Function<? super ListBuilder<T>, Void> function) {
            function.apply(this);
            return this;
        }

        /**
         * Build a new list using the elements specified for this builder.
         *
         * <p>
         * Note that this delegates to {@link #get(ImmutableList)}.
         * </p>
         *
         * @return The built list of elements.
         */
        @NotNull
        @Contract("-> !null")
        @Override
        public List<T> get() {
            return get(ImmutableList.copyOf(elements));
        }

        /**
         * Returns itself.
         */
        @Override
        @NotNull
        @Contract(value="-> !null", pure=true)
        public ListBuilder<T> always() {
            return this;
        }

        /**
         * Returns either {@code this} object itself, or a new {@link NeverListBuilder} if {@code yes} is {@code false}.
         */
        @Override
        @NotNull
        public ListBuilder<T> maybe(boolean yes) {
            if(yes) {
                return this;
            } else {
                return new NeverListBuilder<>(this, this);
            }
        }

        @NotNull
        @Contract(pure=true)
        public ListBuilder<T> endMaybe() {
            return parent;
        }

    }

    /**
     * A {@link ListBuilder} which doesn't actually do anything.
     */
    protected static class NeverListBuilder<T> extends ListBuilder<T> {

        @NotNull
        private final ListBuilder<T> alwaysBuilder;

        @NotNull
        private final ListBuilder<T> parent;

        protected NeverListBuilder(@NotNull ListBuilder<T> alwaysBuilder, @NotNull ListBuilder<T> parent) {
            this.alwaysBuilder = alwaysBuilder;
            this.parent = parent;
        }

        @Override
        @Contract(pure=true)
        protected ListBuilder<T> add(@NotNull Supplier<T> element) {
            return this;
        }

        @Override
        @Contract(pure=true)
        protected ListBuilder<T> addSuppliers(@NotNull Iterable<Supplier<T>> elements) {
            return this;
        }

        @Override
        @Contract(pure=true)
        protected ListBuilder<T> addSuppliers(@NotNull Iterator<Supplier<T>> elements) {
            return this;
        }

        @Override
        @Contract(pure=true)
        protected ListBuilder<T> addSuppliers(@NotNull Supplier<T>[] elements) {
            return this;
        }

        @Override
        @Contract(pure=true)
        protected ListBuilder<T> maybeAdd(@NotNull Supplier<T> element, boolean add) {
            return this;
        }

        /**
         * Note that the never build <em>does</em> invoke the given function. The object passed in is the never builder
         * itself, so any methods the function invokes on the builder have no impact.
         */
        @Override
        public ListBuilder<T> apply(@NotNull Function<? super ListBuilder<T>, Void> function) {
            function.apply(this);
            return this;
        }

        /**
         * Returns the originating (non-conditional) list builder.
         */
        @Override
        @Contract(pure=true)
        public ListBuilder<T> always() {
            return alwaysBuilder;
        }

        /**
         * Returns a new {@link NeverListBuilder} itself. Chaining two "maybe" calls is like an AND: so if this AND that,
         * then change the state of the builder. But if the first one is already false, it doesn't matter what the second
         * one is, the aggregate is false.
         */
        @Override
        @Contract(pure=true)
        public ListBuilder<T> maybe(boolean yes) {
            return new NeverListBuilder<>(alwaysBuilder, this);
        }

        @NotNull
        @Contract(pure=true)
        public ListBuilder<T> endMaybe() {
            return parent;
        }

        /**
         * Delegates to the originating (non-conditional) list builder.
         */
        @Override
        public List<T> get() {
            return alwaysBuilder.get();
        }
    }

}
