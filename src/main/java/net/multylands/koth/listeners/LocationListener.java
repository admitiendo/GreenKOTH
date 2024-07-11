package net.multylands.koth.listeners;


import net.multylands.koth.GreenKOTH;
import net.multylands.koth.manager.KothManager;
import net.multylands.koth.object.Koth;
import net.multylands.koth.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class LocationListener implements Listener {
    GreenKOTH plugin;

    BukkitScheduler scheduler = GreenKOTH.get().getServer().getScheduler();

    public LocationListener(GreenKOTH plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAreaEnter(PlayerMoveEvent event) {
        KothManager manager = GreenKOTH.kothManager;
        if (manager.isThereAKoth()) {
            Koth koth = manager.getCurrentKoth();
            Player player = event.getPlayer();
            if (LocationUtils.checkIfIsInBetweenLocations(koth.corner1, koth.corner2, player.getLocation())) {
                koth.setKingUUID(player.getUniqueId().toString());
                scheduler.runTaskTimer(GreenKOTH.get(), () -> {

                }, koth.getCapTime(), koth.getCapTime());
            }
        }
    }

    @EventHandler
    public void onAreaLeave(PlayerMoveEvent event) {
        KothManager manager = GreenKOTH.kothManager;
        if (manager.isThereAKoth()) {
            Koth koth = manager.getCurrentKoth();
            Player player = event.getPlayer();
            if (!LocationUtils.checkIfIsInBetweenLocations(koth.corner1, koth.corner2, player.getLocation())) {
                koth.setNoKing();
            }
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        KothManager manager = GreenKOTH.kothManager;
        if (manager.isThereAKoth()) {
            Koth koth = manager.getCurrentKoth();
            Player player = event.getPlayer();
            if (koth.getKingUUID().equals(player.getUniqueId().toString())) {
                koth.setNoKing();
            }
        }
    }
}
