package com.survivalkid.game.entity.item.impl;

import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.core.enums.StateEnum;
import com.survivalkid.game.entity.item.StandardObject;
import com.survivalkid.game.entity.personage.Personage;

public class EnemySpeedReducer extends StandardObject {

	/** Duration of the effect */
	private static final long ALTER_GAME_DURATION = 10000;

	/** Default constructor. */
	public EnemySpeedReducer() {
		super("Time decelerator", SpriteEnum.HOURGLASS_GREEN, 0, 0, 0);
		description = "Reduce the speed of all the enemies during 10 seconds.";
	};

	/** Default constructor. */
	public EnemySpeedReducer(int _x, int _y) {
		super("Time decelerator", SpriteEnum.HOURGLASS_GREEN, _x, _y, 0);
		description = "Reduce the speed of all the enemies during 10 seconds.";
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
		_perso.addState(StateEnum.STATE_LOW_SPEED_ENEMIES, ALTER_GAME_DURATION);
	}

}
