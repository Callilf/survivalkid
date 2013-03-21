package com.survivalkid.game.entity.item;

import android.graphics.Point;

import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.util.MoveUtil;

/**
 * Define a standard object.
 * @author Thomas
 *
 */
public abstract class StandardObject extends ItemEntity {
	
	/** Default constructor. */
	public StandardObject(String _name, SpriteEnum spriteEnum, int _x, int _y, int _power) {
		super("_name", spriteEnum, _x, _y, 0);
		init();
	}

	/** Initialize the item. */
	protected abstract void init();

	@Override
	public void die() {
		dead = true;
	}
	
	/**
	 * Initialize the position, speed and misc. attributes of the enemy.
	 */
	@Override
	public void initRandomPositionAndSpeed(Point playerPosition) {
		// random spawn position
		int randomX = (int) (MoveUtil.getRandomPositionX() - sprite.getWidth());
		sprite.setX(randomX);
		sprite.setY(MoveUtil.BACKGROUND_TOP - sprite.getHeight());

		init();
	}

}
