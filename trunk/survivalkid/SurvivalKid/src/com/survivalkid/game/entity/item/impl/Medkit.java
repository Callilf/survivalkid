package com.survivalkid.game.entity.item.impl;

import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.Life.EnumLife;
import com.survivalkid.game.entity.item.StandardObject;
import com.survivalkid.game.entity.personage.Personage;

public class Medkit extends StandardObject {
	
	/** Default constructor. */
	public Medkit() {
		this(0, 0);
	}
	
	/** Default constructor. */
	public Medkit(int _x, int _y) {
		super("Medkit", SpriteEnum.MEDKIT_SMALL, _x, _y, 0);
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
	public void collideWithPerso(Personage _perso) {
		_perso.revoceryLife(15, EnumLife.RECOVERY_LIFE);
	}

}
