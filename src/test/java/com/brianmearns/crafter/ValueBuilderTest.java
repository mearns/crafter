package com.brianmearns.crafter;

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
        ValueBuilder<String> builder = ValueBuilder.ofInstance("Foobartle");
        ValueBuilder<String> uut = ValueBuilder.ofBuilder(builder);

        assertEquals("Expected built value to be the value that the builder passed into contructor builds.", "Foobartle", uut.get());
    }

    @Test
    public void testConstructor_Builder_changeBuilderAfter() {
        ValueBuilder<String> builder = ValueBuilder.ofInstance("Foobartle");
        ValueBuilder<String> uut = ValueBuilder.ofBuilder(builder);

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
        ValueBuilder<Integer> uut = ValueBuilder.ofInstance(1);
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
        ValueBuilder<Integer> uut = ValueBuilder.ofInstance(5);
        ValueBuilder<Integer> res = uut.maybeSet(100, true);

        assertSame("The maybeSet method should return the instance it was invoked on.", uut, res);
        assertEquals("Expected the maybeSet method to change the value for the builder when true is given.", Integer.valueOf(100), uut.get());
    }

    @Test
    public void testMaybeSet_Builder_true() {
        ValueBuilder<Integer> uut = ValueBuilder.ofBuilder(ValueBuilder.ofInstance(12));
        ValueBuilder<Integer> res = uut.maybeSet(121, true);

        assertSame("The maybeSet method should return the instance it was invoked on.", uut, res);
        assertEquals("Expected the maybeSet method to change the value for the builder when true is given.", Integer.valueOf(121), uut.get());
    }

    @Test
    public void testMaybeSet_false() {
        ValueBuilder<Integer> uut = ValueBuilder.ofInstance(5);
        ValueBuilder<Integer> res = uut.maybeSet(100, false);

        assertSame("The maybeSet method should return the instance it was invoked on.", uut, res);
        assertEquals("Expected the maybeSet method to not change the value for the builder when false is given.", Integer.valueOf(5), uut.get());
    }

    @Test
    public void testMaybeSet_Builder_false() {
        ValueBuilder<Integer> uut = ValueBuilder.ofBuilder(ValueBuilder.ofInstance(12));
        ValueBuilder<Integer> res = uut.maybeSet(121, false);

        assertSame("The maybeSet method should return the instance it was invoked on.", uut, res);
        assertEquals("Expected the maybeSet method to not change the value for the builder when false is given.", Integer.valueOf(12), uut.get());
    }

}