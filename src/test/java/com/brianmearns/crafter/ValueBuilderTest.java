package com.brianmearns.crafter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

}