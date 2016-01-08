package com.brianmearns.crafter;

import com.brianmearns.crafter.util.SupplierFunctions;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * A {@link BuilderInterface} for a list of values.
 *
 * @author Brian Mearns <bmearns@ieee.org>
 */
@SuppressWarnings("unused")
public abstract class ListBuilder<T> implements BuilderInterface<List<T>> {

    /**
     * Static factory method to create an instance.
     */
    @Nonnull
    public static <T> ListBuilder<T> create() {
        return new DefaultListBuilder<>();
    }

    /**
     * Static factory method to create an instance.
     * @param cls Specifies the type {@code T} of the builder returned.
     */
    @Nonnull
    public static <T> ListBuilder<T> create(Class<T> cls) {
        return new DefaultListBuilder<>();
    }

    /**
     * Helper method for adding an element as a supplier of that element.
     * @param element Supplier of the element to add.
     *
     * @return {@code this} object itself, for chaining convenience.
     */
    @Nonnull
    protected abstract ListBuilder<T> add(@Nonnull Supplier<? extends T> element);

    /**
     * Helper method for adding an iterable of suppliers of elements.
     *
     * @param elements Suppliers for all the elements to add.
     *
     * @return {@code this} object itself, for chaining convenience.
     */
    @Nonnull
    protected abstract ListBuilder<T> addSuppliers(@Nonnull Iterable<? extends Supplier<? extends T>> elements);

    /**
     * Helper method for adding an iterator of suppliers of elements.
     *
     * @param elements Suppliers for all the elements to add.
     *
     * @return {@code this} object itself, for chaining convenience.
     */
    @Nonnull
    protected abstract ListBuilder<T> addSuppliers(@Nonnull Iterator<? extends Supplier<? extends T>> elements);

    /**
     * Helper method for adding an array of suppliers of elements.
     *
     * @param elements Suppliers for all the elements to add.
     *
     * @return {@code this} object itself, for chaining convenience.
     */
    @Nonnull
    protected abstract ListBuilder<T> addSuppliers(@Nonnull Supplier<? extends T>[] elements);

    /**
     * Helper method to add the given element, if and only if the given boolean is {@code true}. Otherwise
     * has no effect on the state of the builder.
     *
     * @param element A supplier for the element to {@link #add(Supplier)}.
     * @param add Whether or not to add the element.
     *
     * @return {@code this} object itself, for chaining convenience.
     */
    @Nonnull
    protected abstract ListBuilder<T> maybeAdd(@Nonnull Supplier<? extends T> element, boolean add);

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
    @Nonnull
    public ListBuilder<T> addAll(@Nonnull Iterable<? extends T> elements) {
        return addSuppliers(Iterables.transform(elements, SupplierFunctions.<T>supplierOfInstanceFunction()));
    }

    /**
     * Add all of the given {@link BuilderInterface Builders}, in order, as builders of element values to the end of the list.
     *
     * <p>
     * As with {@link #add(BuilderInterface)}, this does not invoke any of the builders immediately to build the element value.
     * Each builder will be invoked once during a call to {@link #get()}.
     *
     * @param elements An {@link Iterable} of the builders to add.
     *
     * @return {@code this} object itself, for chaining convenience.
     *
     * @see #addBuilders(Iterator)
     * @see #addBuilders(BuilderInterface[])
     * @see #addAll(Iterable)
     */
    @Nonnull
    public ListBuilder<T> addBuilders(@Nonnull Iterable<? extends BuilderInterface<? extends T>> elements) {
        return addSuppliers(elements);
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
    @Nonnull
    public ListBuilder<T> addAll(@Nonnull Iterator<? extends T> elements) {
        return addSuppliers(Iterators.transform(elements, SupplierFunctions.<T>supplierOfInstanceFunction()));
    }

    /**
     * Add all of the given {@link BuilderInterface Builders}, in order, as builders of element values to the end of the list.
     *
     * <p>
     * As with {@link #add(BuilderInterface)}, this does not invoke any of the builders immediately to build the element value.
     * Each builder will be invoked once during a call to {@link #get()}.
     *
     * @param elements An {@link Iterator} over the builders to add.
     *
     * @return {@code this} object itself, for chaining convenience.
     *
     * @see #addBuilders(Iterable)
     * @see #addBuilders(BuilderInterface[])
     * @see #addAll(Iterator)
     */
    @Nonnull
    public ListBuilder<T> addBuilders(@Nonnull Iterator<? extends BuilderInterface<? extends T>> elements) {
        return addSuppliers(elements);
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
     * @see #addBuilders(BuilderInterface[])
     */
    @Nonnull
    public ListBuilder<T> addAll(@Nonnull T[] elements) {
        return addSuppliers(Iterables.transform(Arrays.asList(elements), SupplierFunctions.<T>supplierOfInstanceFunction()));
    }

    /**
     * Add all of the given {@link BuilderInterface Builders}, in order, as builders of element values to the end of the list.
     *
     * <p>
     * As with {@link #add(BuilderInterface)}, this does not invoke any of the builders immediately to build the element value.
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
    @Nonnull
    public ListBuilder<T> addBuilders(@Nonnull BuilderInterface<? extends T>[] elements) {
        return addSuppliers(elements);
    }

    /**
     * Add the given item as the next element in the list.
     *
     * @param element The element to append to the list.
     *
     * @return {@code this} object itself, for chaining convenience.
     */
    @Nonnull
    public ListBuilder<T> add(@Nullable T element) {
        return add(Suppliers.ofInstance(element));
    }

    /**
     * Add the given {@link BuilderInterface} as a builder for the next item in the list. The builder is not invoked to build
     * the value immediately, its {@link BuilderInterface#get()} method is invoked to build the element value only when <em>this</em>
     * object's {@link #get()} method is invoked to build a list of values.
     *
     * @param elementBuilder Builder for the element to put at the end of the current list of elements.
     *
     * @return {@code this} object itself, for chaining convenience.
     */
    @Nonnull
    public ListBuilder<T> add(@Nonnull BuilderInterface<? extends T> elementBuilder) {
        return add((Supplier<? extends T>) elementBuilder);
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
    @Nonnull
    public ListBuilder<T> maybeAdd(@Nullable T element, boolean add) {
        return maybeAdd(Suppliers.ofInstance(element), add);
    }

    /**
     * Build a new list using the elements specified for this builder.
     *
     * @return The built list of elements.
     */
    @Nonnull
    @Override
    public abstract List<T> get() throws IncompleteBuilderException;

    /**
     * Adds the given element builder for the next item in the list, if and only if the
     * given boolean is {@code true}. Otherwise has no effect on the state of the builder.
     *
     * @param element The element value to {@link #add(BuilderInterface)}.
     * @param add Whether or not to add the element.
     *
     * @return {@code this} object itself, for chaining convenience.
     */
    @Nonnull
    public ListBuilder<T> maybeAdd(@Nonnull BuilderInterface<? extends T> element, boolean add) {
        return maybeAdd((Supplier<? extends T>) element, add);
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
     * @return This {@code ListBuilder} itself, for chaining convenience.
     */
    @Nonnull
    public abstract ListBuilder<T> apply(@Nonnull Function<ListBuilder<T>, Void> function);

    /**
     * Returns the top-level non-conditional builder.
     */
    @Nonnull
    public abstract ListBuilder<T> always();

    /**
     * Returns a builder which either does or doesn't delegate to this builder based on the given boolean.
     * @param yes If {@code true}, then methods invoked on the returned builder will modify the state of {@code this}
     *            builder. Otherwise, methods invoked on the returned builder will not modify state.
     */
    @Nonnull
    public abstract ListBuilder<T> maybe(boolean yes);

    /**
     * Returns the parent list builder of a conditional list builder. This is not necessarily the originating top level
     * builder if you have nested (or rather chained) calls to {@link #maybe(boolean)}.
     */
    @Nonnull
    public abstract ListBuilder<T> endMaybe();

    @SuppressWarnings("unused")
    protected static class DefaultListBuilder<T> extends ListBuilder<T> {

        @Nonnull
        private final List<Supplier<? extends T>> elements;

        {
            //Linked list is good because we're mostly just appending to it, and then iterating through it.
            // Linked lists are pretty good at both.
            elements = new LinkedList<>();
        }

        public DefaultListBuilder() {

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
        @Nonnull
        protected List<T> get(@Nonnull ImmutableList<Supplier<? extends T>> suppliers) {
            ArrayList<T> list = new ArrayList<>(suppliers.size());
            for (Supplier<? extends T> supplier : suppliers) {
                list.add(supplier.get());
            }
            return list;
        }

        @Override
        @Nonnull
        protected ListBuilder<T> add(@Nonnull Supplier<? extends T> element) {
            elements.add(element);
            return this;
        }

        @Override
        @Nonnull
        protected ListBuilder<T> addSuppliers(@Nonnull Iterable<? extends Supplier<? extends T>> elements) {
            Iterables.addAll(this.elements, elements);
            return this;
        }

        @Override
        @Nonnull
        protected ListBuilder<T> addSuppliers(@Nonnull Iterator<? extends Supplier<? extends T>> elements) {
            while(elements.hasNext()) {
                this.elements.add(elements.next());
            }
            return this;
        }

        @Override
        @Nonnull
        protected ListBuilder<T> addSuppliers(@Nonnull Supplier<? extends T>[] elements) {
            return addSuppliers(Arrays.asList(elements));
        }

        @Override
        @Nonnull
        protected ListBuilder<T> maybeAdd(@Nonnull Supplier<? extends T> element, boolean add) {
            if(add) {
                this.add(element);
            }
            return this;
        }

        @Override
        @Nonnull
        public ListBuilder<T> apply(@Nonnull Function<ListBuilder<T>, Void> function) {
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
        @Nonnull
        @Override
        public List<T> get() throws IncompleteBuilderException {
            return get(ImmutableList.copyOf(elements));
        }

        /**
         * Returns itself.
         */
        @Override
        @Nonnull
        public ListBuilder<T> always() {
            return this;
        }

        /**
         * Returns either {@code this} object itself, or a new {@link NeverListBuilder} if {@code yes} is {@code false}.
         */
        @Override
        @Nonnull
        public ListBuilder<T> maybe(boolean yes) {
            if(yes) {
                return this;
            } else {
                return new NeverListBuilder<>(this, this);
            }
        }

        @Nonnull
        @Override
        public ListBuilder<T> endMaybe() {
            return this;
        }

    }

    /**
     * A {@link ListBuilder} which doesn't actually do anything.
     */
    protected static class NeverListBuilder<T> extends ListBuilder<T> {

        @Nonnull
        private final ListBuilder<T> alwaysBuilder;

        @Nonnull
        private final ListBuilder<T> parent;

        protected NeverListBuilder(@Nonnull ListBuilder<T> alwaysBuilder, @Nonnull ListBuilder<T> parent) {
            this.alwaysBuilder = alwaysBuilder;
            this.parent = parent;
        }

        @Override
        @Nonnull
        protected ListBuilder<T> add(@Nonnull Supplier<? extends T> element) {
            return this;
        }

        @Override
        @Nonnull
        protected ListBuilder<T> addSuppliers(@Nonnull Iterable<? extends Supplier<? extends T>> elements) {
            return this;
        }

        @Override
        @Nonnull
        protected ListBuilder<T> addSuppliers(@Nonnull Iterator<? extends Supplier<? extends T>> elements) {
            return this;
        }

        @Override
        @Nonnull
        protected ListBuilder<T> addSuppliers(@Nonnull Supplier<? extends T>[] elements) {
            return this;
        }

        @Override
        @Nonnull
        protected ListBuilder<T> maybeAdd(@Nonnull Supplier<? extends T> element, boolean add) {
            return this;
        }

        /**
         * Note that the never build <em>does not</em> invoke the given function at all, it simply returns itself, as usual.
         */
        @Nonnull
        @Override
        public ListBuilder<T> apply(@Nonnull Function<ListBuilder<T>, Void> function) {
            return this;
        }

        /**
         * Returns the originating (non-conditional) list builder.
         */
        @Override
        @Nonnull
        public ListBuilder<T> always() {
            return alwaysBuilder;
        }

        /**
         * Returns a new {@link NeverListBuilder} itself. Chaining two "maybe" calls is like an AND: so if this AND that,
         * then change the state of the builder. But if the first one is already false, it doesn't matter what the second
         * one is, the aggregate is false.
         */
        @Override
        @Nonnull
        public ListBuilder<T> maybe(boolean yes) {
            return new NeverListBuilder<>(alwaysBuilder, this);
        }

        @Nonnull
        public ListBuilder<T> endMaybe() {
            return parent;
        }

        /**
         * Delegates to the originating (non-conditional) list builder.
         */
        @Override
        @Nonnull
        public List<T> get() throws IncompleteBuilderException {
            return alwaysBuilder.get();
        }
    }

}
