package com.brianmearns.crafter;

public abstract class AbstractBuilder<T> implements Builder<T> {

    public ObjectBuilder<T> apply(Function<ObjectBuilder<T>, ?> callback) {
        callback.apply(this);
        return this;
    }

}

