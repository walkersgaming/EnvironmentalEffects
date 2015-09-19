package com.github.maxopoly.repeatingEffects;

import org.bukkit.Location;
import org.bukkit.block.Biome;

import com.github.maxopoly.exceptions.ConfigParseException;

/**
 * Used to describe an area for an effect, does not take y-level into account.
 * Additional shapes may be added to the shape enum if they are also implemented
 * in the isInRange method
 * 
 * @author Max
 *
 */
public class Area {
	public enum Shape {
		RECTANGLE, CIRCLE, BIOME, GLOBAL;
	}

	private Biome biome;
	private Location center;
	private Shape shape;
	private int xSize;
	private int zSize;

	/**
	 * Constructor for biome based areas
	 * 
	 * @param shape
	 *            must be Shape.BIOME
	 * @param biome
	 *            biome describing the area
	 * @throws ConfigParseException
	 *             In case this constructor is used with a shape other than
	 *             BIOME
	 */
	public Area(Shape shape, Biome biome) throws ConfigParseException {
		this.shape = shape;
		this.biome = biome;
		if (shape != Shape.BIOME) {
			throw new ConfigParseException();
		}
	}

	/**
	 * Constructor for global areas
	 * 
	 * @param shape
	 *            always Shape.GLOBAL
	 */
	public Area(Shape shape) throws ConfigParseException {
		if (shape != Shape.GLOBAL) {
			throw new ConfigParseException();
		}
		this.shape = shape;
	}

	/**
	 * Constructor for a geometrical shape whichs length and width are the same
	 * 
	 * @param shape
	 *            geometrical shape
	 * @param center
	 *            location on which the shape is centred
	 * @param size
	 *            half of the diameter of the shape
	 * @throws ConfigParseException
	 *             thrown if the given shape is a biome or global, those cant
	 *             have given sizes
	 */
	public Area(Shape shape, Location center, int size)
			throws ConfigParseException {
		this(shape, center, size, size);
	}

	/**
	 * Constructor for a geometrical shape whichs length and width are different
	 * 
	 * @param shape
	 *            geometrical shape
	 * @param center
	 *            location on which the shape is centred
	 * @param xSize
	 *            half of the diameter of the shape in x-direction/east-west
	 * @param zSize
	 *            half of the diameter of the shape in y-direction/north-south
	 * @throws ConfigParseException
	 *             thrown if this constructor is used with a global or biome
	 *             shape, because those cant have given sizes
	 */
	public Area(Shape shape, Location center, int xSize, int zSize)
			throws ConfigParseException {
		if (shape == Shape.BIOME || shape == Shape.GLOBAL) {
			throw new ConfigParseException();
		}
		this.shape = shape;
		this.center = center;
		this.xSize = xSize;
		this.zSize = zSize;
	}

	/**
	 * Checks whether the given location is inside this area
	 * 
	 * @param loc
	 *            location be compared to this area
	 * @return true if the location is inside the area, false if not
	 */
	public boolean isInArea(Location loc) {
		switch (shape) {
		case GLOBAL:
			return true;
		case BIOME:
			return loc.getBlock().getBiome() == biome;
		case RECTANGLE:
			return xSize <= getXDifference(loc) && zSize <= getZDifference(loc);
		case CIRCLE:
			return getXZDistance(loc) <= xSize;
		default:
			return false; // hopefully never gonna happen

		}
	}

	// these methods are not safe, calling them when the area is actually
	// not a geometrical shape with x and z will throw exceptions
	private double getXZDistance(Location loc) {
		double x = getXDifference(loc);
		double z = getZDifference(loc);
		return Math.sqrt(x * x + z * z);
	}

	private double getXDifference(Location loc) {
		return Math.abs(loc.getX() - center.getX());
	}

	private double getZDifference(Location loc) {
		return Math.abs(loc.getZ() - center.getZ());
	}

}
