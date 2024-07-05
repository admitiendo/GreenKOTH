package net.multylands.koth.listeners;


import net.multylands.koth.GreenKOTH;
import net.multylands.koth.object.Koth;
import net.multylands.koth.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class LocationListener implements Listener {
    GreenKOTH plugin;

    public LocationListener(GreenKOTH plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAreaEnter(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLoc = player.getLocation();
        boolean isOnTop = false;
        Koth topArea = null;

        Koth current = GreenKOTH.current;

        if (!GreenKOTH.currentTimer.isKothActive()) {
            return;
        }

        for (Koth topAreaFromLoop : GreenKOTH.kothList.values()) {
            Location corner1 = topAreaFromLoop.getFirstCorner();
            Location corner2 = topAreaFromLoop.getSecondCorner();

            isOnTop = true;
            topArea = topAreaFromLoop;
            if (LocationUtils.checkIfIsInBetweenLocations(corner1, corner2, playerLoc)) {
                current.setKingUUID(player.getUniqueId().toString());
            }
            break;
        }

        for (Koth topAreaFromLoop : GreenKOTH.kothList.values()) {
            Location corner1 = topAreaFromLoop.getFirstCorner();
            Location corner2 = topAreaFromLoop.getSecondCorner();

            isOnTop = true;
            topArea = topAreaFromLoop;
            break;

        }
        if (!isOnTop) {
            return;
        }

        String UUID = player.getUniqueId().toString();
        topArea.setKingUUID(UUID);
        GreenKOTH.kothList.put(topArea.getID(), topArea);
    }

    @EventHandler
    public void onAreaLeave(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String playerUUID = player.getUniqueId().toString();
        Location playerLoc = player.getLocation();

        for (Koth topAreaFromLoop : GreenKOTH.kothList.values()) {
            Location corner1 = topAreaFromLoop.getFirstCorner();
            Location corner2 = topAreaFromLoop.getSecondCorner();
            if (!topAreaFromLoop.isThereAKing()) {
                continue;
            }
            if (LocationUtils.checkIfIsInBetweenLocations(corner1, corner2, playerLoc)) {
                continue;
            }
            if (!topAreaFromLoop.getKingUUID().equals(playerUUID)) {
                continue;
            }
            topAreaFromLoop.setKingUUID(null);
            GreenKOTH.kothList.put(topAreaFromLoop.getID(), topAreaFromLoop);
        }
    }
}
