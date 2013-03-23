package com.survivalkid.game.particle;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.singleton.GameContext;

public class Particle {

	/** The sprite. */
	private AnimatedSprite sprite;
	/** The expiration time of the particle. */
	private long expirationTime;

	private long speedXChange;
	private long speedYChange;
	private int speedXChangePeriod;
	private long speedYChangePeriod;

	/** The horizontal speed. */
	private int speedX;
	/** The vertical speed. */
	private int speedY;
	/** The direction of the sprite. */
	private int direction;

	/** The paint for fadeOut. */
	private Paint fadeOutPaint;
	
	private boolean deleteParticleWhenAnimFinished;

	/** Whether it is being draw or not. */
	private boolean visible;
	/** Whether the particle should fade out or disappear instantely. */
	private boolean fadeOut;

	public Particle(SpriteEnum _spriteEnum, int _timeout, int _speedX, int _speedY) {
		sprite = new AnimatedSprite(_spriteEnum);
		speedX = _speedX;
		speedY = _speedY;
		expirationTime = GameContext.getSingleton().gameDuration + _timeout;
	}

	public void update(long gameDuration) {
		if (gameDuration > expirationTime) {
			// Expiration reached, either disappear or fade
			if (fadeOut) {
				if (fadeOutPaint.getAlpha() == 0) {
					visible = false;
				} else {
					fadeOutPaint.setAlpha(fadeOutPaint.getAlpha() - 5);
				}
			} else {
				visible = false;
			}
		}

		if (deleteParticleWhenAnimFinished && sprite.isAnimationFinished()) {
			visible = false;
		}

		if (!visible) {
			return;
		}

		if (gameDuration > speedXChange) {
			sprite.setX(sprite.getX() + speedX);
			speedXChange = gameDuration + speedXChangePeriod;
		}
		if (gameDuration > speedYChange) {
			sprite.setY(sprite.getY() + speedY);
			speedYChange = gameDuration + speedYChangePeriod;
		}

		sprite.update(gameDuration, direction);

	}

	public void draw(Canvas canvas) {
		if (fadeOut) {
			sprite.draw(canvas, direction, fadeOutPaint);
		} else {
			sprite.draw(canvas, direction, null);
		}
	}

	// ------------------------------------------ GETTERS AND SETTERS
	/**
	 * @return the sprite
	 */
	public AnimatedSprite getSprite() {
		return sprite;
	}

	/**
	 * @param sprite
	 *            the sprite to set
	 */
	public void setSprite(AnimatedSprite sprite) {
		this.sprite = sprite;
	}

	/**
	 * @return the expirationTime
	 */
	public long getExpirationTime() {
		return expirationTime;
	}

	/**
	 * @param expirationTime
	 *            the expirationTime to set
	 */
	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

	/**
	 * @return the speedX
	 */
	public int getSpeedX() {
		return speedX;
	}

	/**
	 * @param speedX
	 *            the speedX to set
	 */
	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	/**
	 * @return the speedY
	 */
	public int getSpeedY() {
		return speedY;
	}

	/**
	 * @param speedY
	 *            the speedY to set
	 */
	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

	/**
	 * @return the direction
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible
	 *            the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * @return the fadeOut
	 */
	public boolean isFadeOut() {
		return fadeOut;
	}

	/**
	 * @param fadeOut
	 *            the fadeOut to set
	 */
	public void setFadeOut(boolean fadeOut) {
		this.fadeOut = fadeOut;
		if (fadeOut) {
			fadeOutPaint = new Paint();
		}
	}

	/**
	 * Set the timeout.
	 * 
	 * @param timeout
	 *            the time out to set
	 */
	public void setTimeOut(int timeout) {
		expirationTime = GameContext.getSingleton().gameDuration + timeout;
	}

	/**
	 * Set x.
	 * 
	 * @param x
	 */
	public void setX(int x) {
		sprite.setX(x);
	}

	/**
	 * Set y.
	 * 
	 * @param y
	 */
	public void setY(int y) {
		sprite.setY(y);
	}

	/**
	 * @return the speedXChangePeriod
	 */
	public int getSpeedXChangePeriod() {
		return speedXChangePeriod;
	}

	/**
	 * @param speedXChangePeriod
	 *            the speedXChangePeriod to set
	 */
	public void setSpeedXChangePeriod(int speedXChangePeriod) {
		this.speedXChangePeriod = speedXChangePeriod;
		speedXChange = 0;
	}

	/**
	 * @return the speedYChangePeriod
	 */
	public long getSpeedYChangePeriod() {
		return speedYChangePeriod;
	}

	/**
	 * @param speedYChangePeriod
	 *            the speedYChangePeriod to set
	 */
	public void setSpeedYChangePeriod(long speedYChangePeriod) {
		this.speedYChangePeriod = speedYChangePeriod;
		speedYChange = 0;
	}

	/**
	 * @return the deleteParticleWhenAnimFinished
	 */
	public boolean isDeleteParticleWhenAnimFinished() {
		return deleteParticleWhenAnimFinished;
	}

	/**
	 * @param deleteParticleWhenAnimFinished the deleteParticleWhenAnimFinished to set
	 */
	public void setDeleteParticleWhenAnimFinished(boolean deleteParticleWhenAnimFinished) {
		this.deleteParticleWhenAnimFinished = deleteParticleWhenAnimFinished;
	}

}
