package net.multylands.koth.object.events;

import net.multylands.koth.object.Koth;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;


public class KothStartEvent extends KothEvent implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private boolean cancelled = false;
	
	
	public KothStartEvent(Koth koth) {
		super(koth);
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
