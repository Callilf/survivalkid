package com.survivalkid.game.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class ActionButton {
	
	private Bitmap sprite;
	private Bitmap spritePressed;
	private int x;
	private int y;
	private boolean isPressed = false;
	
	private static final int MARGIN = 25;
	
	
	/**
	 * Constructor
	 * @param _bitmap the sprite
	 * @param _x the x position
	 * @param _y the y position
	 */
	public ActionButton(Bitmap _bitmap, Bitmap _bitmapPressed) {
		sprite = _bitmap;
		spritePressed = _bitmapPressed;
	}
	
	/**
	 * Constructor
	 * @param _bitmap the sprite
	 * @param _x the x position
	 * @param _y the y position
	 */
	public ActionButton(Bitmap _bitmap, Bitmap _bitmapPressed, int _x, int _y) {
		sprite = _bitmap;
		spritePressed = _bitmapPressed;
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
		if(isPressed) {
			canvas.drawBitmap(spritePressed, x, y, null);
		} else {
			canvas.drawBitmap(sprite, x, y, null);

		}
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
	
	public boolean isOnButton(int _x, int _y) {
		//Log.d("TEST", _x+" > "+x+" && "+_x+" < "+x+" + "+getWidth()+" && "+_y+" > "+y+" && "+_y+" < "+y+" + "+getHeight());
		return _x+MARGIN > x && _y+MARGIN > y && _x - MARGIN < x+ getWidth() && _y - MARGIN < y + getHeight();
	}

	/**
	 * @return the isPressed
	 */
	public boolean isPressed() {
		return isPressed;
	}

	/**
	 * @param isPressed the isPressed to set
	 */
	public void setPressed(boolean isPressed) {
		this.isPressed = isPressed;
	}
	
	
}
