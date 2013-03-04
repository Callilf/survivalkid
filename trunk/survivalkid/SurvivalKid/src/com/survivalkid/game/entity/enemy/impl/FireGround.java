package com.survivalkid.game.entity.enemy.impl;

import android.graphics.Canvas;
import android.graphics.Point;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.Life.EnumLife;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.util.MoveUtil;

public class FireGround extends EnemyEntity {

	/** nominal duration in ms of the fire */
	private static final int INDEX_TIMELIFE = 3000;
	
	/** duration of the recovery when the player is hit */
	private static final int RECOVERY_TIME = 200;
	
	/** Speed of the fall*/
	private static final int FALL = 2;
	
	/** time of the dead of the fire */
	private long endTime;
	
	/** The death anim. */
	private AnimatedSprite deathAnim;
		
	/**
	 * Constructor
	 */
	public FireGround() {
		super("FireGround", SpriteEnum.FIRE_GROUND, 0, MoveUtil.GROUND, 2, 1);
		
		deathAnim = new AnimatedSprite(SpriteEnum.FIRE_GROUND, 0, 0);
	}
	
	public void place(int _x, int _y) {
		setX(_x);
		setY(_y);
		redefineHitBox((sprite.getWidth() * 15) / 100,
				(sprite.getHeight() * 65) / 100,
				(sprite.getWidth() * 75) / 100,
				(sprite.getHeight() * 30) / 100);
	}
	
	@Override
	public void applyCollisionCharacter(Personage _personage) {
			_personage.takeDamage(dammage, EnumLife.TAKE_DAMAGE, RECOVERY_TIME);
	}

	@Override
	public void initRandomPositionAndSpeed(Point playerPosition) {
		// no random position neither speed
		
		// multiply INDEX_TIMELIFE by a number between 0.8 and 1.2
		long timeLife = (long) (((Math.random()/2.5)+0.8)*INDEX_TIMELIFE);
		endTime = timeLife + GameContext.getSingleton().gameDuration;
		setSpeedY(FALL);
		play("fire", true, true);
	}

	@Override
	public void die() {
		dying = true;

		deathAnim.setX((sprite.getX() + sprite.getWidth() / 2) - deathAnim.getWidth() / 2);
		deathAnim.setY((sprite.getY() + sprite.getHeight() / 2) - deathAnim.getHeight() / 2);
		deathAnim.play("faint", false, true);
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
		
		super.update(gameDuration);
		if (GameContext.getSingleton().gameDuration > endTime) {
			die();
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (dying) {
			deathAnim.draw(canvas, DirectionConstants.RIGHT);
		} else {
			super.draw(canvas);
		}
	}


}
