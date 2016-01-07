package com.brianmearns.crafter;

import com.brianmearns.crafter.util.InvokeCountingFunction;
import com.google.common.base.Function;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class ValueBuilderTest {

    @Test
    public void testConstructor_Builder() {
        ValueBuilder<String> builder = ValueBuilder.create("Foobartle");
        ValueBuilder<String> uut = ValueBuilder.create(builder);

        assertEquals("Expected built value to be the value that the builder passed into contructor builds.", "Foobartle", uut.get());
    }

    @Test
    public void testConstructor_Builder_changeBuilderAfter() {
        ValueBuilder<String> builder = ValueBuilder.create("Foobartle");
        ValueBuilder<String> uut = ValueBuilder.create(builder);

        builder.set("This is my real value.");

        assertEquals("Expected changing the value of the source builder would be reflected in outer builder.", "This is my real value.", uut.get());
    }

    @Test
    public void testOfBuilderFunction() {
        Function<Builder<String>, ValueBuilder<String>> uut = ValueBuilder.ofBuilderFunction();
        assertNotNull(uut);

        Builder<String> builder = new Builder<String>() {
            @Override
            public String get() {
                return "Mwa ha ha!";
            }
        };
        ValueBuilder<String> result = uut.apply(builder);
        assertNotNull("Function should not return null", result);
        assertEquals("Returned ValueBuilder should build the value produced by the applied builder.", "Mwa ha ha!", result.get());
    }

    @Test
    public void testOfInstanceFunction() {
        Function<String, ValueBuilder<String>> uut = ValueBuilder.ofInstanceFunction();
        assertNotNull(uut);

        String testValue = "I hope this comes out.";
        ValueBuilder<String> result = uut.apply(testValue);
        assertNotNull("Function should not return null", result);
        assertSame("Returned ValueBuilder should build the value given.", testValue, result.get());
    }

    @Test
    public void testApply() {
        ValueBuilder<Integer> uut = ValueBuilder.create(1);
        ValueBuilder<Integer> res = uut.apply(new Function<ValueBuilder<Integer>, Void>() {
            @Nullable
            @Override
            public Void apply(ValueBuilder<Integer> input) {
                input.set(3);
                return null;
            }
        });

        assertSame("The apply method should return the instance it was invoked on.", uut, res);
        assertEquals("Expected the applied function to change the state of the builder.", Integer.valueOf(3), uut.get());
    }

    @Test
    public void testMaybeSet_true() {
        ValueBuilder<Integer> uut = ValueBuilder.create(5);
        ValueBuilder<Integer> res = uut.maybeSet(100, true);

        assertSame("The maybeSet method should return the instance it was invoked on.", uut, res);
        assertEquals("Expected the maybeSet method to change the value for the builder when true is given.", Integer.valueOf(100), uut.get());
    }

    @Test
    public void testMaybeSet_Builder_true() {
        ValueBuilder<Integer> uut = ValueBuilder.create(ValueBuilder.create(12));
        ValueBuilder<Integer> res = uut.maybeSet(121, true);

        assertSame("The maybeSet method should return the instance it was invoked on.", uut, res);
        assertEquals("Expected the maybeSet method to change the value for the builder when true is given.", Integer.valueOf(121), uut.get());
    }

    @Test
    public void testMaybeSet_false() {
        ValueBuilder<Integer> uut = ValueBuilder.create(5);
        ValueBuilder<Integer> res = uut.maybeSet(100, false);

        assertSame("The maybeSet method should return the instance it was invoked on.", uut, res);
        assertEquals("Expected the maybeSet method to not change the value for the builder when false is given.", Integer.valueOf(5), uut.get());
    }

    @Test
    public void testMaybeSet_Builder_false() {
        ValueBuilder<Integer> uut = ValueBuilder.create(ValueBuilder.create(12));
        ValueBuilder<Integer> res = uut.maybeSet(121, false);

        assertSame("The maybeSet method should return the instance it was invoked on.", uut, res);
        assertEquals("Expected the maybeSet method to not change the value for the builder when false is given.", Integer.valueOf(12), uut.get());
    }

    @Test
    public void test_always() {
        ValueBuilder<Integer> uut = ValueBuilder.create(5);
        ValueBuilder<Integer> res = uut.always();

        assertSame("The always method should return the instance it was invoked on.", uut, res);
    }

    @Test
    public void test_endMaybe() {
        ValueBuilder<Integer> uut = ValueBuilder.create(5);
        ValueBuilder<Integer> res = uut.endMaybe();

        assertSame("The always method should return the instance it was invoked on.", uut, res);
    }

    @Test
    public void testMaybe_true() {
        ValueBuilder<Integer> uut = ValueBuilder.create(5);
        ValueBuilder<Integer> res = uut.maybe(true);

        assertSame("The maybe method should return the instance it was invoked on.", uut, res);
    }

    @Test
    public void testMaybe_false_set() {
        ValueBuilder<Integer> orig = ValueBuilder.create(5);
        ValueBuilder<Integer> uut = orig.maybe(false);
        ValueBuilder<Integer> res = uut.set(81);

        assertSame("Expected never builder to return itself from set method.", uut, res);
        assertEquals("Expected the set method to not change the state of the builder after a maybe(false).", Integer.valueOf(5), uut.get());
    }

    @Test
    public void testMaybe_false_maybeSet_true() {
        ValueBuilder<Integer> orig = ValueBuilder.create(5);
        ValueBuilder<Integer> uut = orig.maybe(false);
        ValueBuilder<Integer> res = uut.maybeSet(81, true);

        assertSame("Expected never builder to return itself from maybeSet method.", uut, res);
        assertEquals("Expected the maybeSet method to not change the state of the builder after a maybe(false).", Integer.valueOf(5), uut.get());
    }

    @Test
    public void testMaybe_false_maybeSet_false() {
        ValueBuilder<Integer> orig = ValueBuilder.create(5);
        ValueBuilder<Integer> uut = orig.maybe(false);
        ValueBuilder<Integer> res = uut.maybeSet(81, false);

        assertSame("Expected never builder to return itself from maybeSet method.", uut, res);
        assertEquals("Expected the maybeSet method to not change the state of the builder after a maybe(false).", Integer.valueOf(5), uut.get());
    }

    @Test
    public void testMaybe_false_get() {
        ValueBuilder<Integer> orig = ValueBuilder.create(5);
        ValueBuilder<Integer> uut = orig.maybe(false);
        orig.set(15);

        assertEquals("Expected never builder to delegate to always builder on get().", Integer.valueOf(15), uut.get());
    }

    @Test
    public void testMaybe_false_always() {
        ValueBuilder<Integer> orig = ValueBuilder.create(5);
        ValueBuilder<Integer> uut = orig.maybe(false);
        ValueBuilder<Integer> res = uut.always();

        assertSame("Expected never builder to return the always builder from always().", orig, res);
    }

    @Test
    public void testMaybe_false_maybe_true() {
        ValueBuilder<Integer> orig = ValueBuilder.create(5);
        ValueBuilder<Integer> uut = orig.maybe(false).maybe(true);
        uut.set(51);


        assertEquals("Expected never builder to return another never builder from maybe(true).", Integer.valueOf(5), uut.get());
    }

    @Test
    public void testMaybe_false_endMaybe() {
        ValueBuilder<Integer> uut = ValueBuilder.create(5);
        ValueBuilder<Integer> res = uut.maybe(false).endMaybe();

        assertSame("Expected never builder to return parent from endMaybe()", uut, res);
    }

    @Test
    public void testMaybe_false_apply() {
        ValueBuilder<Integer> orig = ValueBuilder.create(5);
        ValueBuilder<Integer> uut = orig.maybe(false);
        InvokeCountingFunction<ValueBuilder<Integer>, Void> func = InvokeCountingFunction.reallyDoNothing();
        ValueBuilder<Integer> res = uut.apply(func);

        assertEquals("Expected function to not be called by never builder's apply method.", 0, func.getCount());
        assertSame("Expected never builder's apply method to return the instance it was invoked on.", uut, res);
    }

    @Test
    public void test_nested_neverBuilders() {
        ValueBuilder<Integer> uut = ValueBuilder.create(5);
        ValueBuilder<Integer> res = uut.maybe(false).maybe(false).maybe(false).endMaybe().endMaybe().endMaybe();

        assertSame("Expected never builder correctly nest and un-nest with endMaybe", uut, res);
    }

    @Test(expected = IncompleteBuilderException.class)
    public void test_create() {
        ValueBuilder.create().get();
    }

    @Test(expected = IncompleteBuilderException.class)
    public void test_create_Class() {
        ValueBuilder.create(Integer.class).get();
    }

}