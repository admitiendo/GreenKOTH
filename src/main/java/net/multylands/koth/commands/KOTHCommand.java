package net.multylands.koth.commands;

import net.multylands.koth.GreenKOTH;
import net.multylands.koth.manager.KothCreationManager;
import net.multylands.koth.manager.KothManager;
import net.multylands.koth.object.Koth;
import net.multylands.koth.utils.chat.CC;
import net.multylands.koth.utils.commands.Command;
import net.multylands.koth.utils.commands.CommandArgs;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.K;

import java.util.Arrays;
import java.util.Collections;

public class KOTHCommand {
    GreenKOTH plugin;
    FileConfiguration languageConfig = plugin.languageConfig;

    @Command(name = "koth",
            permission = "greenkoth.admin",
            description = "GreenKoth Main Command",
            inGameOnly = true,
            usage = "/koth")
    public void mainCommand(CommandArgs args) {
        for (String msg : languageConfig.getStringList("admin.help")) {
            args.getSender().sendMessage(CC.translate(msg));
        }
    }

    @Command(name = "koth.create",
            permission = "greenkoth.admin",
            description = "GreenKoth create koth command",
            inGameOnly = true,
            usage = "/koth create")
    public void createCommand(CommandArgs args) {
        ItemStack item = new ItemStack(Material.BEACON);
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.ARROW_FIRE, 2, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setLore(Arrays.asList(CC.translate("&bInput the koth name to proceed with the second step.")));
        item.setItemMeta(meta);

        Player player = args.getPlayer();
        new AnvilGUI.Builder()
                .interactableSlots(AnvilGUI.Slot.INPUT_LEFT)
                .itemLeft(item)
                        .onClose(stateSnapshot -> {
                    stateSnapshot.getPlayer().sendMessage(CC.translate("&f&L| &BNow select the two corners of the koth."));
                    new KothCreationManager().secondStepCreation(player, stateSnapshot.getText());
                }).onClick((slot, stateSnapshot) -> { // Either use sync or async variant, not both
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    return java.util.List.of();
                })
                .preventClose()
                .text("Name:")
                .title("Enter the name of the new KOTH.")
                .plugin(GreenKOTH.get())
                .open(player);
    }

    @Command(name = "koth.reload",
            permission = "greenkoth.admin",
            description = "GreenKoth reload command",
            inGameOnly = true,
            usage = "/koth reload")
    public void reloadCommand(CommandArgs args) {
        Player player = args.getPlayer();

        plugin.reloadAreasConfig();
        plugin.reloadConfig();
        plugin.reloadLanguageConfig();
        player.sendMessage(CC.translate(plugin.languageConfig.getString("admin.reload.all-config-reloaded")));

        KothManager kothManager = GreenKOTH.kothManager;


        if (!kothManager.getKothsFromFile().isEmpty()) {
           kothManager.koths.addAll(kothManager.getKothsFromFile());
           player.sendMessage(CC.translate("&f&l| &aLos koths se han cargado con exito!"));

            for (Koth koth : kothManager.koths) {
                player.sendMessage(CC.translate(
                        "&f&l| &b" + koth.ID +
                                " - " + koth.corner1.getBlockX() +
                                " - " + koth.corner1.getBlockY() +
                                " - " + koth.corner1.getBlockZ() +
                                " / " + koth.corner2.getBlockX() +
                                " - " + koth.corner2.getBlockY() +
                                " - " + koth.corner2.getBlockZ()));
            }
        } else {
            player.sendMessage(CC.translate("No se han podido cargar los koths, asegurate de crear al menos 1 koth."));
        }
    }

    @Command(name = "koth.start",
            permission = "greenkoth.admin",
            description = "GreenKoth start command",
            inGameOnly = true,
            usage = "/koth start <Koth>")
    public void startKoth(CommandArgs args) {

    }
}
