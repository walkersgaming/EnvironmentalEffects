package com.github.maxopoly.datarepresentations;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;

/**
 * Describes a specific state of armour for a player. This is used by the
 * ArmourBasedDamage effect to describe an armour condition, which is wrong and
 * should be punished. Each armour slot has a Boolean, true means if something
 * is in the slot, the player will be punished, false means the player will be
 * punished if nothing is in the slot and null means the slot doesn't matter
 * 
 * @author Max
 *
 */
public class ArmourState {
	private Boolean head;
	private Boolean chest;
	private Boolean pants;
	private Boolean boots;

	public ArmourState(Boolean head, Boolean chest, Boolean pants, Boolean boots) {
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
	 * @return true if the player is wearing "wrong" armour according to this
	 *         instance, false if he is not
	 */
	public boolean isPlayerWearingWrongArmour(Player p) {
		EntityEquipment eq = p.getEquipment();
		if (head == null || (eq.getHelmet() != null) == head) {
			if (chest == null || (eq.getChestplate() != null) == chest) {
				if (pants == null || (eq.getLeggings() != null) == pants) {
					if (boots == null || (eq.getBoots() != null) == boots) {
						return false;
					}
				}
			}
		}
		return true;
	}
}
