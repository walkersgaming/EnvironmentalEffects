package Environmental.Effects.repeatingEffects.biomeBased;


import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Allows to change the length of ingame days or to set the time to a specific
 * value permanently All of this is only done playerside, so the players time
 * will be async with the server time
 * 
 * @author Max
 *
 */
public class DaytimeModifier extends RepeatingEffectBiomeBased {

	final Long permanentTime; // in ticks
	final boolean shouldTimeChange;
	final Float daySpeed; //multiplier, 1.0 makes for standard 20 min days, 0.5 would be 40 min day,
						 //2.0 10 min etc.
	final long ticksInbetweenRuns;
	long previousRunTime; // in ticks
	long currentRunTime; //in ticks

	public DaytimeModifier(JavaPlugin plugin, Biome biome, Long permanentTime,
			Float daySpeed, long ticksInbetweenRuns) {
		super(plugin,biome);
		this.permanentTime = permanentTime;
		shouldTimeChange = permanentTime != null;
		this.daySpeed = daySpeed;
		this.ticksInbetweenRuns = ticksInbetweenRuns;
		this.currentRunTime = 6000;  //starting time
	}

	public void run() {
		currentPlayers = getCurrentPlayers();

		if (!shouldTimeChange) {
			for (Player p : currentPlayers) {
				if (isPlayerinBiome(p)) {
					p.setPlayerTime(permanentTime, false);
				}

			}
			return;

		}

		currentRunTime = previousRunTime
				+ (long) (ticksInbetweenRuns * daySpeed);
		if (currentRunTime> 23999) {
			currentRunTime -= 24000L;
		}
		for (Player p : currentPlayers) {
			if (isPlayerinBiome(p)) {
				p.setPlayerTime(currentRunTime, false);
			}
		}
		previousRunTime = currentRunTime;
		scheduleNextRun();

	}
	
	public void applyToPlayer(Player p) {
		if (!isPlayerinBiome(p)) {
			return;
		}
		if (!shouldTimeChange) {
			p.setPlayerTime(permanentTime, false);
		}
		else {
			p.setPlayerTime(currentRunTime, false);
		}
	}
	
	/**
	 * Schedules the next run to update rain for all players
	 */
	public void scheduleNextRun() {
		plugin.getServer().getScheduler()
				.scheduleSyncDelayedTask(plugin, this, ticksInbetweenRuns);
	}


}
