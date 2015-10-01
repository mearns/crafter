package com.brianmearns.crafter.util;

import com.brianmearns.crafter.Builder;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

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
    @NotNull
    @Contract(" -> !null")
    public static <T> SupplierOfInstanceFunction<T> supplierOfInstanceFunction() {
        return new SupplierOfInstanceFunction<>();
    }

    @NotNull
    @Contract(" -> !null")
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
        @NotNull
        @Override
        @Contract("_ -> !null")
        public Supplier<T> apply(@Nullable T input) {
            return Suppliers.ofInstance(input);
        }
    }

    /**
     * Function required for type conversion from {@link Builder} to {@link Supplier}, which simply returns any
     * {@link Builder} given to it.
     */
    public static class BuilderToSupplierFunction<T> implements Function<Builder<T>, Supplier<T>> {
        @Override
        public Supplier<T> apply(@Nullable Builder<T> input) {
            return input;
        }
    }
}
