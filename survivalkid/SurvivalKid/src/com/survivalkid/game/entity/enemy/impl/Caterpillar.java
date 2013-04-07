package com.survivalkid.game.entity.enemy.impl;

import android.graphics.Canvas;
import android.graphics.Point;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.Life.EnumLife;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.util.MoveUtil;

public class Caterpillar extends EnemyEntity {

	private static final float CATERPILLAR_LOWEST_SPEED = 1.8f;
	private static final float CATERPILLAR_HIGHEST_SPEED = 2.8f;
	
	/** duration of the recovery when the player is hit */
	private static final int RECOVERY_TIME_NORMAL = 300;
	private static final int RECOVERY_TIME_PURPLE = 500;
	
	private AnimatedSprite deathAnim;
	
	/** Default constructor. */
	public Caterpillar() {
		super("Caterpillar", SpriteEnum.CATERPILLAR, 0, 0, 5, 0);
		description = "Its crawls more or less slowly, but isn't very dangerous. Take care of the violet ones though.";
	};

	/** Initialize the enemy. */
	private void init() {
		gravity = 2;
		affectedByWalls = false;

		deathAnim = new AnimatedSprite(SpriteEnum.SMOKE_WHITE_SMALL, 0, 0);

		redefineHitBox((sprite.getWidth() * 18) / 100, (sprite.getHeight() * 65) / 100, (sprite.getWidth() * 74) / 100,
				(sprite.getHeight() * 35) / 100);
		play("crawl", true, true);
	}

	@Override
	public void update(long gameDuration) {
		if (dying) {
			deathAnim.update(gameDuration, DirectionConstants.RIGHT);
			if (deathAnim.isAnimationFinished()) {
				dead = true;
			}
			return;
		}

		if (sprite.getX() + sprite.getWidth() < MoveUtil.BACKGROUND_LEFT || sprite.getX() > MoveUtil.BACKGROUND_RIGHT + sprite.getWidth()) {
			dead = true;
		}
		
		super.update(gameDuration);
	}

	@Override
	public void applyCollisionCharacter(Personage _personage) {
		int recovery = (dammage > 5)? RECOVERY_TIME_PURPLE : RECOVERY_TIME_NORMAL;
		if (_personage.takeDamage(dammage, EnumLife.TAKE_DAMAGE, recovery)) {
			die();
		}
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
		float speed = (float) (Math.random() * (CATERPILLAR_HIGHEST_SPEED - CATERPILLAR_LOWEST_SPEED) + CATERPILLAR_LOWEST_SPEED);
		if(speed >= 2.5f) {
			sprite = new AnimatedSprite(SpriteEnum.CATERPILLAR_PURPLE);
			dammage = 10;
		}
		
		// random spawn position
		int random = (int) (Math.random() * 100);
		if (random < 34) {
			sprite.setX(MoveUtil.BACKGROUND_LEFT - sprite.getWidth());
			sprite.setY(MoveUtil.GROUND - sprite.getHeight());
			direction = DirectionConstants.RIGHT;
		} else if (random < 69) {
			sprite.setX(MoveUtil.BACKGROUND_RIGHT + sprite.getWidth());
			sprite.setY(MoveUtil.GROUND - sprite.getHeight());
			direction = DirectionConstants.LEFT;
		} else if (random < 84) {
			sprite.setX(MoveUtil.BACKGROUND_LEFT - sprite.getWidth());
			sprite.setY(MoveUtil.BACKGROUND_TOP - 20);
			direction = DirectionConstants.RIGHT;
		} else {
			sprite.setX(MoveUtil.BACKGROUND_RIGHT + sprite.getWidth());
			sprite.setY(MoveUtil.BACKGROUND_TOP - 20);
			direction = DirectionConstants.LEFT;
		}
		
		// init speed
		setSpeedY(0);
		if (direction == DirectionConstants.LEFT) {
			setSpeedX(-speed);
		} else {
			setSpeedX(speed);
		}

		init();
	}

}
