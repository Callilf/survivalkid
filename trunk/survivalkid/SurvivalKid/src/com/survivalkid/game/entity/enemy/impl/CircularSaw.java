package com.survivalkid.game.entity.enemy.impl;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.Life.EnumLife;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.personage.Personage;

public class CircularSaw extends EnemyEntity {

	/**
	 * Create enemy
	 */
	public CircularSaw(Bitmap _bitmap, int _x, int _y, int _nbColums,
			int _nbRows) {
		super("CircularSaw", _bitmap, _x, _y, _nbColums, _nbRows, 10, 3);

		redefineHitBox((sprite.getWidth() * 12) / 100,
				(sprite.getHeight() * 12) / 100,
				(sprite.getWidth() * 80) / 100, (sprite.getHeight() * 80) / 100);
		addAnimation("rotate", new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, 20);
		play("rotate", true, true);
	}

	@Override
	public void collide(GameEntity _gameEntity) {
		if (_gameEntity instanceof Personage) {
			((Personage) _gameEntity).getLife().modifyLife(1, EnumLife.TAKE_DAMAGE);
		}
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub

	}

	public static EnemyEntity generateRandowStartingPosition(
			Point playerPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initRandomPositionAndSpeed(Point playerPosition) {
		// TODO Auto-generated method stub

	}

}