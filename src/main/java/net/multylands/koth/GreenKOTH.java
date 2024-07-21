package net.multylands.koth;

import net.multylands.koth.commands.KOTHCommand;
import net.multylands.koth.listeners.KothCreationListener;
import net.multylands.koth.listeners.KothEventsListener;
import net.multylands.koth.listeners.LocationListener;
import net.multylands.koth.manager.KothManager;
import net.multylands.koth.object.Koth;
import net.multylands.koth.utils.ConfigUtils;
import net.multylands.koth.utils.chat.CC;
import net.multylands.koth.utils.commands.CommandFramework;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class GreenKOTH extends JavaPlugin {
    public static HashMap<String, Koth> kothList = new HashMap<>();
    public static String newVersion = null;
    public File areasFile;
    public String areasFileName = "areas.yml";
    public File configFile;
    public String configFileName = "config.yml";
    public File languageFile;
    public String languageFileName = "language.yml";
    public FileConfiguration ignoresConfig;
    public FileConfiguration areasConfig;
    public FileConfiguration languageConfig;
    public static BukkitScheduler scheduler = Bukkit.getScheduler();
    public static HashMap<String, CommandExecutor> commandExecutors = new HashMap<>();

    public static KothManager kothManager;

    public static Koth current;

    @Override
    public void onEnable() {
        createConfigs();
        kothManager = new KothManager();
        CommandFramework framework = new CommandFramework(this);
        framework.registerCommands(new KOTHCommand());

        Bukkit.getPluginManager().registerEvents(new LocationListener(), this);
        Bukkit.getPluginManager().registerEvents(new KothCreationListener(), this);
        Bukkit.getPluginManager().registerEvents(new KothEventsListener(), this);

        Bukkit.getConsoleSender().sendMessage(CC.translate("&bThe koth list is loading. please wait."));
        scheduler.runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                if (kothManager.getKothsFromFile() != null) {
                    kothManager.koths.addAll(kothManager.getKothsFromFile());
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&bThe KOTH list has loaded!, &7Current koths:"));
                    for (Koth koth : kothManager.koths) {
                        Bukkit.getConsoleSender().sendMessage(CC.translate("&f&l| &b" + koth.ID));
                    }
                } else {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&cCouldn't load koths, make sure to create at least 1 koth."));
                }
            }
        }, 200);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&cGreenKOTH plugin has been disabled!"));
    }

    public static GreenKOTH get() {
        return getPlugin(GreenKOTH.class);
    }

    public FileConfiguration getAreasConfig() {
        return areasConfig;
    }

    private void createConfigs() {
        try {
            ConfigUtils configUtils = new ConfigUtils(this);
            areasFile = new File(getDataFolder(), areasFileName);
            configFile = new File(getDataFolder(), configFileName);
            languageFile = new File(getDataFolder(), languageFileName);
            //we are checking if files exist to avoid console spamming. try it and see :)
            if (!languageFile.exists()) {
                saveResource(languageFileName, false);
            }
            if (!configFile.exists()) {
                saveDefaultConfig();
            }
            if (!areasFile.exists()) {
                saveResource(areasFileName, false);
            }
            areasConfig = new YamlConfiguration();
            ignoresConfig = new YamlConfiguration();
            languageConfig = new YamlConfiguration();

            areasConfig.load(areasFile);
            languageConfig.load(languageFile);
            getConfig().load(configFile);
            configUtils.addMissingKeysAndValues(getConfig(), configFileName);
            configUtils.addMissingKeysAndValues(areasConfig, areasFileName);
            configUtils.addMissingKeysAndValues(languageConfig, languageFileName);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveAreasConfig() {
        try {
            areasConfig.save(areasFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reloadAreasConfig() {
        areasFile = new File(getDataFolder(), areasFileName);
        areasConfig = YamlConfiguration.loadConfiguration(areasFile);
        kothList.clear();
    }

    public void reloadLanguageConfig() {
        languageFile = new File(getDataFolder(), "language.yml");
        languageConfig = YamlConfiguration.loadConfiguration(languageFile);
    }
}
