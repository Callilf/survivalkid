package com.survivalkid.game.entity.item.impl;

import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.Life.EnumLife;
import com.survivalkid.game.entity.item.StandardObject;
import com.survivalkid.game.entity.personage.Personage;

public class Medkit extends StandardObject {
	
	/** Default constructor. */
	public Medkit() {
		super("Medkit", SpriteEnum.MEDKIT, 0, 0, 0);
	}
	
	/** Default constructor. */
	public Medkit(int _x, int _y) {
		super("Medkit", SpriteEnum.MEDKIT, _x, _y, 0);
		init();
	}

	@Override
	protected void init() {
		timeToLive = 5000;
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
			((Personage) _gameEntity).revoceryLife(20, EnumLife.RECOVERY_LIFE);
			die();
		}
	}

}
