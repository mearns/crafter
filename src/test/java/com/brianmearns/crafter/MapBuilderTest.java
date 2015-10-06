package com.brianmearns.crafter;

import com.google.common.collect.ImmutableList;
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

}
