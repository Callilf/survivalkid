package com.survivalkid.game.entity.enemy.impl;

import android.graphics.Canvas;
import android.graphics.Point;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.Life.EnumLife;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.util.BitmapUtil;
import com.survivalkid.game.util.MoveUtil;

public class Caterpillar extends EnemyEntity {

	private AnimatedSprite deathAnim;
	private boolean dying;

	/** Default constructor. */
	public Caterpillar() {
		super("Caterpillar", BitmapUtil.CATERPILLAR, 0, 0, 4, 1, 5, 0);
	};

	/** Initialize the enemy. */
	private void init() {
		gravity = 2;
		affectedByWalls = false;

		deathAnim = new AnimatedSprite(BitmapUtil.SMOKE_WHITE_SMALL, 0, 0, 7, 1);
		dying = false;
		deathAnim.addAnimation("die", new int[] { 0, 1, 2, 3, 4, 5, 6 }, 25);

		redefineHitBox((sprite.getWidth() * 18) / 100, (sprite.getHeight() * 65) / 100, (sprite.getWidth() * 74) / 100,
				(sprite.getHeight() * 35) / 100);
		addAnimation("crawl", new int[] { 0, 1, 2, 3, 2, 1 }, 13);
		play("crawl", true, true);
	}

	@Override
	public void update(long gameTime) {
		if (dying) {
			deathAnim.update(gameTime, DirectionConstants.RIGHT);
			if (deathAnim.isAnimationFinished()) {
				dead = true;
			}
			return;
		}

		super.update(gameTime);

		if (direction == DirectionConstants.LEFT) {
			sprite.offset(-2, 0);
		} else {
			sprite.offset(2, 0);
		}

		if (sprite.getX() < -20 || sprite.getX() > MoveUtil.SCREEN_WIDTH + sprite.getWidth()) {
			dead = true;
		}
	}

	@Override
	public void collide(GameEntity _gameEntity) {
		if (!dying) {
			if (_gameEntity instanceof Personage) {
				((Personage) _gameEntity).takeDamage(dammage, EnumLife.TAKE_DAMAGE);
				die();
			}
		}
		// the method die should be called after x touch (or at the first touch)
	}

	@Override
	public void die() {
		dying = true;

		deathAnim.setX((sprite.getX() + sprite.getWidth() / 2) - deathAnim.getWidth() / 2);
		deathAnim.setY((sprite.getY() + sprite.getHeight() / 2) - deathAnim.getHeight() / 2);
		deathAnim.play("die", false, true);
	}

	@Override
	public void draw(Canvas canvas) {
		if (dying) {
			deathAnim.draw(canvas, DirectionConstants.RIGHT);
		} else {
			super.draw(canvas);
		}
	}

	/**
	 * Initialize the position, speed and misc. attributes of the enemy.
	 */
	@Override
	public void initRandomPositionAndSpeed(Point playerPosition) {
		// random spawn position
		int random = (int) (Math.random() * 100);
		if (random < 34) {
			sprite.setX(-20);
			sprite.setY(380);
			direction = DirectionConstants.RIGHT;
		} else if (random < 69) {
			sprite.setX(MoveUtil.SCREEN_WIDTH + 20);
			sprite.setY(380);
			direction = DirectionConstants.LEFT;
		} else if (random < 84) {
			sprite.setX(0);
			sprite.setY(-20);
			direction = DirectionConstants.RIGHT;
		} else {
			sprite.setX(MoveUtil.SCREEN_WIDTH);
			sprite.setY(-20);
			direction = DirectionConstants.LEFT;
		}

		init();
	}

}
