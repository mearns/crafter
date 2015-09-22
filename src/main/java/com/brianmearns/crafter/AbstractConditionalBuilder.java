package com.brianmearns.crafter;

public abstract class AbstractConditionalBuilder<T> implements ConditionalBuilder<T> {

    private final ObjectBuilder<T> parentBuilder;

    protected AbstractConditionalBuilder(@NotNull ObjectBuilder<T> parentBuilder) {
        this.parentBuilder = parentBuilder;
    }

    @Override
    @NotNull
    public ObjectBuilder<T> always() {
        return parentBuilder;
    }

}

