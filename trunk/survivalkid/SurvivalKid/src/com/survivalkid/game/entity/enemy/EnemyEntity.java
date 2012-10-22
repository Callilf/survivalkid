package com.survivalkid.game.entity.enemy;

import android.graphics.Canvas;
import android.graphics.Point;

import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.GameEntity;

public abstract class EnemyEntity extends GameEntity {

	protected int dammage;
	protected int difficulty;


	public EnemyEntity(String _name, SpriteEnum spriteEnum, int _x, int _y, int _dammage, int _difficulty) {
		super(_name, spriteEnum, _x, _y);
		dammage = _dammage;
		difficulty = _difficulty;
	}

	public EnemyEntity() {
		super();
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	}

	public abstract void initRandomPositionAndSpeed(Point playerPosition);

}
