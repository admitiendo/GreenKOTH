package net.multylands.koth.listeners;

import net.multylands.koth.utils.chat.CC;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class KothCreationListener implements Listener {

    private ArrayList<Player> clicklist = new ArrayList<>();

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("greenkoth.admin")) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (clicklist.contains(player)) {
                    player.sendMessage(CC.translate("&CYou already set corner 1!"));
                    return;
                }

                if (!event.hasItem()) {
                    return;
                }

                ItemStack item = event.getItem();

                if (!item.hasItemMeta()) {
                    return;
                }

                if (!item.getItemMeta().getDisplayName().equals(CC.translate("&bSelect the corners. (Left Click / Right Click)"))) {
                    return;
                }

                ItemMeta meta = item.getItemMeta();

                int x = event.getClickedBlock().getX();
                int y = event.getClickedBlock().getY();
                int z = event.getClickedBlock().getZ();

                meta.setDisplayName(CC.translate("&bSelect the corners."));
                meta.setLore(Arrays.asList(
                        CC.translate("&bCorner 1: &7(&f" + x + ", " + y + ", " + z + "&7)"),
                        CC.translate("&BCorner 2: &cNot set")));

                player.getInventory().remove(item);
                item.setItemMeta(meta);
                player.getInventory().addItem(item);

                player.sendMessage(CC.translate("&bYou set corner 1 to: &7(&f" + x + ", " + y + ", " + z + "&7)"));
                player.sendMessage(CC.translate("&bYou now can set corner 2 with right click."));
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (!event.hasItem()) {
                    return;
                }

                ItemStack item = event.getItem();

                if (!item.hasItemMeta()) {
                    return;
                }

                if (!item.getItemMeta().getDisplayName().equals(CC.translate("&bSelect the corners. (Left Click / Right Click)"))) {
                    return;
                }

                ItemMeta meta = item.getItemMeta();

                int x = event.getClickedBlock().getX();
                int y = event.getClickedBlock().getY();
                int z = event.getClickedBlock().getZ();

                String loreline1 = meta.getLore().get(0);
                meta.setLore(Arrays.asList(loreline1,
                        CC.translate("&BCorner 2: &7(&f" + x + ", " + y + ", " + z + "&7)")));

                player.getInventory().remove(item);
                item.setItemMeta(meta);
                player.getInventory().addItem(item);

                clicklist.add(player);
                player.sendMessage(CC.translate("&bYou set corner 2 to: &7(&f" + x + ", " + y + ", " + z + "&7)"));
                player.sendMessage(CC.translate("&bYou can now set the koth cap time."));
            }
        }
    }
}
