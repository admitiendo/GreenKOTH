package net.multylands.koth.listeners;

import net.multylands.koth.GreenKOTH;
import net.multylands.koth.commands.KOTHCommand;
import net.multylands.koth.manager.KothCreationManager;
import net.multylands.koth.object.Koth;
import net.multylands.koth.utils.chat.CC;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class KothCreationListener implements Listener {
    private ItemStack kothWand = new ItemStack(Material.GOLDEN_HOE);

    private Koth koth;

    private ArrayList<Player> capTimeList = new ArrayList<>();
    private ArrayList<Player> creationWand1 = new ArrayList<>();
    private ArrayList<Player> creationWand2 = new ArrayList<>();

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) throws IOException {
        Player player = event.getPlayer();
        if (player.hasPermission("greenkoth.admin")) {
            if (KOTHCommand.kothCreationManager.createList.containsKey(player)) {
                if (KOTHCommand.kothCreationManager.kothNameSetted.contains(player)) {
                    player.sendMessage(CC.translate("&CThe KOTH name is already set. Please continue with the second step."));
                    return;
                }

                if (!event.getMessage().contains(" ")) {
                    player.sendMessage(CC.translate("&aPerfect!, the KOTH name is: &f" + event.getMessage()));
                    Koth k = KOTHCommand.kothCreationManager.createList.get(player);
                    koth = k;
                    k.setID(event.getMessage());
                    KOTHCommand.kothCreationManager.kothNameSetted.add(player);
                    event.setCancelled(true);

                    ItemMeta meta = kothWand.getItemMeta();

                    meta.setDisplayName(CC.translate("&bSelect the corners."));
                    meta.setLore(Arrays.asList(
                            CC.translate("&bCorner 1: &cNot set"),
                            CC.translate("&BCorner 2: &cNot set")));
                    meta.addEnchant(Enchantment.VANISHING_CURSE, 5, false);
                    kothWand.setItemMeta(meta);

                    player.getInventory().addItem(kothWand);
                    player.sendMessage(CC.translate("&aYou have been given the wand to select the koth corners."));
                    player.sendMessage(CC.translate("&aLeft Click for set the corner."));

                    creationWand1.add(player);
                } else {
                    player.sendMessage(CC.translate("&cThe KOTH name must not have spaces."));
                    event.setCancelled(true);
                }
            } else if (capTimeList.contains(player)) {
                if (!event.getMessage().contains(" ")) {
                    if (StringUtils.isNumeric(event.getMessage())) {
                        capTimeList.remove(player);
                        event.setCancelled(true);
                        int time = Integer.parseInt(event.getMessage());

                        player.getWorld().playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0f, 2.0f);
                        koth.setCapTime(time);
                        KOTHCommand.kothCreationManager.kothNameSetted.remove(player);
                        player.sendMessage(CC.translate("&bThe KOTH &f" + koth.getID() + " &bhas been successfully created!"));
                        GreenKOTH.kothManager.saveKothToFile(koth.build());
                    } else {
                        event.setCancelled(true);
                        player.sendMessage(CC.translate("&cThe KOTH cap time must be numbers only. &7&O(Seconds)"));
                    }
                } else {
                    player.sendMessage(CC.translate("&cThe KOTH cap time must not have spaces."));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("greenkoth.admin")) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (creationWand1.contains(player)) {
                    event.setCancelled(true);
                    if (!event.hasItem()) {
                        return;
                    }
                    assert event.getItem() != null;
                    assert event.getItem().getItemMeta() != null;

                    if (!event.getItem().equals(kothWand)) {
                        return;
                    }

                    int x = event.getClickedBlock().getX();
                    int y = event.getClickedBlock().getY();
                    int z = event.getClickedBlock().getZ();

                    player.sendMessage(CC.translate("&bYou set corner 1 to: &7(&f" + x + ", " + y + ", " + z + "&7)"));
                    player.sendMessage(CC.translate("&bYou now can set corner 2."));

                    koth.setCorner1(event.getClickedBlock().getLocation());

                    creationWand2.add(player);
                    creationWand1.remove(player);
                } else if (creationWand2.contains(player)) {
                    event.setCancelled(true);
                    if (!event.hasItem()) {
                        return;
                    }
                    assert event.getItem() != null;
                    assert event.getItem().getItemMeta() != null;

                    if (!event.getItem().equals(kothWand)) {
                        return;
                    }

                    int x = event.getClickedBlock().getX();
                    int y = event.getClickedBlock().getY();
                    int z = event.getClickedBlock().getZ();

                    player.sendMessage(CC.translate("&bYou set corner 2 to: &7(&f" + x + ", " + y + ", " + z + "&7)"));
                    player.sendMessage(CC.translate("&bNow shift + right click to the air to set the KOTH captime."));

                    koth.setCorner2(event.getClickedBlock().getLocation());

                    KOTHCommand.kothCreationManager.createList.remove(player);
                    creationWand2.remove(player);
                }
            } else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                if (!event.hasItem()) {
                    return;
                }
                assert event.getItem() != null;
                assert event.getItem().getItemMeta() != null;

                if (!event.getItem().equals(kothWand)) {
                    return;
                }

                if (!player.isSneaking()) {
                    return;
                }

                player.getInventory().remove(event.getItem());
                player.sendMessage(CC.translate("&bType the KOTH cap time in seconds."));

                capTimeList.add(player);
            }
        }
    }
}
