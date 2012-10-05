package com.survivalkid.game.entity.item;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.entity.GameEntity;

public abstract class ItemEntity extends GameEntity {

	protected int power;
	
	public ItemEntity(String _name, AnimatedSprite _sprite, int _power) {
		super(_name, _sprite);
		power = _power;
	}

}
