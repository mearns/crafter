package com.brianmearns.crafter;

import com.brianmearns.crafter.util.InvokeCountingFunction;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Brian Mearns <bmearns@ieee.org>
 */
public class ListBuilderTest {

    @Test
    public void testMaybe_false() {
        ListBuilder<String> uut = ListBuilder.create(String.class)
                .add("foo").add("Bar").maybe(false).add("Trot").add("Burger").always().add("thunder");

        assertArrayEquals(new String[]{
                "foo", "Bar", "thunder"
        }, uut.get().toArray());
    }

    @Test
    public void testMaybe_true() {
        ListBuilder<String> uut = ListBuilder.create(String.class)
                .add("foo").add("Bar").maybe(true).add("Trot").add("Burger").always().add("thunder");

        assertArrayEquals(new String[]{
                "foo", "Bar", "Trot", "Burger", "thunder"
        }, uut.get().toArray());
    }

    @Test
    public void testMaybe_nested_false_true() {
        ListBuilder<String> uut = ListBuilder.create(String.class)
                .add("foo").add("Bar")
                    .maybe(false).add("Trot").add("Burger")
                    .maybe(true).add("Barn").add("owl")
                .always().add("thunder");

        assertArrayEquals(new String[]{
                "foo", "Bar", "thunder"
        }, uut.get().toArray());
    }

    @Test
    public void testMaybe_nested_true_false() {
        ListBuilder<String> uut = ListBuilder.create(String.class)
                .add("foo").add("Bar")
                    .maybe(true).add("Trot").add("Burger")
                    .maybe(false).add("rough").add("evangelist")
                .always().add("thunder");

        assertArrayEquals(new String[]{
                "foo", "Bar", "Trot", "Burger", "thunder"
        }, uut.get().toArray());
    }


    @Test
    public void testMaybe_nested_true_true() {
        ListBuilder<String> uut = ListBuilder.create(String.class)
                .add("foo").add("Bar")
                .maybe(true).add("Trot").add("Burger")
                .maybe(true).add("rough").add("evangelist")
                .always().add("thunder");

        assertArrayEquals(new String[]{
                "foo", "Bar", "Trot", "Burger", "rough", "evangelist", "thunder"
        }, uut.get().toArray());
    }

    @Test
    public void testMaybe_nested_endMaybe() {
        ListBuilder<String> uut = ListBuilder.create(String.class)
                .add("foo").add("Bar")
                .maybe(true).add("Trot").add("Burger")
                .maybe(false).add("rough").add("evangelist")
                .endMaybe().add("turtle")
                .always().add("thunder");

        assertArrayEquals(new String[]{
                "foo", "Bar", "Trot", "Burger", "turtle", "thunder"
        }, uut.get().toArray());
    }

    @Test
    public void test_always() {
        ListBuilder<String> uut = ListBuilder.create();
        ListBuilder<String> res = uut.always();

        assertSame("Expect value returned by always() is the original builder.", uut, res);
    }

    @Test
    public void test_endMaybe() {
        ListBuilder<String> uut = ListBuilder.create();
        ListBuilder<String> res = uut.endMaybe();

        assertSame("Expect value returned by endMaybe() is the original builder.", uut, res);
    }

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
                .add(ValueBuilder.create("foo"))
                .add(ValueBuilder.create("bar"))
                .add(ValueBuilder.create("baz"));

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
        final BuilderInterface<String>[] builders = new BuilderInterface[] {
                ValueBuilder.create(expected[0]),
                ValueBuilder.create(expected[1]),
                ValueBuilder.create(expected[2])
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
        final List<? extends BuilderInterface<String>> builders = Lists.transform(Arrays.asList(expected), ValueBuilder.<String>ofInstanceFunction());
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

    @Test
    public void testAddAll_Iterator_Builder_T() throws Exception {
        final String[] expected = new String[]{"carrots", "cars", "cauliflower"};
        final List<? extends BuilderInterface<String>> builders = Lists.transform(Arrays.asList(expected), ValueBuilder.<String>ofInstanceFunction());
        ListBuilder<String> uut = ListBuilder.create();
        ListBuilder<String> res = uut.addBuilders(builders.iterator());

        assertSame("Expect value returned by add(T) is the original builder.", uut, res);
        assertArrayEquals("Expected addAll(Iterator<T>) to add the given items to the list.", expected, uut.get().toArray());
    }

    @Test
    public void testApply() {
        final ListBuilder<Integer> uut = ListBuilder.create(Integer.class).add(1).add(7);
        ListBuilder<Integer> res = uut.apply(new Function<ListBuilder<Integer>, Void>() {
            @Nullable
            @Override
            public Void apply(ListBuilder<Integer> input) {
                assertSame("Expected the apply() method to apply the function to the instance on which it is invoked.", uut, input);
                input.add(32);
                return null;
            }
        });

        assertSame("The apply method should return the instance it was invoked on.", uut, res);
        assertArrayEquals("Expected the applied function to change the state of the builder.",
                new Integer[]{1, 7, 32}, uut.get().toArray());
    }

    @Test
    public void testMaybeAdd_true() {
        ListBuilder<Integer> uut = ListBuilder.create(Integer.class).add(1).add(7);
        ListBuilder<Integer> res = uut.maybeAdd(-5, true);

        assertSame("The maybeAdd method should return the instance it was invoked on.", uut, res);
        assertArrayEquals("Expected the maybeAdd method to change the state of the builder.",
                new Integer[]{1, 7, -5}, uut.get().toArray());
    }

    @Test
    public void testMaybeAdd_Builder_true() {
        ListBuilder<Integer> uut = ListBuilder.create(Integer.class).add(1).add(7);
        ListBuilder<Integer> res = uut.maybeAdd(ValueBuilder.create(5), true);

        assertSame("The maybeAdd method should return the instance it was invoked on.", uut, res);
        assertArrayEquals("Expected the maybeAdd method to change the state of the builder.",
                new Integer[]{1, 7, 5}, uut.get().toArray());
    }

    @Test
    public void testMaybeAdd_false() {
        ListBuilder<Integer> uut = ListBuilder.create(Integer.class).add(1).add(7);
        ListBuilder<Integer> res = uut.maybeAdd(-5, false);
        uut.add(12);

        assertSame("The maybeAdd method should return the instance it was invoked on.", uut, res);
        assertArrayEquals("Expected the maybeAdd method to not change the state of the builder.",
                new Integer[]{1, 7, 12}, uut.get().toArray());
    }

    @Test
    public void testMaybeAdd_Builder_false() {
        ListBuilder<Integer> uut = ListBuilder.create(Integer.class).add(15).add(-5);
        ListBuilder<Integer> res = uut.maybeAdd(ValueBuilder.create(8), false);
        uut.add(32);

        assertSame("The maybeAdd method should return the instance it was invoked on.", uut, res);
        assertArrayEquals("Expected the maybeAdd method to not change the state of the builder.",
                new Integer[]{15, -5, 32}, uut.get().toArray());
    }

    @Test
    public void testNeverListBuilder_apply() {
        ListBuilder<Integer> uut = ListBuilder.create(Integer.class).add(3).add(1);
        InvokeCountingFunction<ListBuilder<Integer>, Void> func = new InvokeCountingFunction<ListBuilder<Integer>, Void>() {
            @Nullable
            @Override
            public Void reallyApply(ListBuilder<Integer> input) {
                input.add(5);
                return null;
            }
        };
        uut.maybe(false).apply(func);
        ListBuilder<Integer> res = uut.always();
        res.add(4);

        assertSame("The never builder should have returned the original instance from the always method.", uut, res);
        assertEquals("Expected the apply method on the never builder not to be called.", 0, func.getCount());
        assertArrayEquals("Expected the apply method on the never builder to not change the state of the builder.",
                new Integer[]{3, 1, 4}, uut.get().toArray());

    }

    @Test
    public void testNeverListBuilder_get() {
        ListBuilder<Integer> uut = ListBuilder.create(Integer.class).add(7).add(0).maybe(false);
        uut.add(7).add(14);

        assertArrayEquals("Expected NeverListBuilder to return the always builder's list from get().",
                new Integer[]{7, 0}, uut.get().toArray());
    }

    @Test
    public void testNeverListBuilder_addAll_Iterable() {
        ListBuilder<Integer> uut = ListBuilder.create(Integer.class).add(6).add(8);
        uut.maybe(false).addAll(ImmutableList.of(7, 5, 3, 0));
        uut.add(9);

        assertArrayEquals("Expected the never builder's addAll method to not add any of the elements with an iterable.",
                new Integer[]{6, 8, 9}, uut.get().toArray());
    }

    @Test
    public void testNeverListBuilder_addAll_Iterator() {
        ListBuilder<Integer> uut = ListBuilder.create(Integer.class).add(6).add(8);
        uut.maybe(false).addAll(ImmutableList.of(7, 5, 3, 0).iterator());
        uut.add(9);

        assertArrayEquals("Expected the never builder's addAll method to not add any of the elements with an iterator.",
                new Integer[]{6, 8, 9}, uut.get().toArray());
    }

    @Test
    public void testNeverListBuilder_addAll_array() {
        ListBuilder<Integer> uut = ListBuilder.create(Integer.class).add(6).add(8);
        uut.maybe(false).addAll(new Integer[] {7, 5, 3, 0, 9});
        uut.add(9);

        assertArrayEquals("Expected the never builder's addAll method to not add any of the elements with an array.",
                new Integer[]{6, 8, 9}, uut.get().toArray());
    }

    @Test
    public void testNeverListBuilder_addBuilders_Iterable() {
        ListBuilder<Integer> uut = ListBuilder.create(Integer.class).add(6).add(8);
        uut.maybe(false).addBuilders(Lists.transform(ImmutableList.of(7, 5, 3, 0), ValueBuilder.<Integer>ofInstanceFunction()));
        uut.add(9);

        assertArrayEquals("Expected the never builder's addBuilders method to not add any of the elements with an iterable of builders.",
                new Integer[]{6, 8, 9}, uut.get().toArray());
    }

    @Test
    public void testNeverListBuilder_addBuilders_Iterator() {
        ListBuilder<Integer> uut = ListBuilder.create(Integer.class).add(6).add(8);
        uut.maybe(false).addBuilders(Lists.transform(ImmutableList.of(7, 5, 3, 0), ValueBuilder.<Integer>ofInstanceFunction()).iterator());
        uut.add(9);

        assertArrayEquals("Expected the never builder's addBuilders method to not add any of the elements with an iterator of Builders.",
                new Integer[]{6, 8, 9}, uut.get().toArray());
    }

    @Test
    public void testNeverListBuilder_addBuilders_array() {
        ListBuilder<Integer> uut = ListBuilder.create(Integer.class).add(6).add(8);
        @SuppressWarnings("unchecked")
        BuilderInterface<Integer>[] builders = Lists.transform(ImmutableList.of(7, 5, 3, 0), ValueBuilder.<Integer>ofInstanceFunction()).toArray(new BuilderInterface[4]);
        uut.maybe(false).addBuilders(builders);
        uut.add(9);

        assertArrayEquals("Expected the never builder's addBuilders method to not add any of the elements with an array of Builders.",
                new Integer[]{6, 8, 9}, uut.get().toArray());
    }

    @Test
    public void testNeverListBuilder_maybeAdd_true() {
        ListBuilder<Integer> uut = ListBuilder.create(Integer.class).add(5).add(7);
        uut.maybe(false).maybeAdd(9, true);

        assertArrayEquals("Expected the never builder's maybeAdd method to not add the elements regardless of the boolean passed in.",
                new Integer[]{5, 7}, uut.get().toArray());
    }

    @Test
    public void testNeverListBuilder_maybeAdd_false() {
        ListBuilder<Integer> uut = ListBuilder.create(Integer.class).add(5).add(7);
        uut.maybe(false).maybeAdd(9, false);

        assertArrayEquals("Expected the never builder's maybeAdd method to not add the elements regardless of the boolean passed in.",
                new Integer[]{5, 7}, uut.get().toArray());
    }

}