package net.multylands.koth.listeners;


import net.multylands.koth.GreenKOTH;
import net.multylands.koth.manager.KothManager;
import net.multylands.koth.object.Koth;
import net.multylands.koth.object.LocationPair;
import net.multylands.koth.object.events.PlayerEnterKothEvent;
import net.multylands.koth.object.events.PlayerExitKothEvent;
import net.multylands.koth.utils.LocationUtils;
import net.multylands.koth.utils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class LocationListener implements Listener {
    BukkitScheduler scheduler = GreenKOTH.get().getServer().getScheduler();

    @EventHandler
    public void onKothEnter(PlayerMoveEvent e) {
        if (GreenKOTH.kothManager.isThereAKoth()) {
            Koth koth = GreenKOTH.kothManager.getCurrentKoth();
            Player player = e.getPlayer();

            LocationPair pair = new LocationPair(koth.getCorner1(), koth.getCorner2());

            if(LocationUtils.isLocationInside(e.getTo(), pair) && !LocationUtils.isLocationInside(e.getFrom(), pair)) { // Player entered KoTH capture zone
                PlayerEnterKothEvent event = new PlayerEnterKothEvent(e.getPlayer(), koth, e.getFrom(), e.getTo());
                event.setCancelled(e.isCancelled());

                Bukkit.getPluginManager().callEvent(event);

                e.setFrom(event.getFrom());
                e.setTo(event.getTo());
                e.setCancelled(event.isCancelled());
            } else if(!LocationUtils.isLocationInside(e.getTo(), pair) && LocationUtils.isLocationInside(e.getFrom(), pair)) { // Player left KoTH capture zone
                PlayerExitKothEvent event = new PlayerExitKothEvent(e.getPlayer(), koth, e.getFrom(), e.getTo());
                event.setCancelled(e.isCancelled());

                Bukkit.getPluginManager().callEvent(event);

                e.setFrom(event.getFrom());
                e.setTo(event.getTo());
                e.setCancelled(event.isCancelled());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if (GreenKOTH.kothManager.isThereAKoth()) {
            Koth koth = GreenKOTH.kothManager.getCurrentKoth();

            LocationPair pair = new LocationPair(koth.getCorner1(), koth.getCorner2());

            if (LocationUtils.isLocationInside(e.getTo(), pair) && !LocationUtils.isLocationInside(e.getFrom(), pair)) { // Player entered KoTH capture zone
                PlayerEnterKothEvent event = new PlayerEnterKothEvent(e.getPlayer(), koth, e.getFrom(), e.getTo());
                event.setCancelled(e.isCancelled());

                Bukkit.getPluginManager().callEvent(event);

                e.setFrom(event.getFrom());
                e.setTo(event.getTo());
                e.setCancelled(event.isCancelled());
            } else if (!LocationUtils.isLocationInside(e.getTo(), pair) && LocationUtils.isLocationInside(e.getFrom(), pair)) { // Player left KoTH capture zone
                PlayerExitKothEvent event = new PlayerExitKothEvent(e.getPlayer(), koth, e.getFrom(), e.getTo());
                event.setCancelled(e.isCancelled());

                Bukkit.getPluginManager().callEvent(event);

                e.setFrom(event.getFrom());
                e.setTo(event.getTo());
                e.setCancelled(event.isCancelled());
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
