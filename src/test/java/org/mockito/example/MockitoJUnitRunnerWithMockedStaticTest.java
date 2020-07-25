package org.mockito.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test class that uses the {@link Mock} annotation on a field with type {@link MockedStatic}. This
 * class uses the {@link MockitoJUnitRunner} to create the mocks. It's intended to show that the
 * test immediately after a failing test will fail with a NPE because the mocks are not
 * re-initialized.
 */
@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // using this to force the "fail" test occurs first
public class MockitoJUnitRunnerWithMockedStaticTest
{
    @Mock
    private Supplier<String> mockSupplier;
    @Mock
    private MockedStatic<Validator> mockStaticValidator;

    /**
     * Intentional failing test to show the test immediately after this fails due to NPE because the
     * mocks are not re-initialized.
     */
    @Test
    public void testName1()
    {
        fail("intentional failure");
    }

    /**
     * Fails with NPE.
     */
    @Test
    public void testName2()
    {
        Function<Supplier<String>, String> trim = (supplier) -> {
            String value = supplier.get();
            return value == null ? "" : value.trim();
        };

        when(mockSupplier.get()).thenReturn(null); // NPE occurs when calling mockSupplier.get()
        assertEquals("", trim.apply(mockSupplier));
    }

    /**
     * This test ends up failing because of "org.mockito.exceptions.misusing.NotAMockException:
     * Argument passed to Mockito.mockingDetails() should be a mock, but is an instance of class
     * java.lang.Class!.".
     * <p>
     * Note: The only reason this even went down that path is because the previous test's NPE
     * failure was leaked to this test inside of the listener in DefaultInternalRunner.
     */
    @Test
    public void testName3()
    {
        Function<Supplier<String>, String> trim = (supplier) -> {
            String value = supplier.get();
            return value == null ? "" : value.trim();
        };

        when(mockSupplier.get()).thenReturn("");
        assertEquals("", trim.apply(mockSupplier));
    }

    /**
     * Fails due to NPE again.
     */
    @Test
    public void testName4()
    {
        Function<Supplier<String>, String> trim = (supplier) -> {
            String value = supplier.get();
            return value == null ? "" : value.trim();
        };

        when(mockSupplier.get()).thenReturn(" ");
        assertEquals("", trim.apply(mockSupplier));
    }

    private static class Validator
    {
        public static void validatePositive(int value)
        {
            // do nothing
        }
    }
}