package net.multylands.koth.listeners;

import net.multylands.koth.object.Koth;
import net.multylands.koth.object.events.PlayerCaptureKothEvent;
import net.multylands.koth.object.events.PlayerEnterKothEvent;
import net.multylands.koth.object.events.PlayerExitKothEvent;
import net.multylands.koth.utils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KothEventsListener implements Listener {


    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onKothEnter(PlayerEnterKothEvent e) {
        Player player = e.getPlayer();
        Koth koth = e.getKoth();

        if (koth.isThereAKing()) {
            e.setCancelled(true);
        }

        koth.startCaptureTimer(player, true);
    }


    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onKothExit(PlayerExitKothEvent e) {
        Player player = e.getPlayer();
        Koth koth = e.getKoth();

        if (koth.isThereAKing() && koth.getKing().equals(player)) {
             koth.stopCaptureTimer(player, true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerCaptureKoth(PlayerCaptureKothEvent e) {
        Player player = e.getPlayer();
        Koth koth = e.getKoth();

        List<ItemStack> loot = new ArrayList<ItemStack>();
        Map<String, Double> nonItemLoot = new HashMap<String, Double>();


        e.setLoot(loot);
        e.setNonItemLoot(nonItemLoot);
    }
}
