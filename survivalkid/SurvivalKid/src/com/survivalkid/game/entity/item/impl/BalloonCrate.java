package com.survivalkid.game.entity.item.impl;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import com.survivalkid.game.core.Animation;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.item.ItemEntity;
import com.survivalkid.game.util.MoveUtil;

public class BalloonCrate extends ItemEntity {
	
	private Rect balloonTouchBox;
	
	private boolean falling = false;
	private boolean releaseObject = false;
	
	/** Default constructor. */
	public BalloonCrate() {
		super("Balloon", SpriteEnum.BALLOON_CRATE, 0, 0, 0);
	};
	
	/** Default constructor. */
	public BalloonCrate(int _x, int _y) {
		super("Balloon", SpriteEnum.BALLOON_CRATE, _x, _y, 0);
		init();
	};

	/** Initialize the enemy. */
	private void init() {
		timeToLive = 15000;
		
		gravity = 0;
		affectedByFloor = true;
		affectedByCeiling = false;
		affectedByWalls = false;
		
		redefineHitBox((sprite.getWidth() * 30) / 100, (sprite.getHeight() * 65) / 100, (sprite.getWidth() * 36) / 100,
				(sprite.getHeight() * 30) / 100);
		
		setBalloonTouchBox(new Rect(sprite.getX() + (sprite.getWidth() * 30) / 100, sprite.getY() + (sprite.getHeight() * 5) / 100, sprite.getX() + (sprite.getWidth() * 30) / 100 + (sprite.getWidth() * 36) / 100,
				sprite.getY() + (sprite.getHeight() * 5) / 100 + (sprite.getHeight() * 45) / 100));
		
		new Animation("test", new int[] { 2}, 8);
		
		play("swing", true, true);
	}

	@Override
	public void update(long gameTime) {		
		if(falling) {
			stop();
			gravity = 1;
		}
		
		setBalloonTouchBox(new Rect(sprite.getX() + (sprite.getWidth() * 30) / 100, sprite.getY() + (sprite.getHeight() * 5) / 100, sprite.getX() + (sprite.getWidth() * 30) / 100 + (sprite.getWidth() * 36) / 100,
				sprite.getY() + (sprite.getHeight() * 5) / 100 + (sprite.getHeight() * 45) / 100));
		
		if(onFloor()) {
			releaseObject = true;
			dead = true;
		}
		
		super.update(gameTime);
	}

	@Override
	public void die() {
		dead = true;
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	}

	
	public void touched() {
		falling = true;
		setSpeedX(0);
	}
	
	
	
	
	
	/**
	 * Initialize the position, speed and misc. attributes of the enemy.
	 */
	@Override
	public void initRandomPositionAndSpeed(Point playerPosition) {
		
		int randomSide = (int) (Math.random() * 2);
		int randomSpeed =  (int) (Math.random() * 3);
		
		int randomY = 20 + (int) (Math.random() * MoveUtil.GROUND - sprite.getHeight() - 20);
		sprite.setY(randomY);
		
		if(randomSide<1) {
			//LEFT
			sprite.setX(-sprite.getWidth());
			setSpeedX(4 + randomSpeed);
		} else {
			//RIGHT
			sprite.setX(MoveUtil.SCREEN_WIDTH);
			setSpeedX(-4 - randomSpeed);
		}
		
		
		if(sprite.getY() < MoveUtil.GROUND/2) {
			float randomFallSpeed = 1;
			setSpeedY(randomFallSpeed);
		} else {
			float randomFallSpeed = 1;
			setSpeedY(-randomFallSpeed);
		}


		init();
	}

	@Override
	public void collide(GameEntity _gameEntity) {
//		if (_gameEntity instanceof Personage) {
//			((Personage) _gameEntity).revoceryLife(30, EnumLife.RECOVERY_LIFE);
//			die();
//		}
	}

	/**
	 * @return the balloonTouchBox
	 */
	public Rect getBalloonTouchBox() {
		return balloonTouchBox;
	}

	/**
	 * @param balloonTouchBox the balloonTouchBox to set
	 */
	public void setBalloonTouchBox(Rect balloonTouchBox) {
		this.balloonTouchBox = balloonTouchBox;
	}

	/**
	 * @return the releaseObject
	 */
	public boolean isReleaseObject() {
		return releaseObject;
	}

}
