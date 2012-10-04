package com.survivalkid.game.entity.enemy;

import android.graphics.Rect;

import com.survivalkid.game.entity.GameEntity;

public abstract class EnemyEntity extends GameEntity {

	protected int dammage;
	protected int difficulty;
	
	public EnemyEntity(String _name, Rect _sprite, int _dammage, int _difficulty) {
		super(_name, _sprite);
		dammage = _dammage;
		difficulty = _difficulty;
	}
	
}
