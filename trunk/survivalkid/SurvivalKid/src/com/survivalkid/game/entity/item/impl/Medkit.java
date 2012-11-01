package com.survivalkid.game.entity.item.impl;

import android.graphics.Canvas;

import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.Life.EnumLife;
import com.survivalkid.game.entity.item.ItemEntity;
import com.survivalkid.game.entity.personage.Personage;

public class Medkit extends ItemEntity {
	
	/** Default constructor. */
	public Medkit() {
		super("Medkit", SpriteEnum.MEDKIT, 0, 0, 0);
	};
	
	/** Default constructor. */
	public Medkit(int _x, int _y) {
		super("Medkit", SpriteEnum.MEDKIT, _x, _y, 0);
		init();
	};

	/** Initialize the enemy. */
	private void init() {
		gravity = 1;
		affectedByFloor = true;
		affectedByCeiling = false;
		redefineHitBox((sprite.getWidth() * 10) / 100, (sprite.getHeight() * 10) / 100, (sprite.getWidth() * 80) / 100,
				(sprite.getHeight() * 90) / 100);
	}

	@Override
	public void update(long gameTime) {		
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

	/**
	 * Initialize the position, speed and misc. attributes of the enemy.
	 */
//	@Override
//	public void initRandomPositionAndSpeed(Point playerPosition) {
//		// random spawn position
//		int random = (int) (Math.random() * 100);
//		if (random < 34) {
//			sprite.setX(-20);
//			sprite.setY(380);
//			direction = DirectionConstants.RIGHT;
//		} else if (random < 69) {
//			sprite.setX(MoveUtil.SCREEN_WIDTH + 20);
//			sprite.setY(380);
//			direction = DirectionConstants.LEFT;
//		} else if (random < 84) {
//			sprite.setX(0);
//			sprite.setY(-20);
//			direction = DirectionConstants.RIGHT;
//		} else {
//			sprite.setX(MoveUtil.SCREEN_WIDTH);
//			sprite.setY(-20);
//			direction = DirectionConstants.LEFT;
//		}
//		
//		// init speed
//		setSpeedY(0);
//		if (direction == DirectionConstants.LEFT) {
//			setSpeedX(-2);
//		} else {
//			setSpeedX(2);
//		}
//
//		init();
//	}

	@Override
	public void collide(GameEntity _gameEntity) {
		if (_gameEntity instanceof Personage) {
			((Personage) _gameEntity).revoceryLife(30, EnumLife.RECOVERY_LIFE);
			die();
		}
	}

}
