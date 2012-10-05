package com.survivalkid.game.entity;

import android.graphics.Canvas;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.util.MoveUtil;

public abstract class GameEntity {
	/** Last affected id. */
	public static int lastId;

	/** The id of the gameEntity. */
	private int id;
	/** The name of the gameEntity. */
	private String name;
	/** The sprite (will be an animation in the future). */
	private AnimatedSprite sprite;

	// Speed attributes
	private int speedX;
	private int speedY;
	private boolean isSubjectToGravity;
	
	public void update(long gameTime) {
		sprite.update(gameTime);
	}

	public abstract void collide(GameEntity _gameEntity);

	public void draw(Canvas canvas) {
		sprite.draw(canvas);
	}

	public abstract void die();

	/**
	 * Move the sprite according to its inertia : its current {@link #speedX}
	 * and {@link #speedY}, and also gravity if {@link #isSubjectToGravity()} is
	 * true.
	 */
	public void move() {
		addX(speedX);
		addY(speedY);
		if (isSubjectToGravity) {
			speedY -= MoveUtil.GRAVITY;
		}
	}

	private void addY(int _dy) {
		int newY = sprite.getY() + _dy;
		if (newY < 0) {
			newY = 0;
			speedY = 0;
		} else if (newY + sprite.getHeight() > MoveUtil.MAX_Y) {
			newY = MoveUtil.MAX_Y - sprite.getHeight();
			speedY = 0;
		}
		// Now set the new X
		sprite.offset(0, newY - sprite.getY());
	}

	/**
	 * Adds _dx to the position of the sprite, checking if it goes out of
	 * screen. If it does reset speed to 0.
	 * 
	 * @param _dx
	 */
	private void addX(int _dx) {
		int newX = sprite.getX() + _dx;
		if (newX < 0) {
			newX = 0;
			speedX = 0;
		} else if (newX + sprite.getWidth() > MoveUtil.MAX_X) {
			newX = MoveUtil.MAX_X - sprite.getWidth();
			speedX = 0;
		}
		// Now set the new X
		sprite.offset(newX - sprite.getX(), 0);
	}

	/**
	 * Constructor called by parents class
	 * 
	 * @param _name
	 *            name of the entity
	 * @param _sprite
	 *            sprite of the entity
	 */
	public GameEntity(String _name, AnimatedSprite _sprite) {
		id = lastId++;
		name = _name;
		sprite = _sprite;
	}
	
	public boolean onFloor() {
		return false;
	}
	
	// ---- Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AnimatedSprite getSprite() {
		return sprite;
	}

	public void setSprite(AnimatedSprite sprite) {
		this.sprite = sprite;
	}

	/**
	 * @return the speedX
	 */
	public int getSpeedX() {
		return speedX;
	}

	/**
	 * @param _speedX
	 *            the speedX to set
	 */
	public void setSpeedX(int _speedX) {
		this.speedX = _speedX;
	}

	/**
	 * @return the speedY
	 */
	public int getSpeedY() {
		return speedY;
	}

	/**
	 * @param _speedY
	 *            the speedY to set
	 */
	public void setSpeedY(int _speedY) {
		this.speedY = _speedY;
	}

	/**
	 * @return the isSubjectToGravity
	 */
	public boolean isSubjectToGravity() {
		return isSubjectToGravity;
	}

	/**
	 * @param _isSubjectToGravity
	 *            the isSubjectToGravity to set
	 */
	public void setSubjectToGravity(boolean _isSubjectToGravity) {
		this.isSubjectToGravity = _isSubjectToGravity;
	}

}
