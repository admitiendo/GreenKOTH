package net.multylands.koth.commands;

import net.multylands.koth.GreenKOTH;
import net.multylands.koth.object.Koth;
import net.multylands.koth.timer.KOTHTimer;
import net.multylands.koth.utils.chat.CC;
import net.multylands.koth.utils.commands.Command;
import net.multylands.koth.utils.commands.CommandArgs;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.K;

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

    @Command(name = "koth.arealist",
            permission = "greenkoth.admin",
            description = "GreenKoth Area list",
            inGameOnly = true,
            usage = "/koth arealist")
    public void areaListCommand(CommandArgs args) {
        Player player = args.getPlayer();

        if (plugin.areasConfig.getKeys(false).isEmpty()) {
            player.sendMessage(CC.translate(plugin.languageConfig.getString("admin.area-list.no-areas-available")));
            return;
        }
        player.sendMessage(CC.translate(languageConfig.getString("admin.area-list.meaning")));
        player.sendMessage(CC.translate(plugin.languageConfig.getString("admin.area-list.list")));
        String inactiveFormat = plugin.languageConfig.getString("admin.area-list.list-format.inactive");
        String activeFormat = plugin.languageConfig.getString("admin.area-list.list-format.active");
        for (String areaName : plugin.areasConfig.getKeys(false)) {
            if (plugin.areasConfig.getLocation(areaName + ".pos1") == null
                    || plugin.areasConfig.getLocation(areaName + ".pos2") == null) {
                player.sendMessage(CC.translate(inactiveFormat.replace("%area%", areaName)));
                continue;
            }
            player.sendMessage(CC.translate(activeFormat.replace("%area%", areaName)));
        }
    }

    @Command(name = "koth.createarea",
            permission = "greenkoth.admin",
            description = "GreenKoth create area command",
            inGameOnly = true,
            usage = "/koth createarea")
    public void createArea(CommandArgs args) {
        Player player = args.getPlayer();


        String areaName = args.getArgs(0);
        if (plugin.areasConfig.contains(areaName)) {
            player.sendMessage(CC.translate(plugin.languageConfig.getString("admin.create-area.already-exists")));
            return;
        }
        plugin.areasConfig.set(areaName + ".isnew", true);
        plugin.saveAreasConfig();
        player.sendMessage(CC.translate(plugin.languageConfig.getString("admin.create-area.success").replace("%area%", areaName)));

    }

    @Command(name = "koth.deletearea",
            permission = "greenkoth.admin",
            description = "GreenKoth delete area command",
            inGameOnly = true,
            usage = "/koth deletearea")
    public void deleteArea(CommandArgs args) {
        Player player = args.getPlayer();

        String areaName = args.getArgs(0);
        if (!plugin.areasConfig.contains(areaName)) {
            player.sendMessage(CC.translate(plugin.languageConfig.getString("admin.delete-area.doesnt-exists")));
            return;
        }
        plugin.areasConfig.set(areaName, null);
        plugin.saveAreasConfig();
        plugin.reloadAreasConfig();
        player.sendMessage(CC.translate(plugin.languageConfig.getString("admin.delete-area.success").replace("%area%", areaName)));

    }

    @Command(name = "koth.setpos",
            permission = "greenkoth.admin",
            description = "GreenKoth set pos command",
            inGameOnly = true,
            usage = "/koth setpos")
    public void setPos(CommandArgs args) {
        Player player = args.getPlayer();


        String topAreaName = args.getArgs(0);
        String pos = args.getArgs(1).toLowerCase();
        if (!plugin.areasConfig.contains(topAreaName)) {
            player.sendMessage(CC.translate(plugin.languageConfig.getString("admin.set-pos.wrong-area")));
            return;
        }
        if (!pos.equals("pos1") && !pos.equals("pos2")) {
            player.sendMessage(CC.translate(plugin.languageConfig.getString("admin.set-pos.wrong-pos")));
            return;
        }
        plugin.areasConfig.set(topAreaName + "." + pos, player.getLocation());
        //just removing the temporary value below
        plugin.areasConfig.set(topAreaName + ".isnew", null);
        plugin.saveAreasConfig();
        player.sendMessage(CC.translate(plugin.languageConfig.getString("admin.set-pos.success").replace("%pos%", pos)));

        if (plugin.areasConfig.getLocation(topAreaName + ".pos1") == null
                || plugin.areasConfig.getLocation(topAreaName + ".pos2") == null) {
            return;
        }
        Location loc1 = plugin.areasConfig.getLocation(topAreaName + ".pos1");
        Location loc2 = plugin.areasConfig.getLocation(topAreaName + ".pos2");
        Koth area = new Koth(loc1, loc2, topAreaName);
        GreenKOTH.kothList.put(topAreaName, area);
        player.sendMessage(CC.translate(plugin.languageConfig.getString("admin.set-pos.area-loaded")));
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

    }

    @Command(name = "koth.start",
            permission = "greenkoth.admin",
            description = "GreenKoth start command",
            inGameOnly = true,
            usage = "/koth start <Koth>")
    public void startKoth(CommandArgs args) {
        Player player = args.getPlayer();

        if (GreenKOTH.currentTimer.isKothActive()) {
            player.sendMessage(CC.translate("&cThe koth &f" + GreenKOTH.current.ID + " &cis currently active!"));
            return;
        }

        String kothname = args.getArgs(1);

        if (!plugin.areasConfig.contains(kothname)) {
            player.sendMessage(CC.translate("&cThe koth &f" + kothname + " does not exist!"));
            player.sendMessage(CC.translate(plugin.languageConfig.getString("admin.area-list.list")));
            String inactiveFormat = plugin.languageConfig.getString("admin.area-list.list-format.inactive");
            String activeFormat = plugin.languageConfig.getString("admin.area-list.list-format.active");
            for (String areaName : plugin.areasConfig.getKeys(false)) {
                if (plugin.areasConfig.getLocation(areaName + ".pos1") == null
                        || plugin.areasConfig.getLocation(areaName + ".pos2") == null) {
                    player.sendMessage(CC.translate(inactiveFormat.replace("%area%", areaName)));
                    continue;
                }
                player.sendMessage(CC.translate(activeFormat.replace("%area%", areaName)));
            }
            return;
        }

        Location loc1 = plugin.areasConfig.getLocation(kothname + ".pos1");
        Location loc2 = plugin.areasConfig.getLocation(kothname + ".pos2");

        Koth koth = new Koth(loc1, loc2, kothname);
        KOTHTimer timer = new KOTHTimer(GreenKOTH.get(), koth);
        koth.startKoth();
        timer.setKothActive(true);
        timer.startTimer();

        GreenKOTH.currentTimer = timer;
        GreenKOTH.current = koth;


        player.sendMessage(CC.translate(""));
    }
}
