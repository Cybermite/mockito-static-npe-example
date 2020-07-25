package org.mockito.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

/**
 * Test class that uses the {@link Mock} annotation on a field with type {@link MockedStatic}. This
 * class uses before and after methods to manually close the mocks created by mockito. It's intended
 * to show the comparison with {@link MockitoJUnitRunnerWithMockedStaticTest} where tests fail in
 * that class with a NPE whereas this test class only has the one intended test failure (non-NPE).
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // using this to force the "fail" test occurs first
public class MockitoOpenMocksMockedStaticTest
{
    @Mock
    private Supplier<String> mockSupplier;
    @Mock
    private MockedStatic<Validator> mockStaticValidator;

    private AutoCloseable closeable;

    @Before
    public void before()
    {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @After
    public void after() throws Exception
    {
        if(closeable != null)
        {
            closeable.close();
        }
    }

    @Test
    public void testName1()
    {
        fail("intentional failure");
    }

    @Test
    public void testName2()
    {
        Function<Supplier<String>, String> trim = (supplier) -> {
            String value = supplier.get();
            return value == null ? "" : value.trim();
        };

        when(mockSupplier.get()).thenReturn(null);
        assertEquals("", trim.apply(mockSupplier));
    }

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