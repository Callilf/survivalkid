package com.survivalkid.game.core.enums;

public enum StateEnum {
	/** Recovery mode : temporary invincibility after a hit! */
	STATE_RECOVERY,
	/** Low speed mode */
	STATE_LOW_SPEED(SpriteEnum.RED_CLOCK),
	/** High speed enemy mode */
	STATE_HIGH_SPEED_ENEMIES(SpriteEnum.HOURGLASS_RED),
	/** Low speed enemy mode */
	STATE_LOW_SPEED_ENEMIES(SpriteEnum.HOURGLASS_GREEN),
	/** Knocked back by an enemy (like the bull). */
	STATE_KNOCK_BACK;
	
	private SpriteEnum sprite;
	
	private StateEnum() {
		sprite = null;
	}
	
	private StateEnum(SpriteEnum sprite) {
		this.sprite = sprite;
	}
	
	public SpriteEnum getSprite() {
		return sprite;
	}
	
}
	

