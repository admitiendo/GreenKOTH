package net.multylands.koth.object.events;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.multylands.koth.object.Koth;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerCaptureKothEvent extends PlayerKothEvent implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private boolean cancelled = false;
	
	private List<String> commands;
	private Map<String, Double> nonItemLoot;
	
	
	public PlayerCaptureKothEvent(Player player, Koth koth, List<String> commands) {
		super(player, koth);
		
	    this.commands = commands;
	}
	
	
		public List<String> getCommands() {
	    return Collections.unmodifiableList(commands);
	}

	public void setCommands(List<String> commands) {
	    this.commands = commands;
	}

	public void addCommand(String cmd) {
		commands.add(cmd);
	}

	public void removeLoot(String cmd) {
		commands.remove(cmd);
	}
	

	public boolean isCancelled() {
		return cancelled;
	}
    
	public void setCancelled(boolean cancelled) {
	    this.cancelled = cancelled;
	}
    
    
    @Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
    
    
}
