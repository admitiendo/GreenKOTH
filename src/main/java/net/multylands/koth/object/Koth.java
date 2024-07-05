package net.multylands.koth.object;

import net.multylands.koth.GreenKOTH;
import net.multylands.koth.utils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Koth {
    public String ID;
    public Location corner1;
    public Location corner2;
    public String kingUUID = null;

    public Koth(Location corner1, Location corner2, String ID) {
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.ID = ID;
    }

    public Location getFirstCorner() {
        return corner1;
    }

    public Location getSecondCorner() {
        return corner2;
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

    public void startKoth() {
        Bukkit.getScheduler().runTaskTimer(GreenKOTH.get(), () -> {
        }, 1, 2);
    }
}
