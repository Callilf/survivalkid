package com.survivalkid.game.entity.enemy.impl;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.util.MoveUtil;

public class FireMeteor extends Meteore {
	
	public FireMeteor() {
		super("Meteore", SpriteEnum.FIRE_METEORE, 0, 0, 10, 1);
		description = "Test description FireMeteore";
	}
	
	/**
	 * Pop the ground fire at the position of the explosion of the meteor
	 */
	@Override
	protected void terminate() {
		FireGround fire = creator.createEnemy(FireGround.class, true);
		//Log.w("FIRE",""+(Math.min(hitBox.bottom,MoveUtil.GROUND) - fire.getSprite().getHeight())+ " - "+(MoveUtil.GROUND-sprite.getHeight()));
		fire.place(sprite.getX() + sprite.getWidth()/2 - fire.getSprite().getWidth()/2, 
				Math.min(hitBox.bottom,MoveUtil.GROUND) - (int)(Math.ceil(fire.getSprite().getHeight()*0.95)));
		emitter.setStopping(true);
	}
	
	@Override
	protected void initDeathAnim() {
		deathAnim = new AnimatedSprite(SpriteEnum.FIRE_METEORE_EXPLOSION, 0, 0);
	}
}
