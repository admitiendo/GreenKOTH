package net.multylands.koth.object;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.multylands.koth.GreenKOTH;
import net.multylands.koth.object.events.PlayerStartCaptureKothEvent;
import net.multylands.koth.object.events.PlayerStopCaptureKothEvent;
import net.multylands.koth.object.events.PlayerCaptureKothEvent;
import net.multylands.koth.utils.chat.CC;
import net.multylands.koth.utils.time.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Koth {
    public String ID;
    public int capTime;
    public Location corner1;
    public Location corner2;
    public Player king;

    private long autoStartTimer = 0;
    private long autoEndTimer = 0;
    private long capTimer = 0;

    private long lastAutoStartDelay = -1;
    private long lastAutoEndDelay = -1;
    private long lastCapDelay = -1;

    private int autoStartTimerID = -1;
    private int autoEndTimerID = -1;
    private int capTimerID = -1;

    private boolean isBeingCapped = false;
    private Player cappingPlayer;

    public Koth() {

    }

    public Koth(Location corner1, Location corner2, String ID, int capTime) {
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.ID = ID;
        this.capTime = capTime;
    }

    public Koth build() {
        return new Koth(corner1, corner2, getID(), getCapTime());
    }
    public void setCapTime(int capTime) {
        this.capTime = capTime;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getCapTime() {
        return capTime;
    }

    public Location getCorner1() {
        return corner1;
    }

    public Location getCorner2() {
        return corner2;
    }

    public void setCorner1(Location loc) {
        this.corner1 = loc;
    }

    public void setCorner2(Location loc) {
        this.corner2 = loc;
    }

    public void setKing(Player player) {
        this.king = player;
    }

    public void setNoKing() {
        this.king = null;
    }

    public boolean isThereAKing() {
        return !(king == null);
    }

    public Player getKing() {
        return king;
    }

    public String getID() {
        return ID;
    }

    public void start() {
        CC.broadcast("&f&l| &bThe koth " + ID + " has started!");
        CC.broadcast("&f&l| &bLocation: &7(&c"
                + getCenterBlock().getX() + " "
                + getCenterBlock().getY() + " "
                + getCenterBlock().getZ() + "&7)");
        GreenKOTH.kothManager.setActiveKoth(this);
    }

    public void stop(String reason) {
        switch (reason) {
            case "capped": {
                CC.broadcast("&f&l| &bThe koth " + ID + " has been capped!");
                if (getKing() != null) {
                    CC.broadcast("&f&l| &bCapper: " + getKing().getName());
                } else {
                    CC.broadcast("&f&l| &bCapper: &ccouln't resolve capper (Contact developer if the error happens again!)");
                }
                PlayerCaptureKothEvent captureKothEvent = new PlayerCaptureKothEvent(getKing(), this, GreenKOTH.kothManager.getCommands());
                Bukkit.getPluginManager().callEvent(captureKothEvent);

                GreenKOTH.kothManager.setActiveKoth(null);
                stopCaptureTimer(getKing(), false);
                this.setNoKing();
                break;
            }
            case "command": {
                CC.broadcast("&f&l| &bThe koth " + ID + " has been stopped!");
                GreenKOTH.kothManager.setActiveKoth(null);
                this.setNoKing();
                break;
            }
        }
    }

    public void startCaptureTimer(final Player player, boolean broadcast) {
        PlayerStartCaptureKothEvent event = new PlayerStartCaptureKothEvent(player, this);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;


        final int CAPTURE_TIME = getCapTime();
        lastCapDelay = CAPTURE_TIME;

        isBeingCapped = true;
        cappingPlayer = player;
        setKing(player);

        capTimerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(GreenKOTH.get(), new Runnable() {
            public void run() {
                capTimer++;
                long t = (CAPTURE_TIME - capTime);

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new TextComponent(CC.translate("&cKOTH: &f" + getID() + " &7| &c" +  TimeUtil.hhmmssFromMillis(t) + "s remaining &7|" + " &cCap Time: &f" + TimeUtil.hhmmssFromMillis(getCapTime()) + "s")));

                if (capTimer == capTime / 2) {
                    CC.broadcast("&f&l| &b" + player.getName() + " &cis capping koth &f" + getID() + " &7(&C" + TimeUtil.hhmmssFromMillis(t) + "s remaining&7)");
                }
                if (capTimer == capTime / 4) {
                    CC.broadcast("&f&l| &b" + player.getName() + " &cis capping koth &f" + getID() + " &7(&C" + TimeUtil.hhmmssFromMillis(t) + "s remaining&7)");
                }

                if (capTimer >= CAPTURE_TIME) {
                    stop("capped");
                }
            }
        }, 20, 20);

        if (broadcast) {
            CC.broadcast("&f&l| &bKoth &2" + getID() +
                            " &bis now being capped by: &f" + getKing().getName());
        }
    }

    public void stopCaptureTimer(Player player, boolean broadcast) {
        PlayerStopCaptureKothEvent event = new PlayerStopCaptureKothEvent(cappingPlayer, this);
        Bukkit.getPluginManager().callEvent(event);
        setNoKing();

        Bukkit.getScheduler().cancelTask(capTimerID);
        capTimerID = -1;
        capTimer = 0;
        lastCapDelay = -1;

        isBeingCapped = false;
        cappingPlayer = null;
    }


    public Location getCenterBlock() {
        double x1 = getCorner1().getBlockX();
        double x2 = getCorner2().getBlockX();

        double y1 = getCorner1().getBlockY();
        double y2 = getCorner2().getBlockY();

        double z1 = getCorner1().getBlockZ();
        double z2 = getCorner2().getBlockZ();

        double centerX = (x1 + x2) / 2;
        double centerY = (y1 + y2) / 2;
        double centerZ = (z1 + z2) / 2;

        return new Location(getCorner2().getWorld(), centerX, centerY, centerZ);
    }
}
