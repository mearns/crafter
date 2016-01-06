package com.brianmearns.crafter;

import com.google.common.base.Supplier;
import org.jetbrains.annotations.Nullable;

/**
 * A {@code Builder} implements the builder design pattern, in which one object is used to
 * construct instances of a particular type, after being setup step by step with values to
 * transfer to the built instance.
 *
 * <p>
 * This interface extends the {@link Supplier} interface but does not define any additional
 * methods on the interface. This is meant primarily as a tagging interface, used to indicate
 * the <em>intention</em> of the implementing class to be a builder. The {@link Supplier#get()}
 * method defined by the {@link Supplier} interface is the one that should actually build
 * instances.
 *
 * <p>
 * Builders are typically characterized by a <em>fluent</em>, <em>chainable</em> function interface,
 * in which each configurable property of the object to be built has a method for setting its value
 * in the builder, and that method return the builder itself back for chaining.
 *
 * @author Brian Mearns <bmearns@ieee.org>
 */
public interface Builder<T> extends Supplier<T> {

    /**
     * This is the method that should actually build and return an instance of type {@code T},
     * based on the current configuration of this {@code Builder} object.
     *
     * @return A newly created instance of type {@code T}.
     */
    @Override
    @Nullable
    T get();
}
