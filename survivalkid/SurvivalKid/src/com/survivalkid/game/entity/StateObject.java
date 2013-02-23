package com.survivalkid.game.entity;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.enums.StateEnum;

public class StateObject {
	private AnimatedSprite sprite;
	private StateEnum state;
	private Long expiration;
	private String remainingTimeStr;

	public StateObject(StateEnum se, Long exp) {
		if (se.getSprite() != null) {
			sprite = new AnimatedSprite(se.getSprite(), 0, 0);
		}
		else {
			sprite = null;
		}
		state = se;
		expiration = exp;
		remainingTimeStr = "";
	}

	public void setRemainingTimeStr(String str) {
		remainingTimeStr = str;
	}

	/**
	 * @return the sprite
	 */
	public AnimatedSprite getSprite() {
		return sprite;
	}

	/**
	 * @return the state
	 */
	public StateEnum getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(StateEnum state) {
		this.state = state;
	}

	/**
	 * @param sprite the sprite to set
	 */
	public void setSprite(AnimatedSprite sprite) {
		this.sprite = sprite;
	}

	/**
	 * @return the expiration
	 */
	public Long getExpiration() {
		return expiration;
	}

	/**
	 * @param expiration the expiration to set
	 */
	public void setExpiration(Long expiration) {
		this.expiration = expiration;
	}

	/**
	 * @return the remainingTimeStr
	 */
	public String getRemainingTimeStr() {
		return remainingTimeStr;
	}
	
	
}