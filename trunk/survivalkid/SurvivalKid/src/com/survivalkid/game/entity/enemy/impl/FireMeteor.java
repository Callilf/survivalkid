package com.survivalkid.game.entity.enemy.impl;

import com.survivalkid.game.core.enums.SpriteEnum;

public class FireMeteor extends Meteore {
	
	public FireMeteor() {
		super("Meteore", SpriteEnum.FIRE_METEORE, 0, 0, 10, 1);
	}
	
	/**
	 * Pop the ground fire at the position of the explosion of the meteor
	 */
	@Override
	protected void terminate() {
		FireGround fire = creator.createEnemy(FireGround.class, true);
		fire.setX(sprite.getX() + sprite.getWidth()/2 - fire.getSprite().getWidth()/2);
		fire.setY(sprite.getY());
	}
}
