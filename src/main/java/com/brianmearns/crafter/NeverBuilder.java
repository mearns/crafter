package com.brianmearns.crafter;

public class NeverBuilder<T> extends AbstractConditionalBuilder<T> {

    @Override
    @NotNull
    public ObjectBuilder<T> apply(@NotNull Function<ObjectBuilder<T>, ?> callback) {
        return this;
    }

    @Override
    @NotNull
    public ConditionalBuilder<T> maybe(boolean apply) {
        return this;
    }

    @Override
    @NotNull
    public ConditionalBuilder<T> maybe(@NotNull Callable<Boolean> applyCallback) {
        return this;
    }

    @Override
    @NotNull
    public ConditionalBuilder<T> maybe(@NotNull Function<ObjectBuilder<T>, Boolean> applyCallback) {
        return this;
    }
}

