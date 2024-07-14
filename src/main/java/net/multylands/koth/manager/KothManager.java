package net.multylands.koth.manager;

import net.multylands.koth.GreenKOTH;
import net.multylands.koth.object.Koth;
import net.multylands.koth.utils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.GameEvent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KothManager {
    public List<Koth> koths = new ArrayList<>();

    public Koth currentActive;
    public boolean thereIsKoth;

    public Koth getKothByID(String id) {
        for (Koth k : koths) {
            if (k.getID().equals(id)) {
                return k;
            }
        }
        return null;
    }

    public void saveKothToFile(Koth k) throws IOException {
        FileConfiguration areas = GreenKOTH.get().getAreasConfig();
        areas.set("Koths", k.getID());
        saveLocation1(k);
        saveLocation2(k);
        areas.set("Koths." + k.getID() + ".capTime", k.getCapTime());

        areas.save(GreenKOTH.get().areasFile);
        koths.add(k);
    }

    public Location getCorner1FromFile(String kothName) {
        FileConfiguration areas = GreenKOTH.get().getAreasConfig();
        double x = areas.getDouble("Koths." + kothName + ".corner1.x");
        double y = areas.getDouble("Koths." + kothName + ".corner1.y");
        double z = areas.getDouble("Koths." + kothName + ".corner1.z");
        String worldName = areas.getString("Koths." + kothName + ".world");
        World w;
        if (worldName == null) {
            w = Bukkit.getWorld("world");
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cThe koth &f" + kothName + " &cdoes not have a correct world, tried to use the default \"world\""));
        } else {
            w = Bukkit.getServer().getWorld(worldName);
        }
        return new Location(w, x, y, z);
    }

    public Location getCorner2FromFile(String kothName) {
        FileConfiguration areas = GreenKOTH.get().getAreasConfig();
        double x = areas.getDouble("Koths." + kothName + ".corner2.x");
        double y = areas.getDouble("Koths." + kothName + ".corner2.y");
        double z = areas.getDouble("Koths." + kothName + ".corner2.z");
        String worldName = areas.getString("Koths." + kothName + ".world");
        World w;
        if (worldName == null) {
            w = Bukkit.getWorld("world");
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cThe koth &f" + kothName + " &cdoes not have a correct world, tried to use the default \"world\""));
        } else {
            w = Bukkit.getServer().getWorld(worldName);
        }
        return new Location(w, x, y, z);
    }

    public void saveLocation1(Koth k) {
        FileConfiguration areas = GreenKOTH.get().getAreasConfig();
        areas.set("Koths." + k.getID() + ".world", k.getCorner1().getWorld().getName());
        areas.set("Koths." + k.getID() + ".corner1.x", k.getCorner1().getX());
        areas.set("Koths." + k.getID() + ".corner1.y", k.getCorner1().getY());
        areas.set("Koths." + k.getID() + ".corner1.z", k.getCorner1().getZ());
    }

    public void saveLocation2(Koth k) {
        FileConfiguration areas = GreenKOTH.get().getAreasConfig();
        areas.set("Koths." + k.getID() + ".world", k.getCorner2().getWorld().getName());
        areas.set("Koths." + k.getID() + ".corner2.x", k.getCorner2().getX());
        areas.set("Koths." + k.getID() + ".corner2.y", k.getCorner2().getY());
        areas.set("Koths." + k.getID() + ".corner2.z", k.getCorner2().getZ());
    }

    public List<Koth> getKothsFromFile() {
        FileConfiguration areas = GreenKOTH.get().getAreasConfig();
        if (areas.getConfigurationSection("Koths") != null) {
            List<Koth> toReturn = new ArrayList<>();
            ConfigurationSection section = GreenKOTH.get().areasConfig.getConfigurationSection("Koths");

            assert section != null;
            for (String entry : section.getKeys(false)) {
                Location corner1 = getCorner1FromFile(entry);
                Location corner2 = getCorner2FromFile(entry);
                int capTime = section.getInt(entry + ".capTime");

                assert corner1 != null;
                assert corner2 != null;
                if (!toReturn.contains(new Koth(corner1, corner2, entry, capTime))) {
                    toReturn.add(new Koth(corner1, corner2, entry, capTime));
                }
            }
            return toReturn;
        }
        return null;
    }

    public void setActiveKoth(Koth koth) {
        currentActive = koth;
        thereIsKoth = koth != null;
    }

    public void disableKoth() {
        thereIsKoth = false;
        currentActive = null;
    }

    public boolean isThereAKoth() {
        return thereIsKoth;
    }

    public Koth getCurrentKoth() {
        if (currentActive != null) {
            return currentActive;
        }
        return null;
    }
}
