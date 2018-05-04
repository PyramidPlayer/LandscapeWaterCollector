package ru.toroptsev;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    public static void main(String[] args) {
        if (args.length == 0) {
            log.error("No arguments provided");
            return;
        }
        int[] landscape = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            try {
                landscape[i] = Integer.parseInt(args[i]);
            } catch (NumberFormatException e) {
                log.error("Invalid height: {}", args[i]);
                return;
            }
        }
        WaterCollector collector = new WaterCollector(landscape);
        collector.draw();
        log.info("Collected {} units of water", collector.calc());
    }
}
