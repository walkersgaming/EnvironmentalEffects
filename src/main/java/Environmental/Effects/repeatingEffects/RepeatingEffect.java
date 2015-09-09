package Environmental.Effects.repeatingEffects;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class RepeatingEffect implements Runnable{
	protected JavaPlugin plugin;
	protected Collection<? extends Player> currentPlayers;
	
	public RepeatingEffect(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	public abstract void applyToPlayer(Player p);
	
	public Collection<? extends Player> getCurrentPlayers() {
		return plugin.getServer().getOnlinePlayers();
	}
	

}
