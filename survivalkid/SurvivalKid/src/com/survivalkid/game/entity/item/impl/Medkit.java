package com.survivalkid.game.entity.item.impl;

import com.survivalkid.R;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.Life.EnumLife;
import com.survivalkid.game.entity.item.StandardObject;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.singleton.GameContext;

public class Medkit extends StandardObject {
	
	/** Default constructor. */
	public Medkit() {
		super(GameContext.getSingleton().getContext().getString(R.string.medkit), SpriteEnum.MEDKIT_SMALL, 0, 0, 0);
		description = GameContext.getSingleton().getContext().getString(R.string.medkitDesc);
	}
	
	/** Default constructor. */
	public Medkit(int _x, int _y) {
		super(GameContext.getSingleton().getContext().getString(R.string.medkit), SpriteEnum.MEDKIT_SMALL, _x, _y, 0);
		description = GameContext.getSingleton().getContext().getString(R.string.medkitDesc);
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
		_perso.recoveryLife(15, EnumLife.RECOVERY_LIFE);
	}

}
