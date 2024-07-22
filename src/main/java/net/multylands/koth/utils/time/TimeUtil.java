package net.multylands.koth.utils.time;

import java.util.concurrent.TimeUnit;

public class TimeUtil {

    public static String hhmmssFromMillis(long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    public static String formatTimeMillis(long millis) {
        long seconds = millis / 1000L;

        if (seconds < 1) {
            return "0 seconds";
        }

        long minutes = seconds / 60;
        seconds = seconds % 60;

        long hours = minutes / 60;
        minutes = minutes % 60;
        long day = hours / 24;
        hours = hours % 24;
        long years = day / 365;
        day = day % 365;

        StringBuilder time = new StringBuilder();

        if (years != 0) {
            time.append(years).append("y ");
        }

        if (day != 0) {
            time.append(day).append("d ");
        }

        if (hours != 0) {
            time.append(hours).append("h ");
        }

        if (minutes != 0) {
            time.append(minutes).append("m ");
        }

        if (seconds != 0) {
            time.append(seconds).append("s ");
        }

        return time.toString().trim();
    }
}