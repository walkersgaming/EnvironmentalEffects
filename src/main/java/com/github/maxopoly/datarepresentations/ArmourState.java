package com.github.maxopoly.datarepresentations;

import java.util.LinkedList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 * Describes a specific state of armour for a player. This is used by the
 * ArmourBasedDamage effect to describe an armour condition, which is wrong and
 * should be punished. Each armour slot has a list of the enum ArmourType to
 * describe which armour states should be punished.
 * 
 * @author Max
 *
 */
public class ArmourState {
	/**
	 * Describes the possible types of armour. New constants may be added to
	 * this if they are implemented in the isPlayerWearingArmour(Player p)
	 * method
	 * 
	 * @author Max
	 *
	 */
	public enum ArmourType {
		DIAMOND, GOLD, IRON, CHAINMAIL, LEATHER, NONE, ANY
	}

	private LinkedList<ArmourType> head;
	private LinkedList<ArmourType> chest;
	private LinkedList<ArmourType> pants;
	private LinkedList<ArmourType> boots;

	public ArmourState(LinkedList<ArmourType> head,
			LinkedList<ArmourType> chest, LinkedList<ArmourType> pants,
			LinkedList<ArmourType> boots) {
		this.head = head;
		this.chest = chest;
		this.pants = pants;
		this.boots = boots;
	}

	/**
	 * Gets whether the players current armour status is wrong according to this
	 * instance, this returns true, if only a single slot is wrong
	 * 
	 * @param p
	 *            Player to check
	 * @return true if any slot is wrong and the player should be punished,
	 *         false if not.
	 */
	public boolean isPlayerWearingWrongArmour(Player p) {
		EntityEquipment eq = p.getEquipment();
		return checkSlot(head, eq.getHelmet())
				|| checkSlot(chest, eq.getChestplate())
				|| checkSlot(pants, eq.getLeggings())
				|| checkSlot(boots, eq.getBoots());
	}

	private boolean checkSlot(LinkedList<ArmourType> slot, ItemStack equipped) {
		if (slot == null) {
			return false;
		}
		if (equipped != null && slot.contains(ArmourType.ANY)) {
			return true;
		}
		if (equipped == null) {
			return slot.contains(ArmourType.NONE) ? true : false;
		}
		String matString = equipped.getType().toString();
		if (materialCheck(slot, ArmourType.LEATHER, matString)) {
			return true;
		}
		if (materialCheck(slot, ArmourType.CHAINMAIL, matString)) {
			return true;
		}
		if (materialCheck(slot, ArmourType.GOLD, matString)) {
			return true;
		}
		if (materialCheck(slot, ArmourType.IRON, matString)) {
			return true;
		}
		if (materialCheck(slot, ArmourType.DIAMOND, matString)) {
			return true;
		}
		return false;
	}

	private boolean materialCheck(LinkedList<ArmourType> slot, ArmourType at,
			String matString) {
		return (slot != null && slot.contains(at) && matString.startsWith(at
				.toString()));
	}
}