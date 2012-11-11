package com.survivalkid.game.entity.enemy.impl;

import android.graphics.Point;
import android.util.Log;

import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.GameEntity;
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
		
	/**
	 * Constructor
	 */
	public FireGround() {
		super("FireGround", SpriteEnum.FIRE_GROUND, 0, MoveUtil.GROUND, 2, 1);
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
	public void applyCollision(GameEntity _gameEntity) {
		if (_gameEntity instanceof Personage) {
			((Personage) _gameEntity).takeDamage(dammage, EnumLife.TAKE_DAMAGE, RECOVERY_TIME);
		}
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
		dead = true;
	}
	
	@Override
	public void update(long gameTime) {
		super.update(gameTime);
		if (GameContext.getSingleton().gameDuration > endTime) {
			dead = true;
		}
	}


}
