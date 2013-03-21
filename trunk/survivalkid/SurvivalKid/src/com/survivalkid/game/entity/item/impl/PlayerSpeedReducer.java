package com.survivalkid.game.entity.item.impl;

import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.core.enums.StateEnum;
import com.survivalkid.game.entity.item.StandardObject;
import com.survivalkid.game.entity.personage.Personage;

public class PlayerSpeedReducer extends StandardObject {

	/** Duration of the effect */
	private static final long ALTER_GAME_DURATION = 4000;

	/** Default constructor. */
	public PlayerSpeedReducer() {
		this(0, 0);
	};

	/** Default constructor. */
	public PlayerSpeedReducer(int _x, int _y) {
		super("PlayerSpeedReducer", SpriteEnum.RED_CLOCK, _x, _y, 0);
	};

	@Override
	protected void init() {
		timeToLive = 4000;
		setInBalloon(true);

		gravity = 1;
		affectedByFloor = true;
		affectedByCeiling = false;

		redefineHitBoxForItems();
	}
	
	@Override
	public void redefineHitBoxForItems() {
		redefineHitBox((sprite.getWidth() * 10) / 100, (sprite.getHeight() * 10) / 100, (sprite.getWidth() * 80) / 100,
				(sprite.getHeight() * 90) / 100);
	}

	@Override
	public void collideWithPerso(Personage _perso) {
		_perso.addState(StateEnum.STATE_LOW_SPEED, ALTER_GAME_DURATION);
	}

}
