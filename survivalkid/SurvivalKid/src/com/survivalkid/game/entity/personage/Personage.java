package com.survivalkid.game.entity.personage;

import android.graphics.Bitmap;

import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.util.MoveUtil;

public class Personage extends GameEntity {

	/**
	 * Create character
	 */
	public Personage(Bitmap bitmap, int x, int y, int nbColum, int nbRows) {
		super("CharacterA", bitmap, x, y, nbColum, nbRows);
		setSubjectToGravity(true);
	}

	@Override
	public void collide(GameEntity _gameEntity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void die() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void update(long gameTime) {
		MoveUtil.calculNewSpeed(this);
		if (!MoveUtil.isHorizontalMoving()) {
			setSpeedX(getSpeedX()/3);
		}
		super.update(gameTime);
	}

}
