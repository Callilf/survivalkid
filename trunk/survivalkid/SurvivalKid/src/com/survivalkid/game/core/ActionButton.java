package com.survivalkid.game.core;

import static java.lang.Math.max;
import static java.lang.Math.min;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.survivalkid.game.util.CollisionUtil;
import com.survivalkid.game.util.MoveUtil;

public class ActionButton {
	
	private static final String TAG = ActionButton.class.getSimpleName();
	
	private Bitmap sprite;
	private Bitmap spritePressed;
	private Rect hitBox;
	private int x;
	private int y;
	private boolean isPressed = false;
	
	private static final int MARGIN = 24;
	
	
	/**
	 * Constructor
	 * @param _bitmap the sprite
	 * @param _bitmapPressed the sprite when the button is pressed
	 */
	public ActionButton(Bitmap _bitmap, Bitmap _bitmapPressed) {
		sprite = _bitmap;
		spritePressed = _bitmapPressed;
		
		// define the hitBox of the button
		// hitBox = new Rect(0, 0, x + sprite.getWidth() + MARGIN, y + sprite.getHeight() + MARGIN);
	}
	
	/**
	 * Constructor
	 * @param _bitmap the sprite
	 * @param _bitmapPressed the sprite when the button is pressed
	 * @param _x the x position
	 * @param _y the y position
	 */
	public ActionButton(Bitmap _bitmap, Bitmap _bitmapPressed, int _x, int _y) {
		sprite = _bitmap;
		spritePressed = _bitmapPressed;
		setPosition(_x, _y);
	}
	
	/**
	 * Set the positions of the button and initialize the hitbox
	 * @param _x x pos
	 * @param _y y pos
	 */
	public void setPosition(int _x, int _y) {
		x = _x;
		y = _y;
		hitBox = new Rect(max(0,x - MARGIN), max(0,y - MARGIN), 
				min(MoveUtil.SCREEN_WIDTH, x + sprite.getWidth() + MARGIN), 
				min(MoveUtil.SCREEN_HEIGHT, y + sprite.getHeight() + MARGIN));
		// log the initial position of the hitbox
		Log.d(TAG, "Hitbox : left="+hitBox.left+", right="+hitBox.right+", top="+hitBox.top+", bottom="+hitBox.bottom);
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
		if (CollisionUtil.displayHitBoxes) {
			final Paint paint = new Paint();
			paint.setARGB(50, 0, 0, 128);
			canvas.drawRect(hitBox, paint);
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
		//Log.d("TESTB", "left:"+hitBox.left+",right:"+hitBox.right+",top:"+hitBox.top+",bottom:"+hitBox.bottom);
		return hitBox.contains(_x, _y);
		//Log.d("TEST", _x+" > "+x+" && "+_x+" < "+x+" + "+getWidth()+" && "+_y+" > "+y+" && "+_y+" < "+y+" + "+getHeight());
		//return _x+MARGIN > x && _y+MARGIN > y && _x - MARGIN < x+ getWidth() && _y - MARGIN < y + getHeight();
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
