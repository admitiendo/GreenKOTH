package net.multylands.koth.object.events;

import net.multylands.koth.object.Koth;
import org.bukkit.entity.Player;


public abstract class PlayerKothEvent extends KothEvent {
	
	private Player player;
	
	public PlayerKothEvent(Player player, Koth koth) {
		super(koth);
		
		this.player = player;
	}
	
	
	public Player getPlayer() {
		return player;
	}
	
}
