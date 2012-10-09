package com.survivalkid.game.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class ActionButton {
	
	private Bitmap sprite;
	private int x;
	private int y;
	
	
	/**
	 * Constructor
	 * @param _bitmap the sprite
	 * @param _x the x position
	 * @param _y the y position
	 */
	public ActionButton(Bitmap _bitmap) {
		sprite = _bitmap;
	}
	
	/**
	 * Constructor
	 * @param _bitmap the sprite
	 * @param _x the x position
	 * @param _y the y position
	 */
	public ActionButton(Bitmap _bitmap, int _x, int _y) {
		sprite = _bitmap;
		x = _x;
		y = _y;
	}
	
	/**
	 * Set the positions of the button
	 * @param _x x pos
	 * @param _y y pos
	 */
	public void setPosition(int _x, int _y) {
		x = _x;
		y = _y;
	}

	/**
	 * draw
	 * @param canvas the canvas
	 */
	public void draw(Canvas canvas) {
		canvas.drawBitmap(sprite, x, y, null);
	}
	
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	
	public int getWidth() {
		return sprite.getWidth();
	}
	
	public int getHeight() {
		return sprite.getHeight();
	}
}
