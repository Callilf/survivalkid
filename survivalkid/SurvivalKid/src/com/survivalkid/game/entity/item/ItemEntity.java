package com.survivalkid.game.entity.item;

import android.graphics.Rect;

import com.survivalkid.game.entity.GameEntity;

public abstract class ItemEntity extends GameEntity {

	protected int power;
	
	public ItemEntity(String _name, Rect _sprite, int _power) {
		super(_name, _sprite);
		power = _power;
	}

}
