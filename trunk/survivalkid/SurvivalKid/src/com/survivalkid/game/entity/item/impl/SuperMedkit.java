package com.survivalkid.game.entity.item.impl;

import com.survivalkid.R;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.Life.EnumLife;
import com.survivalkid.game.entity.item.StandardObject;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.singleton.GameContext;

public class SuperMedkit extends StandardObject {

	/** Default constructor. */
	public SuperMedkit() {
		super(GameContext.getSingleton().getContext().getString(R.string.superMedkit), SpriteEnum.MEDKIT, 0, 0, 0);
		description = GameContext.getSingleton().getContext().getString(R.string.superMedkitDesc);
	}

	/** Default constructor. */
	public SuperMedkit(int _x, int _y) {
		super(GameContext.getSingleton().getContext().getString(R.string.superMedkit), SpriteEnum.MEDKIT, _x, _y, 0);
		description = GameContext.getSingleton().getContext().getString(R.string.superMedkitDesc);
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
