package com.udacity.examples.Testing;

import org.junit.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;

public class HelperTest {
    @Test
    public void verify_getCount() {
        List<String> empNames = Arrays.asList("test1", "test2");
        final long actual = Helper.getCount(empNames);
        assertEquals(2, actual);
    }

    @Test
    public void verify_getStats() {
        List<Integer> yrsOfExperience = Arrays.asList(13,4,15,6,17,8,19,1,2,3);
        List<Integer> expectedList = Arrays.asList(13,4,15,6,17,8,19,1,2,3);
        IntSummaryStatistics stats = Helper.getStats(yrsOfExperience);
        assertEquals(19, stats.getMax());
        // comparable
        assertEquals(expectedList, yrsOfExperience);
    }

//    @Ignore
    @Test
    public void compare_arrays() {
        int[] yrs = {10, 14, 2};
        int[] expected_yrs = {10, 14, 2};
        assertArrayEquals(expected_yrs, yrs);
    }

    @Before
    public void init() {
        System.out.println("runs before each method");
    }

    @BeforeClass
    public static void setup() {
        System.out.println("runs before each class");
    }

    @After
    public void initEnd() {
        System.out.println("runs after each method");
    }

    @AfterClass
    public static void teardown() {
        System.out.println("runs after each class");
    }
}
