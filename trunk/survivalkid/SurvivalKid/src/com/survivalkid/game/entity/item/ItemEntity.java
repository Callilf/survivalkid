package com.survivalkid.game.entity.item;

import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.item.impl.BalloonCrate;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.util.MoveUtil;

public abstract class ItemEntity extends GameEntity {
	
	private static final String TAG = ItemEntity.class.getSimpleName();

	protected int power;
	protected long spawnTime;
	protected int timeToLive;

	private boolean blinking;
	private long blinkBeginTime;
	private int blinkDuration = 3000;
	
	//Attributes for the balloon mode
	private boolean inBalloon = false;
	private BalloonCrate balloon;

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
		if(inBalloon) {
			balloon.update(gameTime);
			
			if(balloon.isReleaseObject()) {
				setX(balloon.getSprite().getX() + balloon.getSprite().getWidth()/2 - sprite.getWidth()/2);
				setY(MoveUtil.GROUND - sprite.getHeight());
				spawnTime = GameContext.getSingleton().getCurrentTimeMillis();
				blinking = false;
				inBalloon = false;
			}
			
			return;
		}
		
		
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
	
	
	@Override
	public void draw(Canvas canvas){
		if(inBalloon) {
			balloon.draw(canvas);
		} else {
			super.draw(canvas);
		}
	}
	

	public abstract void initRandomPositionAndSpeed(Point playerPosition);

	public boolean isInBalloon() {
		return inBalloon;
	}

	public void setInBalloon(boolean inBalloon) {
		this.inBalloon = inBalloon;
		if(inBalloon) {
			balloon = new BalloonCrate();
			balloon.initRandomPositionAndSpeed(null);
		}
	}
	
	public BalloonCrate getBalloon() {
		return balloon;
	}
	
	public void balloonTouched() {
		if(!balloon.isPierced()) {
			Log.w(TAG, "balloon touched");
			balloon.touched();
		}
	}
}
