package com.survivalkid.game.entity.item;

import android.graphics.Point;

import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.singleton.GameContext;

public abstract class ItemEntity extends GameEntity {

	protected int power;
	protected long spawnTime;
	protected int timeToLive;

	private boolean blinking;
	private long blinkBeginTime;
	private int blinkDuration = 3000;

	/**
	 * 
	 * Constructor called by parents class
	 * 
	 * @param _name
	 *            name of the entity
	 * @param spriteEnum
	 *            the sprite
	 * @param x
	 *            position x initial
	 * @param y
	 *            position y initial
	 * @param _dammage
	 *            how much the perso loose life when he hit the enemy
	 * @param _difficulty
	 *            the index of difficulty of the enemy
	 */
	public ItemEntity(String _name, SpriteEnum spriteEnum, int _x, int _y, int _power) {
		super(_name, spriteEnum, _x, _y);
		power = _power;
		spawnTime = GameContext.getSingleton().getCurrentTimeMillis();
		blinking = false;
	}

	@Override
	public void update(long gameTime) {
		super.update(gameTime);

		if (!blinking) {
			if (gameTime - spawnTime > timeToLive) {
				blinking = true;
				blinkBeginTime = gameTime;
			}
		} else {
			sprite.setRecovery(true);
			if (gameTime - blinkBeginTime > blinkDuration) {
				dead = true;
			}
		}
	}

	public abstract void initRandomPositionAndSpeed(Point playerPosition);
}
