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
        ValueBuilder<String> builder = new ValueBuilder<>("Foobartle");
        ValueBuilder<String> uut = new ValueBuilder<>(builder);

        assertEquals("Expected built value to be the value that the builder passed into contructor builds.", "Foobartle", uut.get());
    }

    @Test
    public void testConstructor_Builder_changeBuilderAfter() {
        ValueBuilder<String> builder = new ValueBuilder<>("Foobartle");
        ValueBuilder<String> uut = new ValueBuilder<>(builder);

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
        ValueBuilder<Integer> uut = new ValueBuilder<>(1);
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

}