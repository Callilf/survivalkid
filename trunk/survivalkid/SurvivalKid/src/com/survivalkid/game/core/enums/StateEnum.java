package com.survivalkid.game.core.enums;

public enum StateEnum {
	/** Recovery mode : temporary invincibility after a hit! */
	STATE_RECOVERY,
	/** Low speed mode */
	STATE_LOW_SPEED(SpriteEnum.RED_CLOCK);
	
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
	

