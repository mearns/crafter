package com.brianmearns.crafter;

/**
 * A {@code ConditionalBuilder} is a builder which is tied to a parent builder
 * and whose state change methods either change the state in the parent builder,
 * or don't do anything.
 *
 * These are normally created with {@link ObjectBuilder#maybe(boolean)} or
 * a similar method.
 *
 * Use {@link always()} to get the parent builder.
 */
public interface ConditionalBuilder<T> extends ObjectBuilder<T> {

    /**
     * Returns the parent {@code ObjectBuilder} from which this conditional
     * builder originated.
     */
    public ObjectBuilder<T> always();

}

