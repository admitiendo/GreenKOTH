package net.multylands.koth.scheduler;

import net.multylands.koth.GreenKOTH;
import org.bukkit.configuration.file.FileConfiguration;

public class KothSchedule {
    private final String kothId;
    private final long start;


    public KothSchedule(String kothId, long millisStart){
        this.kothId = kothId;
        this.start = millisStart;

    }

    public long getStartMillis() {
        return start;
    }

    public String getFormattedTimeLeft(){

        long difference = getStartMillis()-System.currentTimeMillis();

        long seconds = difference / 1000 % 60;
        long minutes = difference / (60 * 1000) % 60;
        long hours = difference / (60 * 60 * 1000) % 24;
        long days = difference / (24 * 60 * 60 * 1000);

        StringBuilder builder = new StringBuilder();

        FileConfiguration config = GreenKOTH.get().getConfig();

        if(days > 0) builder.append(days).append(days == 1 ? config.getString("next-koth-time-format.day") : config.getString("next-koth-time-format.days"));
        if(hours > 0) builder.append(hours).append(hours == 1 ? config.getString("next-koth-time-format.hour") : config.getString("next-koth-time-format.hours"));
        if(minutes > 0) builder.append(minutes).append(minutes == 1 ? config.getString("next-koth-time-format.minute") : config.getString("next-koth-time-format.minutes"));
        if(seconds > 0) builder.append(seconds).append(seconds == 1 ? config.getString("next-koth-time-format.second") : config.getString("next-koth-time-format.seconds"));

        return builder.toString();

    }
}