package com.brianmearns.crafter.util;

import com.brianmearns.crafter.BuilderInterface;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;



/**
 * @author Brian Mearns <bmearns@ieee.org>
 */
@SuppressWarnings("unused")
public abstract class SupplierFunctions {


    /**
     * Create and return a new {@link Function} which takes an instance of type {@code T} and returns
     * a supplier for that instance, using {@link Suppliers#ofInstance(Object)}.
     *
     * @param <T> The type of object which the function will turn into a supplier, of the same type.
     *
     * @return A new {@link SupplierOfInstanceFunction} which transforms an instance into a {@link Supplier}
     * of that instance.
     */
    @Nonnull
    public static <T> SupplierOfInstanceFunction<T> supplierOfInstanceFunction() {
        return new SupplierOfInstanceFunction<>();
    }

    @Nonnull
    public static <T> BuilderToSupplierFunction<T> builderToSupplierFunction() {
        return new BuilderToSupplierFunction<>();
    }

    /**
     * A simple function that maps an instance to a {@link Supplier} of that instance, using
     * {@link Suppliers#ofInstance(Object)}.
     *
     * @param <T> the type of the object which the function will take as input. The function will return a type
     *           of {@code Supplier<T>}, i.e., a {@link Supplier} which returns this type from it's {@link Supplier#get()}
     *           method.
     */
    public static class SupplierOfInstanceFunction<T> implements Function<T, Supplier<T>> {
        @Nonnull
        @Override
        public Supplier<T> apply(@Nullable T input) {
            return Suppliers.ofInstance(input);
        }
    }

    /**
     * Function required for type conversion from {@link BuilderInterface} to {@link Supplier}, which simply returns any
     * {@link BuilderInterface} given to it.
     */
    public static class BuilderToSupplierFunction<T> implements Function<BuilderInterface<T>, Supplier<T>> {
        @Override
        @Nullable
        public Supplier<T> apply(@Nullable BuilderInterface<T> input) {
            return input;
        }
    }
}
