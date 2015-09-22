package com.brianmearns.crafter;

public class AlwaysBuilder<T> extends AbstractConditionalBuilder<T> {

    
    @Override
    @NotNull
    public ObjectBuilder<T> apply(@NotNull Function<ObjectBuilder<T>, ?> callback) {
        callback.apply(this);
        return this;
    }

    @Override
    @NotNull
    public ConditionalBuilder<T> maybe(boolean apply) {
        if (apply) {
            return this;
        }
        return new NeverBuilder<>(this.always());
    }

    @Override
    @NotNull
    public ConditionalBuilder<T> maybe(@NotNull Callable<Boolean> applyCallback);

    @Override
    @NotNull
    public ConditionalBuilder<T> maybe(@NotNull Function<ObjectBuilder<T>, Boolean> applyCallback);

}

