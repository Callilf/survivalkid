package com.survivalkid.game.entity.item.impl;

import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.core.enums.StateEnum;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.item.StandardObject;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.singleton.GameContext;

public class PlayerSpeedReducer extends StandardObject {
	
	/** Duration of the effect */
	private static final long PLAYER_SPEED_REDUCING_DURATION = 4000;
	
	/** Default constructor. */
	public PlayerSpeedReducer() {
		super("PlayerSpeedReducer", SpriteEnum.RED_CLOCK, 0, 0, 0);
	};
	
	/** Default constructor. */
	public PlayerSpeedReducer(int _x, int _y) {
		super("PlayerSpeedReducer", SpriteEnum.RED_CLOCK, _x, _y, 0);
		init();
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
			long expiration = GameContext.getSingleton().gameDuration + PLAYER_SPEED_REDUCING_DURATION;
			_gameEntity.addState(StateEnum.STATE_LOW_SPEED, expiration);
			die();
		}
	}

}
