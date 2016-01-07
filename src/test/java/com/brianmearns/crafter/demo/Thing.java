package com.brianmearns.crafter.demo;

import com.brianmearns.crafter.*;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Thing {

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    private final String str;

    private final Integer i;

    @NotNull
    private final String[] array;

    @NotNull
    private final Map<String, Double> map;


    private Thing(String str, Integer i, @NotNull String[] array, @NotNull Map<String, Double> map) {
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

    @NotNull
    public String[] getArray() {
        return Arrays.copyOf(array, array.length);
    }

    @NotNull
    public Map<String, Double> getMap() {
        return ImmutableMap.copyOf(map);
    }

    public static class Builder implements BuilderInterface<Thing> {

        @NotNull
        public final ValueBuilder<String> str = ValueBuilder.create();

        @NotNull
        public final ValueBuilder<Integer> i = ValueBuilder.create();

        @NotNull
        public final ListBuilder<String> array = ListBuilder.create();

        @NotNull
        public final MapBuilder<String, Double> map = MapBuilder.create();

        @NotNull
        @Override
        public Thing get() throws IncompleteBuilderException {
            List<String> arrayList = array.get();
            return new Thing(str.get(), i.get(), arrayList.toArray(new String[arrayList.size()]), map.get());
        }

        @NotNull
        public Builder apply(Function<Builder, Void> func) {
            func.apply(this);
            return this;
        }
    }
}
