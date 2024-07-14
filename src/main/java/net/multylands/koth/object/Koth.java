package net.multylands.koth.object;

import net.multylands.koth.GreenKOTH;
import net.multylands.koth.utils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;

public class Koth {
    public String ID;
    public int capTime;
    public Location corner1;
    public Location corner2;
    public Player king;

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
        CC.broadcast(CC.translate(player.getDisplayName() + " &bis capping koth &f" + ID));
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
                assert king != null;
                CC.broadcast("&f&l| &bCapper: " + king.getName());
                GreenKOTH.kothManager.setActiveKoth(null);
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
