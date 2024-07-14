package net.multylands.koth.listeners;


import net.multylands.koth.GreenKOTH;
import net.multylands.koth.manager.KothManager;
import net.multylands.koth.object.Koth;
import net.multylands.koth.utils.LocationUtils;
import net.multylands.koth.utils.chat.CC;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class LocationListener implements Listener {
    BukkitScheduler scheduler = GreenKOTH.get().getServer().getScheduler();

    @EventHandler
    public void onAreaEnter(PlayerMoveEvent event) {
        if (GreenKOTH.kothManager.isThereAKoth()) {
            KothManager manager = GreenKOTH.kothManager;
            Koth koth = manager.getCurrentKoth();
            Player player = event.getPlayer();
            if (LocationUtils.checkIfIsInBetweenLocations(koth.corner1, koth.corner2, player.getLocation())) {
                if (koth.isThereAKing()) {
                    return;
                }

                koth.setKing(player);

                scheduler.runTaskTimer(GreenKOTH.get(), () -> {
                    if (LocationUtils.checkIfIsInBetweenLocations(koth.corner1, koth.corner2, player.getLocation())) {
                        player.sendMessage(CC.translate("&bYou capped " + koth.getID() + "!"));
                        koth.stop("capped");
                    } else {
                        koth.setNoKing();
                    }
                }, (koth.getCapTime() * 20L), (koth.getCapTime() * 20L));
            }
        }
    }

    @EventHandler
    public void onAreaLeave(PlayerMoveEvent event) {
        KothManager manager = GreenKOTH.kothManager;
        if (manager.isThereAKoth()) {
            Koth koth = manager.getCurrentKoth();
            Player player = event.getPlayer();
            if (koth.isThereAKing()) {
                if (!LocationUtils.checkIfIsInBetweenLocations(koth.corner1, koth.corner2, player.getLocation())) {
                    if (koth.getKing() == player) {
                        koth.setNoKing();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        KothManager manager = GreenKOTH.kothManager;
        if (manager.isThereAKoth()) {
            Koth koth = manager.getCurrentKoth();
            Player player = event.getPlayer();
            if (koth.isThereAKing()) {
                if (koth.getKing() == player) {
                    koth.setNoKing();
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        KothManager manager = GreenKOTH.kothManager;
        if (manager.isThereAKoth()) {
            Koth koth = manager.getCurrentKoth();
            Player player = event.getEntity();
            if (koth.isThereAKing()) {
                if (koth.getKing() == player) {
                    koth.setNoKing();
                }
            }
        }
    }
}
