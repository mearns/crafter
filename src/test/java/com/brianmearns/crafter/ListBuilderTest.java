package com.brianmearns.crafter;

import com.google.common.collect.Lists;
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
    public void testAdd_Builder_T() throws Exception {
        ListBuilder<String> uut = ListBuilder.create();
        ListBuilder<String> res = uut
                .add(new ValueBuilder<>("foo"))
                .add(new ValueBuilder<>("bar"))
                .add(new ValueBuilder<>("baz"));

        assertSame("Expect value returned by add(T) is the original builder.", uut, res);
        assertArrayEquals("Expected add to add the given elements to the list, in order.", new String[]{"foo", "bar", "baz"}, uut.get().toArray());
    }

    @Test
    public void testAddAll_T_array() throws Exception {
        final String[] expected = new String[]{"burley", "hurley", "Fran"};
        ListBuilder<String> uut = ListBuilder.create();
        ListBuilder<String> res = uut.addAll(expected);

        assertSame("Expect value returned by add(T) is the original builder.", uut, res);
        assertArrayEquals("Expected addAll(T[]) to add the given array to the list.", expected, uut.get().toArray());
    }

    @Test
    public void testAddAll_Builder_T_array() throws Exception {
        final String[] expected = new String[]{"burley", "hurley", "Fran"};
        @SuppressWarnings("unchecked")
        final Builder<String>[] builders = new Builder[] {
                new ValueBuilder<>(expected[0]),
                new ValueBuilder<>(expected[1]),
                new ValueBuilder<>(expected[2])
        };
        ListBuilder<String> uut = ListBuilder.create();
        ListBuilder<String> res = uut.addBuilders(builders);

        assertSame("Expect value returned by add(T) is the original builder.", uut, res);
        assertArrayEquals("Expected addAll(T[]) to add the given array to the list.", expected, uut.get().toArray());
    }

    @Test
    public void testAddAll_Iterable_T() throws Exception {
        final List<String> expected = Arrays.asList("giraffe", "goat", "pizza", "hang glider");
        ListBuilder<String> uut = ListBuilder.create();
        ListBuilder<String> res = uut.addAll(expected);

        assertSame("Expect value returned by add(T) is the original builder.", uut, res);
        assertArrayEquals("Expected addAll(Collection<T>) to add the given list to the list.", expected.toArray(), uut.get().toArray());
    }

    @Test
    public void testAddAll_Iterable_Builder_T() throws Exception {
        final String[] expected = new String[] {"giraffe", "goat", "pizza", "hang glider"};
        final List<? extends Builder<String>> builders = Lists.transform(Arrays.asList(expected), ValueBuilder.<String>ofInstanceFunction());
        ListBuilder < String > uut = ListBuilder.create();
        ListBuilder<String> res = uut.addBuilders(builders);

        assertSame("Expect value returned by add(T) is the original builder.", uut, res);
        assertArrayEquals("Expected addAll(Collection<T>) to add the given list to the list.", expected, uut.get().toArray());
    }

    @Test
    public void testAddAll_Iterator_T() throws Exception {
        final List<String> expected = Arrays.asList("carrots", "cars", "cauliflower");
        ListBuilder<String> uut = ListBuilder.create();
        ListBuilder<String> res = uut.addAll(expected.iterator());

        assertSame("Expect value returned by add(T) is the original builder.", uut, res);
        assertArrayEquals("Expected addAll(Iterator<T>) to add the given items to the list.", expected.toArray(), uut.get().toArray());
    }

}