package net.multylands.koth.timer;

import net.multylands.koth.GreenKOTH;
import net.multylands.koth.object.Koth;
import net.multylands.koth.utils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class KOTHTimer {
    GreenKOTH plugin;
    Koth koth;
    public boolean isActive;

    public KOTHTimer(GreenKOTH plugin, Koth kth) {
        this.plugin = plugin;
        this.koth = kth;
    }

    public boolean isKothActive() {
        return isActive;
    }

    public void setKothActive(boolean status) {
        isActive = status;
    }

    public void startTimer() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Koth topAreaFromLoop : GreenKOTH.kothList.values()) {
                if (!topAreaFromLoop.isThereAKing()) {
                    continue;
                }
                Player winner = Bukkit.getPlayer(UUID.fromString(topAreaFromLoop.getKingUUID()));
                for (String command : plugin.getConfig().getStringList("reward-commands")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", winner.getName()));
                }
                for (String broadcastMessage : plugin.languageConfig.getStringList("koth.someone-won")) {
                    CC.broadcast(broadcastMessage.replace("%player%", winner.getName()));
                }
                for (String message : plugin.languageConfig.getStringList("koth.you-won")) {
                    assert winner != null;
                    winner.sendMessage(CC.translate(message));
                }
            }
        }, 0L, 20L * plugin.getConfig().getInt("koth-timer"));
    }
}
