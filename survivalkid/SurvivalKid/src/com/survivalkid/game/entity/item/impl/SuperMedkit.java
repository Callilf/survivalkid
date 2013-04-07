package com.survivalkid.game.entity.item.impl;

import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.Life.EnumLife;
import com.survivalkid.game.entity.item.StandardObject;
import com.survivalkid.game.entity.personage.Personage;

public class SuperMedkit extends StandardObject {

	/** Default constructor. */
	public SuperMedkit() {
		super("Super medkit", SpriteEnum.MEDKIT, 0, 0, 0);
		description = "Heal 25 pv.";
	}

	/** Default constructor. */
	public SuperMedkit(int _x, int _y) {
		super("Super medkit", SpriteEnum.MEDKIT, _x, _y, 0);
		description = "Heal 25 pv.";
	}

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
		_perso.recoveryLife(25, EnumLife.RECOVERY_LIFE);
	}

}
