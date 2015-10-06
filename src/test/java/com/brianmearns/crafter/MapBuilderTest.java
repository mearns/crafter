package com.brianmearns.crafter;

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

}
