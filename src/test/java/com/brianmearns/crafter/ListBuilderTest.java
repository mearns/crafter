package com.brianmearns.crafter;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

/**
 * @author Brian Mearns <bmearns@ieee.org>
 */
public class ListBuilderTest {

    @Test
    public void testAdd_T() throws Exception {
        ListBuilder<String> uut = ListBuilder.create();
        ListBuilder<String> res = uut.add("foo").add("bar").add("baz");

        assertSame("Expect value returned by add(T) is the original builder.", uut, res);
        assertArrayEquals("Expected add to add the given elements to the list, in order.", new String[]{"foo", "bar", "baz"}, uut.get().toArray());
    }

    @Test
    public void testAddAll_Iterable_T_withArray() throws Exception {
        final String[] expected = new String[]{"burley", "hurley", "Fran"};
        ListBuilder<String> uut = ListBuilder.create();
        ListBuilder<String> res = uut.addAll(expected);

        assertSame("Expect value returned by add(T) is the original builder.", uut, res);
        assertArrayEquals("Expected addAll to add the given array to the list.", expected, uut.get().toArray());
    }

    @Test
    public void testAddAll_Iterable_T_withCollection() throws Exception {
        final List<String> expected = Arrays.asList("burley", "hurley", "Fran");
        ListBuilder<String> uut = ListBuilder.create();
        ListBuilder<String> res = uut.addAll(expected);

        assertSame("Expect value returned by add(T) is the original builder.", uut, res);
        assertArrayEquals("Expected addAll to add the given list to the list.", expected.toArray(), uut.get().toArray());
    }
}