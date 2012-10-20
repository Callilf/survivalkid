package com.survivalkid.game.entity.enemy.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import com.survivalkid.R;
import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.enums.StateEnum;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.util.BitmapUtil;
import com.survivalkid.game.util.MoveUtil;

public class Caterpillar extends EnemyEntity {

	private AnimatedSprite deathAnim;
	private boolean dying;

	/**
	 * Create enemy
	 */
	public Caterpillar(Bitmap _bitmap, int _x, int _y, int _nbColums,
			int _nbRows) {
		super("Caterpillar", _bitmap, _x, _y, _nbColums, _nbRows, 5, 0);

		gravity = 2;
		affectedByWalls = false;

		deathAnim = new AnimatedSprite(
				BitmapUtil.createBitmap(R.drawable.dead_anim_small), 0, 0, 7, 1);
		dying = false;
		deathAnim.addAnimation("die", new int[] { 0, 1, 2, 3, 4, 5, 6 }, 25);

		redefineHitBox((sprite.getWidth() * 18) / 100,
				(sprite.getHeight() * 65) / 100,
				(sprite.getWidth() * 74) / 100, (sprite.getHeight() * 35) / 100);
		addAnimation("crawl", new int[] { 0, 1, 2, 3, 2, 1 }, 13);
		play("crawl", true, true);
	}

	@Override
	public void update(long gameTime) {
		if (dying) {
			deathAnim.update(gameTime, direction);
			if (deathAnim.isAnimationFinished()) {
				dead = true;
			}
		}

		super.update(gameTime);

		if (direction == DirectionConstants.LEFT) {
			sprite.offset(-2, 0);
		} else {
			sprite.offset(2, 0);
		}

		if (sprite.getX() < -20
				|| sprite.getX() > MoveUtil.SCREEN_WIDTH + sprite.getWidth()) {
			dead = true;
		}
	}

	@Override
	public void collide(GameEntity _gameEntity) {
		if (_gameEntity instanceof Personage) {
			if (StateEnum.STATE_RECOVERY.equals(((Personage) _gameEntity)
					.getState())) {
				return;
			}

			((Personage) _gameEntity).getLife().looseLife(dammage);
			((Personage) _gameEntity).setState(StateEnum.STATE_RECOVERY);
			die();
		}
		// the method die should be called after x touch (or at the first touch)
	}

	@Override
	public void die() {
		dying = true;

		deathAnim.setX((sprite.getX() + sprite.getWidth() / 2)
				- deathAnim.getWidth() / 2);
		deathAnim.setY((sprite.getY() + sprite.getHeight() / 2)
				- deathAnim.getHeight() / 2);
		deathAnim.play("die", false, true);
	}

	@Override
	public void draw(Canvas canvas) {
		if (dying) {
			deathAnim.draw(canvas, direction);
		} else {
			super.draw(canvas);
		}
	}

	public static EnemyEntity generateRandowStartingPosition(
			Point playerPosition) {
		int random = (int) (Math.random() * 100);
		Caterpillar cat = null;
		if (random < 34) {
			cat = new Caterpillar(
					BitmapUtil.createBitmap(R.drawable.caterpillar), -20, 380,
					4, 1);
			cat.direction = DirectionConstants.RIGHT;
		} else if (random < 69) {
			cat = new Caterpillar(
					BitmapUtil.createBitmap(R.drawable.caterpillar),
					MoveUtil.SCREEN_WIDTH + 20, 380, 4, 1);
			cat.direction = DirectionConstants.LEFT;
		} else if (random < 84) {
			cat = new Caterpillar(
					BitmapUtil.createBitmap(R.drawable.caterpillar), 0, -20, 4,
					1);
			cat.direction = DirectionConstants.RIGHT;
		} else {
			cat = new Caterpillar(
					BitmapUtil.createBitmap(R.drawable.caterpillar),
					MoveUtil.SCREEN_WIDTH, -20, 4, 1);
			cat.direction = DirectionConstants.LEFT;
		}
		return cat;
	}

	@Override
	public void initRandomPositionAndSpeed(Point playerPosition) {

	}

}
