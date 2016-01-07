package com.brianmearns.crafter;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class IncompleteBuilderExceptionTest {

    @Test
    public void test_constructor_string_throwable() {
        final String message = "this is my message";
        final Throwable cause = new Throwable();
        final IncompleteBuilderException uut = new IncompleteBuilderException(message, cause);

        assertEquals("Expected message to be as specified in constructor.", message, uut.getMessage());
        assertSame("Expected cause to be as specified in constructor.", cause, uut.getCause());
    }

    @Test
    public void test_constructor_string() {
        final String message = "this is my message";
        final IncompleteBuilderException uut = new IncompleteBuilderException(message);

        assertEquals("Expected message to be as specified in constructor.", message, uut.getMessage());
        assertNull("Expected cause to be null for String constructor.", uut.getCause());
    }

    @Test
    public void test_constructor_throwable() {
        final String message = "this is my ";
        final Throwable cause = new Throwable();
        final IncompleteBuilderException uut = new IncompleteBuilderException(cause);

        assertSame("Expected cause to be as specified in constructor.", cause, uut.getCause());
    }

    @Test
    public void test_constructor() {
        final IncompleteBuilderException uut = new IncompleteBuilderException();

        assertNull("Expected cause to be null for no-arg consutrctor.", uut.getCause());
        assertNull("Expected message to be null for no-arg consutrctor.", uut.getMessage());
    }

}