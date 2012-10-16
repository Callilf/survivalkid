package com.survivalkid.game.entity.enemy;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.survivalkid.game.entity.GameEntity;

public abstract class EnemyEntity extends GameEntity {

	protected int dammage;
	protected int difficulty;

	public EnemyEntity(String _name, Bitmap _sprite, int _x, int _y,
			int _nbColumns, int _nbRows, int _dammage, int _difficulty) {
		super(_name, _sprite, _x, _y, _nbColumns, _nbRows);
		dammage = _dammage;
		difficulty = _difficulty;
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	}

}
