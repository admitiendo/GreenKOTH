package net.multylands.koth.utils.player;

import com.google.common.base.Strings;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

import java.io.*;
import java.net.*;

public class PlayerUtil {

    public String getPing(Player player) {
        try {
            String a = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> b = Class.forName("org.bukkit.craftbukkit." + a + ".entity.CraftPlayer");
            Object c = b.getMethod("getHandle").invoke(player);
            int ping = (int) c.getClass().getDeclaredField("ping").get(c);
            String finalPing = "";
            if (ping > 500) {
                finalPing = ChatColor.RED.toString() + "" + ping;
            } else if (ping > 300) {
                finalPing = ChatColor.YELLOW.toString() + "" + ping;
            } else if (ping < 300) {
                finalPing = ChatColor.GREEN.toString() + "" + ping;
            }
            return finalPing;
        } catch (Exception e) {
            return "0";
        }
    }

    public String getHealthNice(Player player) {
        return getProgressBar((int) Math.round(player.getHealth()), 20, 10, "\u2764", ChatColor.RED, ChatColor.GRAY);
    }

    public String getProgressBar(int current, int max, int totalBars, String symbol, ChatColor completedColor,
                                 ChatColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return Strings.repeat("" + completedColor + symbol, progressBars)
                + Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }

    public String getCountry(String IP) {
        URL url;
        BufferedReader in;
        String country = "";

        try {
            url = new URL("http://ip-api.com/json/" + IP + "?fields=country");
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            country = in.readLine().trim();
            if (country.length() <= 0)
                try {
                    InetAddress ip = InetAddress.getLocalHost();
                    System.out.println(ip.getHostAddress().trim());
                    country = ip.getHostAddress().trim();
                } catch (Exception exp) {
                    country = "Not Found";
                }
        } catch (Exception ignored) {
        }
        return country
                .replace("{", "")
                .replace("}", "")
                .replace("\"\"", "")
                .replace(":", "");
    }

    public String getIP(Player player) {
        return player.getAddress().getAddress().toString();
    }



    public boolean isInventoryFull(Player player) {
        return player.getInventory().firstEmpty() < 0;
    }

    public void decrement(Player player, ItemStack itemStack, boolean sound, boolean cursor) {
        if (sound) player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1F, 1F);

        if (itemStack.getAmount() <= 1) {
            if (cursor) player.setItemOnCursor(null);
            else player.setItemInHand(null);
        } else itemStack.setAmount(itemStack.getAmount() - 1);

        player.updateInventory();
    }
}
