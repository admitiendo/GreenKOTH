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

    }

    @EventHandler
    public void onAreaLeave(PlayerMoveEvent event) {

    }
}
