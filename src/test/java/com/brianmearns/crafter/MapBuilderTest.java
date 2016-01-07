package com.brianmearns.crafter;

import com.brianmearns.crafter.util.InvokeCountingFunction;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link MapBuilder}
 * @author Brian Mearns <bmearns@ieee.org>
 */
public class MapBuilderTest {

    @Test
    public void test_create() {
        MapBuilder<Integer, Integer> uut = MapBuilder.create();
        MapBuilder<Integer, Integer> res = uut.put(1, 42);
        Map<Integer, Integer> map = res.get();

        assertSame("Expected return value of put(K,V) to be the same as the original object.", uut, res);
        assertEquals(new HashSet<>(Collections.singleton(1)), map.keySet());
        assertEquals(new HashSet<>(Collections.singleton(42)), new HashSet<>(map.values()));
    }

    @Test
    public void test_put() {
        MapBuilder<Integer, String> uut = MapBuilder.create(Integer.class, String.class);
        MapBuilder<Integer, String> res = uut.put(1, "andromeda");
        Map<Integer, String> map = res.get();

        assertSame("Expected return value of put(K,V) to be the same as the original object.", uut, res);
        assertEquals(new HashSet<>(Collections.singleton(1)), map.keySet());
        assertEquals(new HashSet<>(Collections.singleton("andromeda")), new HashSet<>(map.values()));
    }

    @Test
    public void test_put_builder() {
        MapBuilder<Integer, String> uut = MapBuilder.create(Integer.class, String.class);
        MapBuilder<Integer, String> res = uut.put(1, "andromeda").put(2, ValueBuilder.ofInstance("Saphron"));
        Map<Integer, String> map = res.get();

        assertSame("Expected return value of put(K,V) to be the same as the original object.", uut, res);
        assertEquals("Expected map to have specified size.", 2, map.size());
        assertEquals("Expected map to have specified key set.", new HashSet<>(ImmutableList.of(1, 2)), map.keySet());
        assertEquals("Expected specified value to be mapped to 1:", "andromeda", map.get(1));
        assertEquals("Expected specified value to be mapped to 2:", "Saphron", map.get(2));
    }

    @Test
    public void test_maybe_put_true() {
        MapBuilder<Integer, String> uut = MapBuilder.create(Integer.class, String.class);
        MapBuilder<Integer, String> res = uut.put(1, "andromeda")
                .maybePut(2, "Saphron", true)
                .put(3, "---");
        Map<Integer, String> map = res.get();

        assertSame("Expected return value of put(K,V) to be the same as the original object.", uut, res);
        assertEquals("Expected map to have specified size.", 3, map.size());
        assertEquals("Expected map to have specified key set.", new HashSet<>(ImmutableList.of(1, 2, 3)), map.keySet());
        assertEquals("Expected specified value to be mapped to 1:", "andromeda", map.get(1));
        assertEquals("Expected specified value to be mapped to 2:", "Saphron", map.get(2));
        assertEquals("Expected specified value to be mapped to 3:", "---", map.get(3));
    }

    @Test
    public void test_maybe_put_Builder_true() {
        MapBuilder<Integer, String> uut = MapBuilder.create(Integer.class, String.class);
        MapBuilder<Integer, String> res = uut.put(1, "andromeda")
                .maybePut(2, ValueBuilder.ofInstance("Saphron"), true)
                .put(3, "---");
        Map<Integer, String> map = res.get();

        assertSame("Expected return value of put(K,V) to be the same as the original object.", uut, res);
        assertEquals("Expected map to have specified size.", 3, map.size());
        assertEquals("Expected map to have specified key set.", new HashSet<>(ImmutableList.of(1, 2, 3)), map.keySet());
        assertEquals("Expected specified value to be mapped to 1:", "andromeda", map.get(1));
        assertEquals("Expected specified value to be mapped to 2:", "Saphron", map.get(2));
        assertEquals("Expected specified value to be mapped to 3:", "---", map.get(3));
    }

    @Test
    public void test_maybe_put_false() {
        MapBuilder<Integer, String> uut = MapBuilder.create(Integer.class, String.class);
        MapBuilder<Integer, String> res = uut.put(1, "andromeda")
                .maybePut(2, "Saphron", false)
                .put(3, "---");
        Map<Integer, String> map = res.get();

        assertSame("Expected return value of put(K,V) to be the same as the original object.", uut, res);
        assertEquals("Expected map to have specified size.", 2, map.size());
        assertEquals("Expected map to have specified key set.", new HashSet<>(ImmutableList.of(1, 3)), map.keySet());
        assertEquals("Expected specified value to be mapped to 1:", "andromeda", map.get(1));
        assertEquals("Expected specified value to be mapped to 3:", "---", map.get(3));
    }

    @Test
    public void test_maybe_put_Builder_false() {
        MapBuilder<Integer, String> uut = MapBuilder.create(Integer.class, String.class);
        MapBuilder<Integer, String> res = uut.put(1, "andromeda")
                .maybePut(2, ValueBuilder.ofInstance("Saphron"), false)
                .put(3, "---");
        Map<Integer, String> map = res.get();

        assertSame("Expected return value of put(K,V) to be the same as the original object.", uut, res);
        assertEquals("Expected map to have specified size.", 2, map.size());
        assertEquals("Expected map to have specified key set.", new HashSet<>(ImmutableList.of(1, 3)), map.keySet());
        assertEquals("Expected specified value to be mapped to 1:", "andromeda", map.get(1));
        assertEquals("Expected specified value to be mapped to 3:", "---", map.get(3));
    }

    @Test
    public void testApply() {
        final MapBuilder<Integer, String> uut = MapBuilder.create(Integer.class, String.class)
                .put(1, "one").put(2, "Deux");
        MapBuilder<Integer, String> res = uut.apply(new Function<MapBuilder<Integer, String>, Void>() {
            @Nullable
            @Override
            public Void apply(MapBuilder<Integer, String> input) {
                assertSame("Expected the apply() method to apply the function to the instance on which it is invoked.", uut, input);
                input.put(32, "three tens and two");
                return null;
            }
        });
        Map<Integer, String> map = res.get();

        assertSame("The apply method should return the instance it was invoked on.", uut, res);
        assertEquals("Expected map to have specified size.", 3, map.size());
        assertEquals("Expected map to have specified key set.", new HashSet<>(ImmutableList.of(1, 2, 32)), map.keySet());
        assertEquals("Expected specified value to be mapped to 1:", "one", map.get(1));
        assertEquals("Expected specified value to be mapped to 2:", "Deux", map.get(2));
        assertEquals("Expected specified value to be mapped to 32:", "three tens and two", map.get(32));
    }

    @Test
    public void testMaybe_true() {
        MapBuilder<Integer, String> uut = MapBuilder.create();
        MapBuilder<Integer, String> res = uut.maybe(true);
        assertSame("Expected maybe(true) to return the instance on which it was invoked.", uut, res);
    }

    @Test
    public void testEndMaybe() {
        MapBuilder<Integer, String> uut = MapBuilder.create();
        MapBuilder<Integer, String> res = uut.endMaybe();
        assertSame("Expected endMaybe() to return the instance on which it was invoked.", uut, res);
    }

    @Test
    public void testAlways() {
        MapBuilder<Integer, String> uut = MapBuilder.create();
        MapBuilder<Integer, String> res = uut.always();
        assertSame("Expected always() to return the instance on which it was invoked.", uut, res);
    }

    @Test
    public void testMaybe_false_put() {
        MapBuilder<Integer, String> orig = MapBuilder.create(Integer.class, String.class).put(4, "four").put(5, "not six");
        MapBuilder<Integer, String> uut = orig.maybe(false);
        MapBuilder<Integer, String> res = uut.put(7, "sept");

        assertSame("Expected put() to return the object on which it was invoked on a never builder.", uut, res);
        assertEquals("Expected map to be unaltered by the never builder's put() method.", ImmutableMap.<Integer, String>builder()
                .put(4, "four").put(5, "not six").build(), orig.get());
    }

    @Test
    public void testMaybe_false_apply() {
        MapBuilder<Integer, String> orig = MapBuilder.create(Integer.class, String.class).put(4, "four").put(5, "not six");
        MapBuilder<Integer, String> uut = orig.maybe(false);
        InvokeCountingFunction<MapBuilder<Integer, String>, Void> func = InvokeCountingFunction.reallyDoNothing();
        MapBuilder<Integer, String> res = uut.apply(func);

        assertSame("Expected apply() to return the object on which it was invoked on a never builder.", uut, res);
        assertEquals("Expected never builder's apply() method to not invoke provided function.", 0, func.getCount());
    }

    @Test
    public void testMaybe_false_get() {
        MapBuilder<Integer, String> orig = MapBuilder.create(Integer.class, String.class).put(4, "four").put(5, "not six");
        MapBuilder<Integer, String> uut = orig.maybe(false);

        assertEquals("Expected get() on never builder to return the same map that orig returns", ImmutableMap.<Integer, String>builder()
                .put(4, "four").put(5, "not six").build(), uut.get());
    }

    @Test
    public void testMaybe_false_maybe_false() {
        MapBuilder<Integer, String> orig = MapBuilder.create(Integer.class, String.class).put(4, "four").put(5, "not six");
        MapBuilder<Integer, String> uut = orig.maybe(false).maybe(false);
        uut.put(7, "sept");

        assertEquals("Expected map to be unaltered by nested never builder's put() method.", ImmutableMap.<Integer, String>builder()
                .put(4, "four").put(5, "not six").build(), orig.get());
    }

    @Test
    public void testMaybe_false_maybe_true() {
        MapBuilder<Integer, String> orig = MapBuilder.create(Integer.class, String.class).put(4, "four").put(5, "not six");
        MapBuilder<Integer, String> uut = orig.maybe(false).maybe(true);
        uut.put(7, "sept");

        assertEquals("Expected maybe(true) on a never builder to return another never builder.", ImmutableMap.<Integer, String>builder()
                .put(4, "four").put(5, "not six").build(), orig.get());
    }

    @Test
    public void testMaybe_false_endMaybe() {
        MapBuilder<Integer, String> orig = MapBuilder.create(Integer.class, String.class).put(4, "four").put(5, "not six");
        MapBuilder<Integer, String> uut = orig.maybe(false);
        MapBuilder<Integer, String> res = uut.endMaybe();

        assertSame("Expected endMaybe() to return the parent builder from a never builder.", orig, res);
    }

    @Test
    public void testMaybe_false_always() {
        MapBuilder<Integer, String> orig = MapBuilder.create(Integer.class, String.class).put(4, "four").put(5, "not six");
        MapBuilder<Integer, String> uut = orig.maybe(false).maybe(true).maybe(false);
        MapBuilder<Integer, String> res = uut.always();

        assertSame("Expected always() to return the always builder from a nested never builder.", orig, res);
    }

}
