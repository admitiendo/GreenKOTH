package net.multylands.koth.manager;

import net.multylands.koth.object.Koth;
import net.multylands.koth.utils.chat.CC;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.awt.List;
import java.util.*;

public class KothCreationManager {
    public HashMap<Player, Koth> createList = new HashMap<>();
    public ArrayList<Player> kothNameSetted = new ArrayList<>();
}
