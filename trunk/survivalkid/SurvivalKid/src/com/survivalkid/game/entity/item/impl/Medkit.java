package com.survivalkid.game.entity.item.impl;

import android.graphics.Canvas;
import android.graphics.Point;

import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.Life.EnumLife;
import com.survivalkid.game.entity.item.ItemEntity;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.util.MoveUtil;

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
		timeToLive = 5000;
		setInBalloon(true);
		
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
	

	/**
	 * Initialize the position, speed and misc. attributes of the enemy.
	 */
	@Override
	public void initRandomPositionAndSpeed(Point playerPosition) {
		// random spawn position
		int randomX = 20 + (int) (Math.random() * MoveUtil.SCREEN_WIDTH - 20 - sprite.getWidth());
		sprite.setX(randomX);
		sprite.setY(-sprite.getHeight());

		init();
	}

	@Override
	public void collide(GameEntity _gameEntity) {
		if (_gameEntity instanceof Personage) {
			((Personage) _gameEntity).revoceryLife(30, EnumLife.RECOVERY_LIFE);
			die();
		}
	}

}
