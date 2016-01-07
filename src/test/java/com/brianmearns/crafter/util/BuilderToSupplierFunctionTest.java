package com.brianmearns.crafter.util;

import com.brianmearns.crafter.Builder;
import com.brianmearns.crafter.ValueBuilder;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 *
 */
public class BuilderToSupplierFunctionTest {

    @Test
    public void testApply() throws Exception {
        Function<Builder<String>, Supplier<String>> uut = new SupplierFunctions.BuilderToSupplierFunction<>();

        Builder<String> builder = ValueBuilder.create("test value");
        Supplier<String> supplier = uut.apply(builder);
        assertNotNull("Function should not return null", supplier);
        assertSame("Expected function to return the same instance as given.", builder, supplier);
        assertEquals("Expected function to leave builder unmodified so it returns the same value.", "test value", builder.get());
    }

    @Test
    public void testApply_null() throws Exception {
        Function<Builder<String>, Supplier<String>> uut = new SupplierFunctions.BuilderToSupplierFunction<>();

        Supplier<String> supplier = uut.apply(null);
        assertNull("Function should return null when given null", supplier);
    }
}