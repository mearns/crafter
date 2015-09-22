package com.brianmearns.crafter;

public class CallingConditionalBuilder<T> extends AbstractConditionalBuilder<T> {

    @NotNull
    private final Callable<Boolean> callback;

    public CallingConditionalBuilder(@notnull ObjectBuilder<T> parentBuilder, @NotNull Callable<Boolean> callback) {
        super(parentBuilder);
        this.callback = callback;
    }

    @NotNull
    protected RuntimeException handleException(Exception e) {
        return new RuntimeException(e);
    }

    protected boolean apply() throws RuntimeException {
        try {
            return callback.call(this);
        }
        catch (Exception e) {
            throw handleException(e);
        }
    }

    @Override
    @NotNull
    public ObjectBuilder<T> apply(@NotNull Function<ObjectBuilder<T>, ?> callback) {
        if(apply()) {
            callback.apply(this);
        }
        return this;
    }

    @Override
    @NotNull
    public ConditionalBuilder<T> maybe(boolean apply) {
        if(apply && this.apply()) {
            return new AlwaysBuilder(this.always());
        }
        return new NeverBuilder(this.always());
    }

    @Override
    @NotNull
    public ConditionalBuilder<T> maybe(@NotNull Callable<Boolean> applyCallback) {
        if(this.apply()) {
            return new CallingConditionalBuilder<>(this.always(), applyCallback);
        }
        return new NeverBuilder(this.always());
    }

    @Override
    @NotNull
    public ConditionalBuilder<T> maybe(@NotNull Function<ObjectBuilder<T>, Boolean> applyCallback) {
        if(this.apply()) {
            return new ApplyingConditionalBuilder<>(this.always(), applyCallback);
        }
        return new NeverBuilder(this.always());
    }
    

}

