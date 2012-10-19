package com.survivalkid.game.core.enums;

public enum StateEnum {
	/** Normal state of the character. */
	STATE_NORMAL(0),
	/** Recovery mode (temporary invincibility after a hit!, */
	STATE_RECOVERY(1);
	
	private int number;
	
	StateEnum(int _number) {
		setNumber(_number);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}
