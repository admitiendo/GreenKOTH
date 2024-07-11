package net.multylands.koth.manager;

import net.multylands.koth.utils.chat.CC;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class KothCreationManager {

    public static HashMap<Player, String> creationPlayer = new HashMap<>();

    public void secondStepCreation(Player player, String kothName) {
        creationPlayer.put(player, kothName);
        ItemStack item = new ItemStack(Material.GOLDEN_HOE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(CC.translate("&bSelect the corners. (Left Click / Right Click)"));
        meta.setLore(Arrays.asList(
                CC.translate("&bCorner 1: &cNot set"),
                CC.translate("&BCorner 2: &cNot set")));
        meta.addEnchant(Enchantment.VANISHING_CURSE, 5, false);
        item.setItemMeta(meta);

        player.getInventory().addItem(item);
        player.sendMessage(CC.translate("&aYou have been given the wand to select the koth corners."));
        player.sendMessage(CC.translate("&aLeft Click for Corner 1"));
        player.sendMessage(CC.translate("&aRight Click for Corner 2"));
    }

    public void successfullyCreated(Player player) {
        player.sendMessage(CC.translate("&aSuccessfully created new koth!"));
        creationPlayer.remove(player);
    }
}
