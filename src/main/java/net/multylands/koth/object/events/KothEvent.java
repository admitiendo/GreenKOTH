package net.multylands.koth.object.events;

import net.multylands.koth.object.Koth;
import org.bukkit.event.Event;


public abstract class KothEvent extends Event {
	
	protected Koth koth;
	
	public KothEvent(Koth koth) {
		this.koth = koth;
	}
	
	
	public Koth getKoth() {
		return koth;
	}
	
}
