package com.brianmearns.crafter.util;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 *
 */
public class SupplierFunctionsTest {

    @Test
    public void testSupplierOfInstanceFunction() throws Exception {
        //We already tested the function in SupplierOfInstanceFunctionTest, we just need to make sure they're actually returned by this factory function.
        assertNotNull("Method should return a Function, not null.", SupplierFunctions.supplierOfInstanceFunction());
    }

    @Test
    public void testBuilderToSupplierFunction() throws Exception {
        //We already tested the function in BuilderToSupplierFunctionTest, we just need to make sure they're actually returned by this factory function.
        assertNotNull("Method should return a Function, not null.", SupplierFunctions.builderToSupplierFunction());
    }
}