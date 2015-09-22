package com.brianmearns.crafter;

public class ValueBuilder<T> extends AbstractBuilder<T> {

    private T value;

    public ValueBuilder() {
        this(null);
    }

    public ValueBuilder(T value) {
        set(value);
    }

    @Override
    public T get() {
        return value;
    }

    public ValueBuilder<T> set(T value) {
        this.value = value;
    }

    @Override
    public ConditionalBuilder<T> maybe(boolean apply) {

    }
}

