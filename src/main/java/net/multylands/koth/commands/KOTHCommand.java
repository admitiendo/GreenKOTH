package net.multylands.koth.commands;

import net.multylands.koth.GreenKOTH;
import net.multylands.koth.manager.KothCreationManager;
import net.multylands.koth.manager.KothManager;
import net.multylands.koth.object.Koth;
import net.multylands.koth.utils.chat.CC;
import net.multylands.koth.utils.commands.Command;
import net.multylands.koth.utils.commands.CommandArgs;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public class KOTHCommand {
    GreenKOTH plugin = GreenKOTH.get();
    FileConfiguration languageConfig = plugin.languageConfig;

    public static KothCreationManager kothCreationManager = new KothCreationManager();

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

    @Command(name = "kothstop",
            permission = "greenkoth.admin",
            description = "GreenKoth stop koth command",
            inGameOnly = true,
            usage = "/koth stop")
    public void stopCommand(CommandArgs args) {
        Player player = args.getPlayer();
        if (!GreenKOTH.kothManager.isThereAKoth()) {
            return;
        }

        Koth koth = GreenKOTH.current;

        koth.stop("command");
        player.sendMessage(CC.translate("&cStopped current koth."));
    }

    @Command(name = "koth.list",
            permission = "greenkoth.admin",
            description = "GreenKoth list command",
            inGameOnly = true,
            usage = "/koth list")
    public void kothList(CommandArgs args) {
        Player player = args.getPlayer();


    }

    @Command(name = "kothdelete",
            permission = "greenkoth.admin",
            description = "GreenKoth delete command",
            inGameOnly = true,
            usage = "/kothdelete (Koth)")
    public void kothDelete(CommandArgs args) throws IOException {
        Player player = args.getPlayer();
        String kothName = args.getArgs(0);

        if (GreenKOTH.kothManager.getKothByID(kothName) != null) {
            if (!GreenKOTH.kothManager.isThereAKoth() && GreenKOTH.current != GreenKOTH.kothManager.getKothByID(kothName)) {
                if (GreenKOTH.kothManager.deleteKothFromFile(kothName)) {
                    player.sendMessage(CC.translate("&bSuccessfully deleted koth &f" + kothName));
                } else {
                    player.sendMessage(CC.translate("&cKoth &f" + kothName+ " &cdoes not exists."));
                }
            } else {
                player.sendMessage(CC.translate("&bCouldn't delete koth, please stop the running koth and try again."));
            }
        }


    }

    @Command(name = "koth.create",
            permission = "greenkoth.admin",
            description = "GreenKoth create koth command",
            inGameOnly = true,
            usage = "/koth create")
    public void createCommand(CommandArgs args) {
        Player player = args.getPlayer();
        if (!kothCreationManager.createList.containsKey(args.getPlayer())) {
            player.sendMessage(CC.translate("&bPlease type the KOTH name in the chat &Cwithout &bspaces."));
            kothCreationManager.createList.put(player, new Koth());
        } else {
            player.sendMessage(CC.translate("&cYou are already creating a koth."));
        }
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

    @Command(name = "kothstart",
            permission = "greenkoth.admin",
            description = "GreenKoth start command",
            inGameOnly = true,
            usage = "/kothstart <Koth>")
    public void startKoth(CommandArgs args) {
        Player player = args.getPlayer();
        String kothName = args.getArgs(0);
        if (player.hasPermission("greenkoth.admin")) {
            if (GreenKOTH.kothManager.koths.contains(GreenKOTH.kothManager.getKothByID(kothName))) {
                Koth koth = GreenKOTH.kothManager.getKothByID(kothName);
                if (GreenKOTH.kothManager.isThereAKoth()) {
                    player.sendMessage(CC.translate("&cThe koth &f" + koth.getID() + " &cis currently active!."));
                    return;
                }
                koth.start();
            }
        }
    }
}
