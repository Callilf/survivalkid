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

public class Bull extends EnemyEntity {

	private static final int VITESSE_BULL = 15;
	private static final int SPEED_COLLISION_X = 30;
	private static final int SPEED_COLLISION_Y = -30;

	private AnimatedSprite deathAnim;
	
	/** Default constructor. */
	public Bull() {
		super("Bull", SpriteEnum.BULL, 0, 0, 30, 0);
		attack = DEFAULT_DEFENSE + 2;
		defense = DEFAULT_DEFENSE + 1;
	};

	/** Initialize the enemy. */
	private void init() {
		gravity = 2;
		affectedByWalls = false;

		redefineHitBox((sprite.getWidth() * 25) / 100, (sprite.getHeight() * 20) / 100, (sprite.getWidth() * 70) / 100,
				(sprite.getHeight() * 80) / 100);
		play("run", true, true);
		deathAnim = new AnimatedSprite(SpriteEnum.SMOKE_WHITE_SMALL, 0, 0);

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

		if (sprite.getX() + sprite.getWidth() < 0 || sprite.getX() > MoveUtil.SCREEN_WIDTH) {
			dead = true;
		}
		
		super.update(gameDuration);
	}

	@Override
	public void applyCollisionCharacter(Personage _personage) {
		_personage.takeDamage(dammage, EnumLife.TAKE_DAMAGE);
		int newSpeedX = (direction == DirectionConstants.LEFT)? -SPEED_COLLISION_X : SPEED_COLLISION_X;
		_personage.setSpeedX(newSpeedX);
		_personage.setSpeedY(SPEED_COLLISION_Y);
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
		if (random < 50) {
			sprite.setX(-sprite.getWidth());
			sprite.setY(380);
			direction = DirectionConstants.RIGHT;
		} 
		else {
			sprite.setX(MoveUtil.SCREEN_WIDTH + sprite.getWidth());
			sprite.setY(380);
			direction = DirectionConstants.LEFT;
		}
			
		
		// init speed
		setSpeedY(0);
		if (direction == DirectionConstants.LEFT) {
			setSpeedX(-VITESSE_BULL);
		} else {
			setSpeedX(VITESSE_BULL);
		}

		init();
	}

}
