package com.brianmearns.crafter.util;

import com.google.common.base.Function;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public abstract class InvokeCountingFunction<F, T> implements Function<F, T> {

    @NotNull
    public static <F, T> InvokeCountingFunction<F, T> doNothing() {
        return new DoNothingCountingFunction<>();
    }

    @NotNull
    public static <F> InvokeCountingFunction<F, Void> reallyDoNothing() {
        return new DoNothingCountingFunction<>();
    }

    private int count = 0;

    @Override
    public final T apply(F input) {
        this.count += 1;
        return reallyApply(input);
    }

    protected abstract T reallyApply(F input);

    public int getCount() {
        return count;
    }

    public static class DoNothingCountingFunction<F, T> extends InvokeCountingFunction<F, T> {

        private DoNothingCountingFunction() {

        }

        @Override
        protected T reallyApply(F input) {
            return null;
        }
    }
}
