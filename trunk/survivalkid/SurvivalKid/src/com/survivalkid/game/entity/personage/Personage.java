package com.survivalkid.game.entity.personage;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.entity.GameEntity;

public class Personage extends GameEntity {

	/**
	 * Create character
	 */
	public Personage(AnimatedSprite sprite) {
		super("CharacterA", sprite);
		setSubjectToGravity(true);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void collide(GameEntity _gameEntity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void die() {
		// TODO Auto-generated method stub

	}

}
