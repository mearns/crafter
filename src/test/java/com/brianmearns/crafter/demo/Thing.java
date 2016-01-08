package com.brianmearns.crafter.demo;

import com.brianmearns.crafter.*;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Thing {

    @Nonnull
    public static Builder builder() {
        return new Builder();
    }

    private final String str;

    private final Integer i;

    @Nonnull
    private final String[] array;

    @Nonnull
    private final Map<String, Double> map;


    private Thing(@Nullable String str, @Nullable Integer i, @Nonnull String[] array, @Nonnull Map<String, Double> map) {
        this.str = str;
        this.i = i;
        this.array = Arrays.copyOf(array, array.length);
        this.map = ImmutableMap.copyOf(map);
    }

    public String getStr() {
        return str;
    }

    public Integer getI() {
        return i;
    }

    @Nonnull
    public String[] getArray() {
        return Arrays.copyOf(array, array.length);
    }

    @Nonnull
    public Map<String, Double> getMap() {
        return ImmutableMap.copyOf(map);
    }

    public static class Builder implements BuilderInterface<Thing> {

        @Nonnull
        public final ValueBuilder<String> str = ValueBuilder.create();

        @Nonnull
        public final ValueBuilder<Integer> i = ValueBuilder.create();

        @Nonnull
        public final ListBuilder<String> array = ListBuilder.create();

        @Nonnull
        public final MapBuilder<String, Double> map = MapBuilder.create();

        @Nonnull
        @Override
        public Thing get() throws IncompleteBuilderException {
            List<String> arrayList = array.get();
            return new Thing(str.get(), i.get(), arrayList.toArray(new String[arrayList.size()]), map.get());
        }

        @Nonnull
        public Builder apply(Function<Builder, Void> func) {
            func.apply(this);
            return this;
        }
    }
}
