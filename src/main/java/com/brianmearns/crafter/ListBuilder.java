package com.brianmearns.crafter;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.sun.istack.internal.NotNull;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * Copyright 2015 Commerce Technologies, Inc.
 *
 * @author Brian Mearns <bmearns@commercehub.com>
 */
@SuppressWarnings("unused")
public class ListBuilder<T> implements Builder<List<T>> {

    private List<Supplier<T>> elements = new LinkedList<>();

    public ListBuilder(@NotNull Iterable<T> elements) {
        this();
        addAll(elements);
    }

    public ListBuilder(@NotNull Iterator<T> elements) {
        this();
        addAll(elements);
    }

    public ListBuilder() {

    }

    public ListBuilder<T> add(@NotNull Supplier<T> element) {
        elements.add(element);
        return this;
    }

    public ListBuilder<T> add(T element) {
        return add(Suppliers.ofInstance(element));
    }

    /**
     * Adds the given element, if and only if the given boolean is {@code true}. Otherwise
     * has no effect on the state of the builder.
     *
     * @param element A supplier for the element to add.
     * @param add whether or not to add the element.
     * @return This {@code ListBuilder} object itself, for chaining convenience.
     */
    public ListBuilder<T> maybeAdd(@NotNull Supplier<T> element, boolean add) {
        if(add) {
            this.add(element);
        }
        return this;
    }

    public ListBuilder<T> maybeAdd(T element, boolean add) {
        return maybeAdd(Suppliers.ofInstance(element), add);
    }

    /**
     * Invokes an arbitrary Function, passing {@code this} as the only parameter, and ignoring the output.
     * This is a way to introduce arbitrary code into the middle of a builder chain, if you really want to.
     *
     * @param function The function to apply to this object.
     * @return This {@code ListBuilder} object itself, for chaining convenience.
     */
    public ListBuilder<T> apply(Function<ListBuilder<T>, ?> function) {
        function.apply(this);
        return this;
    }


    /**
     * Helper method to actually build the list returned by {@link #get()}.
     *
     * @param suppliers An immutable list of the suppliers for the element values.
     *
     * @return A list that will be returned by {@link #get()}
     */
    protected List<T> get(ImmutableList<Supplier<T>> suppliers) {
        List<T> res = new ArrayList<>(suppliers.size());
        for(Supplier<T> supplier : suppliers) {
            res.add(supplier.get());
        }
        return res;
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
    @Override
    public List<T> get() {
        return get(ImmutableList.copyOf(elements));
    }
}
