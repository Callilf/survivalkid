package com.survivalkid.game.entity.item.impl;

import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.core.enums.StateEnum;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.item.StandardObject;
import com.survivalkid.game.entity.personage.Personage;

public class EnemySpeedReducer extends StandardObject {
	
	/** Duration of the effect */
	private static final long ALTER_GAME_DURATION = 10000;
	
	/** Default constructor. */
	public EnemySpeedReducer() {
		this(0,0);
	};
	
	/** Default constructor. */
	public EnemySpeedReducer(int _x, int _y) {
		super("EnemySpeedReducer", SpriteEnum.HOURGLASS_GREEN, _x, _y, 0);
	};

	@Override
	protected void init() {
		timeToLive = 4000;
		setInBalloon(true);
		
		gravity = 1;
		affectedByFloor = true;
		affectedByCeiling = false;
		redefineHitBox((sprite.getWidth() * 10) / 100, (sprite.getHeight() * 10) / 100, (sprite.getWidth() * 80) / 100,
				(sprite.getHeight() * 90) / 100);
	}

	@Override
	public void collide(GameEntity _gameEntity) {
		if (_gameEntity instanceof Personage) {
			_gameEntity.addState(StateEnum.STATE_LOW_SPEED_ENEMIES, ALTER_GAME_DURATION);
			die();
		}
	}

}
