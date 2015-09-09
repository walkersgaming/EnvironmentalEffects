package Environmental.Effects.repeatingEffects.locationBased;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import Environmental.Effects.repeatingEffects.RepeatingEffect;

public abstract class RepeatingEffectLocationBased extends RepeatingEffect{
	public enum Shape {
		RECTANGULAR, CIRCLE;
	}

	Shape shape;
	double sizeX;
	double sizeY;
	Location center;

	public RepeatingEffectLocationBased(JavaPlugin plugin, Shape shape, Double size,
			Location center) {
		this(plugin,shape, size, size, center);
	}

	public RepeatingEffectLocationBased(JavaPlugin plugin,Shape shape, Double sizeX,
			Double sizeY, Location center) {
		super(plugin);
		this.shape = shape;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.center = center;
	}

	public boolean isInRange(Location loc) {
		switch (shape) { // switch because we are optimistic for future
							// expansion
		case RECTANGULAR:
			return sizeX <= getXDifference(loc) && sizeY <= getYDifference(loc);
		case CIRCLE:
			return getXYDistance(loc) <= sizeX;
		default:
			return false; //hopefully never gonna happen

		}
	}

	public double getXYDistance(Location loc) {
		double x = getXDifference(loc);
		double y = getYDifference(loc);
		return Math.sqrt(x * x + y * y);
	}

	public double getXDifference(Location loc) {
		return Math.abs(loc.getX() - center.getX());
	}

	public double getYDifference(Location loc) {
		return Math.abs(loc.getY() - center.getY());
	}

}
