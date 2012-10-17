package com.survivalkid.game.entity.enemy.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.survivalkid.R;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.util.BitmapUtil;
import com.survivalkid.game.util.MoveUtil;

public class Caterpillar extends EnemyEntity {

	/**
	 * Create enemy
	 */
	public Caterpillar(Bitmap _bitmap, int _x, int _y, int _nbColums,
			int _nbRows) {
		super("Caterpillar", _bitmap, _x, _y, _nbColums, _nbRows, 10, 3);

		affectedByWalls = false;
		
		redefineHitBox((sprite.getWidth() * 18) / 100,
				(sprite.getHeight() * 65) / 100,
				(sprite.getWidth() * 74) / 100, (sprite.getHeight() * 35) / 100);
		addAnimation("crawl", new int[] { 0, 1, 2, 3, 2, 1 }, 13);
		play("crawl", true, true);
	}

	@Override
	public void update(long gameTime) {
		super.update(gameTime);

		if (direction == DirectionConstants.LEFT) {
			sprite.offset(-2, 0);
		} else {
			sprite.offset(2, 0);
		}
		
		if(sprite.getX() < -20 || sprite.getX() > MoveUtil.SCREEN_WIDTH + sprite.getWidth()) {
			dead = true;
		}
	}

	@Override
	public void collide(GameEntity _gameEntity) {
		if (_gameEntity instanceof Personage) {
			((Personage) _gameEntity).getLife().looseLife(1);
		}
		// the method die should be called after x touch (or at the first touch)
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub

	}

	public static EnemyEntity generateRandowStartingPosition(
			Point playerPosition) {
		int random =  (int) (Math.random() * 100);
		Caterpillar cat = null;
		if(random<49) {
			cat = new Caterpillar(BitmapUtil.createBitmap(R.drawable.caterpillar),
					-20, 380, 4, 1);
			cat.direction = DirectionConstants.RIGHT;
		} else {
			cat = new Caterpillar(BitmapUtil.createBitmap(R.drawable.caterpillar),
					MoveUtil.SCREEN_WIDTH + 20, 380, 4, 1);
			cat.direction = DirectionConstants.LEFT;
		}
		return cat;
	}

}
