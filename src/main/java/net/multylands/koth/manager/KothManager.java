package net.multylands.koth.manager;

import net.multylands.koth.GreenKOTH;
import net.multylands.koth.object.Koth;
import org.bukkit.GameEvent;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KothManager {
    FileConfiguration areas = GreenKOTH.get().areasConfig;

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
        areas.set("Koths", k.getID());
        areas.set("Koths." + k.getID() + ".corner1", k.getCorner1());
        areas.set("Koths." + k.getID() + ".corner2", k.getCorner2());
        areas.set("Koths." + k.getID() + ".capTime", k.getCapTime());
        areas.save(GreenKOTH.get().areasFile);
    }

    public List<Koth> getKothsFromFile() {
        List<Koth> toReturn = new ArrayList<>();
        ConfigurationSection section = areas.getConfigurationSection("Koths");

        for (Map.Entry<String, Object> entry : section.getValues(false).entrySet()) {
            String id = section.getString(entry.getKey());
            Location corner1 = section.getLocation(id + ".corner1");
            Location corner2 = section.getLocation(id + ".corner2");
            int capTime = section.getInt(id + ".capTime");

            toReturn.add(new Koth(corner1, corner2, id, capTime));
        }
        return toReturn;
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
