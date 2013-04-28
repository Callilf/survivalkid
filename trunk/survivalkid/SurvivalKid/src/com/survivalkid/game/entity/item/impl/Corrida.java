package com.survivalkid.game.entity.item.impl;

import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.core.enums.StateEnum;
import com.survivalkid.game.entity.item.StandardObject;
import com.survivalkid.game.entity.personage.Personage;

public class Corrida extends StandardObject {
	
	/** Default constructor. */
	public Corrida() {
		super("Muleta", SpriteEnum.CORRIDA, 0, 0, 0);
		description = "A matador cape that Yugo can use to invoke and dodge a mad bull, thus killing all of the others enemies close to the ground.";
	}
	
	/** Default constructor. */
	public Corrida(int _x, int _y) {
		super("Muleta", SpriteEnum.CORRIDA, _x, _y, 0);
		description = "A matador cape that Yugo can use to invoke and dodge a mad bull, thus killing all of the others enemies close to the ground.";
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
		_perso.addState(StateEnum.STATE_CORRIDA, 5000);
		_perso.playCorridaWaitingAnim();
	}

}
