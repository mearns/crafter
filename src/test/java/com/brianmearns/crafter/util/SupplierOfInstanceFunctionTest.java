package com.brianmearns.crafter.util;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class SupplierOfInstanceFunctionTest {

    @Test
    public void testApply() throws Exception {
        Function<String, Supplier<String>> uut = new SupplierFunctions.SupplierOfInstanceFunction<>();

        String testVal = "Test value";
        Supplier<String> supplier = uut.apply(testVal);
        assertNotNull("Function should not return null", supplier);
        assertSame("Expected function to return a supplier of the same instance as given.", testVal, supplier.get());
    }

    @Test
    public void testApply_null() throws Exception {
        Function<String, Supplier<String>> uut = new SupplierFunctions.SupplierOfInstanceFunction<>();

        Supplier<String> supplier = uut.apply(null);
        assertNotNull("Function should not return null", supplier);
        assertNull("Expected function to return a supplier of null when given null.", supplier.get());
    }
}