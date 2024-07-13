package net.multylands.koth.object;

import net.multylands.koth.GreenKOTH;
import net.multylands.koth.utils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.IOException;

public class Koth {
    public String ID;
    public int capTime;
    public Location corner1;
    public Location corner2;
    public String kingUUID = null;

    public Koth() {

    }

    public Koth(Location corner1, Location corner2, String ID, int capTime) {
        Location corner1Modified = corner1.clone();
        corner1Modified.setY(corner1.getY() + 3);
        this.corner1 = corner1Modified;
        this.corner2 = corner2;
        this.ID = ID;
        this.capTime = capTime;

        try {
            GreenKOTH.kothManager.saveKothToFile(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        this.corner1 = loc;
    }

    public void setKingUUID(String kingUUID) {
        this.kingUUID = kingUUID;
        CC.broadcast(CC.translate(Bukkit.getPlayer(kingUUID).getDisplayName() + " is capping koth " + ID));
    }

    public void setNoKing() {
        this.kingUUID = null;
    }

    public boolean isThereAKing() {
        return !(kingUUID == null);
    }

    public String getKingUUID() {
        return kingUUID;
    }

    public String getID() {
        return ID;
    }

    public void start() {
        CC.broadcast("&f&l| &bThe koth " + ID + " has started!");
        CC.broadcast("&f&l| &bLocation: &7(&c"
                + getCenterBlock(corner1, corner2).getX() + " "
                + getCenterBlock(corner1, corner2).getY() + " "
                + getCenterBlock(corner1, corner2).getZ() + "&7)");
        GreenKOTH.kothManager.setActiveKoth(this);
    }

    public void stop(String reason) {
        switch (reason) {
            case "capped": {
                CC.broadcast("&f&l| &bThe koth " + ID + " has been capped!");
                CC.broadcast("&f&l| &bCapper: " + Bukkit.getPlayer(kingUUID).getDisplayName());
                GreenKOTH.kothManager.setActiveKoth(null);
                break;
            }
            case "command": {
                CC.broadcast("&f&l| &bThe koth " + ID + " has been stopped!");
                GreenKOTH.kothManager.setActiveKoth(null);
                break;
            }
        }
    }

    public Location getCenterBlock(Location c1, Location c2) {
        double x1 = c1.getBlockX();
        double x2 = c2.getBlockX();

        double y1 = c1.getBlockY();
        double y2 = c2.getBlockY();

        double z1 = c1.getBlockZ();
        double z2 = c2.getBlockZ();

        double centerX = (x1 + x2) / 2;
        double centerY = (y1 + y2) / 2;
        double centerZ = (z1 + z2) / 2;

        return new Location(c1.getWorld(), centerX, centerY, centerZ);
    }
}
