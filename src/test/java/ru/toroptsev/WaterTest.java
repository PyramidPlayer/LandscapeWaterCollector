package ru.toroptsev;

import org.junit.Assert;
import org.junit.Test;

public class WaterTest {

    @Test
    public void testNullLandscape() {
        try {
            new WaterCollector(null);
            Assert.fail("Nullpointer validation doesn't work");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testZeroPositionsCount() {
        int[] landscape = new int[]{};
        try {
            new WaterCollector(landscape);
            Assert.fail("Zero positions count validation doesn't work");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testExceedingPositionsCount() {
        int[] landscape = new int[32001];
        try {
            new WaterCollector(landscape);
            Assert.fail("Maximum positions count validation doesn't work");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testNegativeHeight() {
        int[] landscape = new int[]{-1, 4, 9, 3, 0};
        try {
            new WaterCollector(landscape);
            Assert.fail("Negative height validation doesn't work");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testExceedingHeight() {
        int[] landscape = new int[]{34, 4234, 32001, 1, 0};
        try {
            new WaterCollector(landscape);
            Assert.fail("Maximum height validation doesn't work");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testMiminalIncreasing() {
        int[] landscape = new int[]{5, 6};
        WaterCollector collector = new WaterCollector(landscape);
        Assert.assertEquals(0, collector.calc());
        collector.draw();
    }

    @Test
    public void testMiminalDecreasing() {
        int[] landscape = new int[]{7, 0};
        WaterCollector collector = new WaterCollector(landscape);
        Assert.assertEquals(0, collector.calc());
        collector.draw();
    }

    @Test
    public void testMiminalFlat() {
        int[] landscape = new int[]{10, 10};
        WaterCollector collector = new WaterCollector(landscape);
        Assert.assertEquals(0, collector.calc());
        collector.draw();
    }

    @Test
    public void testIncreasing() {
        int[] landscape = new int[]{0, 1, 2, 3, 4, 5, 5, 7, 8, 9, 10};
        WaterCollector collector = new WaterCollector(landscape);
        Assert.assertEquals(0, collector.calc());
        collector.draw();
    }

    @Test
    public void testDecreasing() {
        int[] landscape = new int[]{10, 9, 8, 7, 6, 5, 5, 3, 2, 1, 0};
        WaterCollector collector = new WaterCollector(landscape);
        Assert.assertEquals(0, collector.calc());
        collector.draw();
    }

    @Test
    public void testFlat() {
        int[] landscape = new int[]{5, 5, 5, 5, 5};
        WaterCollector collector = new WaterCollector(landscape);
        Assert.assertEquals(0, collector.calc());
        collector.draw();
    }

    @Test
    public void testOneHill() {
        int[] landscape = new int[]{0, 1, 2, 3, 4, 5, 4, 3, 2, 1, 0};
        WaterCollector collector = new WaterCollector(landscape);
        Assert.assertEquals(0, collector.calc());
        collector.draw();
    }

    @Test
    public void testMultipleEqualHills() {
        int[] landscape = new int[]{1, 3, 8, 7, 7, 4, 0, 0, 3, 7, 6, 5, 3, 2, 8, 1, 2, 5, 1, 8};
        WaterCollector collector = new WaterCollector(landscape);
        Assert.assertEquals(67, collector.calc());
        collector.draw();
    }

    @Test
    public void testMultipleHills() {
        int[] landscape = new int[]{1, 3, 7, 6, 8, 4, 0, 0, 3, 12, 6, 5, 3, 2, 9, 1, 2, 5, 1, 8};
        WaterCollector collector = new WaterCollector(landscape);
        Assert.assertEquals(69, collector.calc());
        collector.draw();
    }

    @Test
    public void testMaxCapacity() {
        int[] landscape = new int[32000];
        landscape[0] = 32000;
        landscape[31999] = 32000;
        WaterCollector collector = new WaterCollector(landscape);
        Assert.assertEquals(1023936000, collector.calc());
    }
}
