package com.survivalkid.game.entity.enemy;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import com.survivalkid.game.entity.GameEntity;

public abstract class EnemyEntity extends GameEntity {

	protected int dammage;
	protected int difficulty;

	/**
	 * 
	 * Constructor called by parents class
	 * 
	 * @param _name name of the entity
	 * @param bitmap image of the entity
	 * @param x position x initial
	 * @param y position y initial
	 * @param nbColum number of column in the sprite
	 * @param nbRows number of line in the sprite
	 * @param _dammage how much the perso loose life when he hit the enemy
	 * @param _difficulty the index of difficulty of the enemy
	 */
	public EnemyEntity(String _name, Bitmap _sprite, int _x, int _y,
			int _nbColumns, int _nbRows, int _dammage, int _difficulty) {
		super(_name, _sprite, _x, _y, _nbColumns, _nbRows);
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
