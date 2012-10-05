package com.survivalkid.game.entity.enemy;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.entity.GameEntity;

public abstract class EnemyEntity extends GameEntity {

	protected int dammage;
	protected int difficulty;
	
	public EnemyEntity(String _name, AnimatedSprite _sprite, int _dammage, int _difficulty) {
		super(_name, _sprite);
		dammage = _dammage;
		difficulty = _difficulty;
	}
	
}
