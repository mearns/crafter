package com.brianmearns.crafter.demo;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class ThingTest {

    @Test
    public void testHappyPath() {
        Thing.Builder uut = Thing.builder();
        uut.str.set("My String");
        uut.i.set(44);
        uut.array.add("foo").add("bar");
        uut.map.put("forty-four", 44.0);

        //XXX: Oops. This is not fluent at all. Probably need the builders to have a parent and a method to escape to it,
        // which will need to be part of the type.

        Thing res = uut.get();

        assertEquals("My String", res.getStr());
        assertEquals((Integer)44, res.getI());
        assertArrayEquals(ImmutableList.of("foo", "bar").toArray(new String[2]), res.getArray());
        assertEquals(ImmutableMap.of("forty-four", 44.0), res.getMap());
    }


}