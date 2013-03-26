package com.survivalkid.game.entity.item.impl;

import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.core.enums.StateEnum;
import com.survivalkid.game.entity.Life.EnumLife;
import com.survivalkid.game.entity.item.StandardObject;
import com.survivalkid.game.entity.personage.Personage;

public class Corrida extends StandardObject {
	
	/** Default constructor. */
	public Corrida() {
		this(0, 0);
	}
	
	/** Default constructor. */
	public Corrida(int _x, int _y) {
		super("Corrida", SpriteEnum.CORRIDA, _x, _y, 0);
	}

	@Override
	protected void init() {
		timeToLive = 5000;
		setInBalloon(true);
		
		gravity = 1;
		affectedByFloor = true;
		affectedByCeiling = false;

		redefineHitBoxForItems();
	}
	
	@Override
	public void redefineHitBoxForItems() {
		redefineHitBox((sprite.getWidth() * 5) / 100, (sprite.getHeight() * 5) / 100, (sprite.getWidth() * 90) / 100,
				(sprite.getHeight() * 95) / 100);
	}

	@Override
	public void collideWithPerso(Personage _perso) {
		_perso.addState(StateEnum.STATE_CORRIDA, 2000);
		_perso.playCorridaWaitingAnim();
	}

}
