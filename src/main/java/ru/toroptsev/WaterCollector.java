package ru.toroptsev;

import lombok.extern.slf4j.Slf4j;

/**
 * Calculator that counts the water collected after the rain in the landscape pits
 */
@Slf4j
public class WaterCollector {

    private static final int MAX_HEIGHT = 32000;
    private static final int MAX_POSITIONS = 32000;

    private int[] landscape;

    /**
     * Constructs collector and validate input
     * @param landscape - an array of heights
     * @throws IllegalArgumentException when input violate the constraints:
     *  1. Input array shouldn't be null or empty
     *  2. Max array length is 32000
     *  3. Each array element should be in the range [0, 32000]
     */
    public WaterCollector(int[] landscape) {
        if (landscape == null || landscape.length == 0 || landscape.length > MAX_POSITIONS) {
            throw new IllegalArgumentException("Invalid landscape array");
        }
        for (int i : landscape) {
            if (i < 0 || i > MAX_HEIGHT) {
                throw new IllegalArgumentException("Invalid height value: " + i);
            }
        }
        this.landscape = landscape;
    }

    /**
     * Calculates water collected in the pits
     * @return number of water units
     */
    public int calc() {
        if (landscape.length < 3) {
            return 0;
        }
        Integer firstHill = getFirstHill();
        if (firstHill == null) { 
            log.info("Flat or always increasing landscape");
            return 0;
        }
        log.debug("First hill: {}", firstHill);

        Integer lastHill = getLastHill();
        if (lastHill == null) {
            log.info("Always decreasing landscape");
            return 0;
        }
        log.debug("Last hill: " + lastHill);
        if (firstHill.equals(lastHill)) {
            log.info("Single hill");
            return 0;
        }

        return splitAndCalc(firstHill, lastHill);
    }

    /**
     * Logs landscape in ASCII
     */
    public void draw() {
        int max = landscape[getFirstMaxHill(0, landscape.length - 1)];
        for (int i = max; i >0; i--) {
            StringBuilder sb = new StringBuilder();
            for (int j : landscape) {
                sb.append(j >= i ? "^ " : "  ");
            }
            log.info(sb.toString());
        }
        StringBuilder sb = new StringBuilder();
        for (int i : landscape) {
            sb.append(i).append(" ");
        }
        log.info(repeatedSymbol('-', sb.length() - 1));
        log.info(sb.toString());
    }

    private int splitAndCalc(final int start, final int end) {
        if (end - start <= 1) {
            log.debug("Interval [{}, {}] is too small", start, end);
            return 0;
        }

        int firstMaxHill = getFirstMaxHill(start, end);
        log.debug("First max hill: {}", firstMaxHill);
        if (firstMaxHill == start) {
            log.debug("First max hill is start. Check from next position as start");
            int nextMaxHill = getFirstMaxHill(start + 1, end);
            log.debug("Next max hill: " + nextMaxHill);
            if (nextMaxHill == end) {
                log.debug("Next max hill is end. Collect water within [{}, {}]", start, end);
                return collect(start, end);
            }
            if (nextMaxHill == start + 1) {
                log.debug("Skip first position and consider [{}, {}]", start + 1, end);
                return splitAndCalc(start + 1, end);
            } else {
                firstMaxHill = nextMaxHill;
            }
        }

        if (firstMaxHill == end) {
            log.debug("First max hill is end. Check previous position as end");
            int previousMaxHill = getFirstMaxHill(start, end - 1);
            log.debug("Previous max hill: {}", previousMaxHill);
            if (previousMaxHill == start) {
                log.debug("Previous max hill is start. Collect water within [{}, {}]", start, end);
                return collect(start, end);
            }
            if (previousMaxHill == end - 1) {
                log.debug("Skip last position and consider [{}, {}]", start, end - 1);
                return splitAndCalc(start, end - 1);
            } else {
                firstMaxHill = previousMaxHill;
            }
        }

        int collected = 0;

        log.debug("Calc left [{}, {}] -> [{}, {}]", start, end, start, firstMaxHill);
        collected += splitAndCalc(start, firstMaxHill);

        log.debug("Calc right [{}, {}] -> [{}, {}]", start, end, firstMaxHill, end);
        collected += splitAndCalc(firstMaxHill, end);

        return collected;
    }

    private Integer getFirstHill() {
        for (int i = 0; i < landscape.length - 1; i++) {
            if (landscape[i + 1] < landscape[i]) {
                return i;
            }
        }
        return null;
    }

    private Integer getLastHill() {
        for (int i = landscape.length - 1; i > 0; i--) {
            if (landscape[i - 1] < landscape[i]) {
                return i;
            }
        }
        return null;
    }

    private int getFirstMaxHill(final int start, final int end) {
        int max = 0;
        int maxIndex = start;
        for (int i = start; i <= end; i++) {
            if (landscape[i] > max) {
                max = landscape[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private int collect(final int start, final int end) {
        int height = Math.min(landscape[start], landscape[end]);
        int collected = 0;
        for (int i = start + 1; i < end; i++) {
            if (landscape[i] < height) {
                int waterHeight = height - landscape[i];
                log.debug("Collected at {}: {}", i, waterHeight);
                collected += waterHeight;
                log.debug("Total collected: {}", collected);
            }
        }
        log.debug("Collected in [{}, {}]: {}", start, end, collected);
        return collected;
    }

    private String repeatedSymbol(char symbol, int n) {
        return new String(new char[n]).replace('\0', symbol);
    }
}
