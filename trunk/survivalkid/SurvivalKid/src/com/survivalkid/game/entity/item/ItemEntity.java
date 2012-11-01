package com.survivalkid.game.entity.item;

import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.GameEntity;

public abstract class ItemEntity extends GameEntity {

	protected int power;
	
	/**
	 * 
	 * Constructor called by parents class
	 * 
	 * @param _name name of the entity
	 * @param spriteEnum the sprite
	 * @param x position x initial
	 * @param y position y initial
	 * @param _dammage how much the perso loose life when he hit the enemy
	 * @param _difficulty the index of difficulty of the enemy
	 */
	public ItemEntity(String _name, SpriteEnum spriteEnum, int _x, int _y, int _power) {
		super(_name, spriteEnum, _x, _y);
		power = _power;
	}
}
