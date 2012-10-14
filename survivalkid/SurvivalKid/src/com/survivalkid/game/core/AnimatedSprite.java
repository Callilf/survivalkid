package com.survivalkid.game.core;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.survivalkid.game.core.Constants.DirectionConstants;

public class AnimatedSprite {

	private static final String TAG = AnimatedSprite.class.getSimpleName();

	// Spritesheet and relatives attributes
	private Bitmap bitmap; // the animation sequence
	private Bitmap flippedBitmap; // The horizontally flipped bitmap
	private Rect sourceRect; // the rectangle to be drawn from the animation
								// bitmap
	private int framePerRow; // number of frames in a single row of the png
	private long frameTicker; // the time of the last frame update

	// Position
	private int x;
	private int y;
	// Height and Width
	private int width;
	private int height;

	// Map of animations
	private Map<String, Animation> animations;

	// Current animation
	private String currentAnimation; // name of the current anim
	private int currentIndex; // Index of the current anim on the current
								// frameList
	private int currentFrame; // Current frame

	private boolean animating = false;
	private boolean repeat = false;

	// Next animation attributes
	private boolean animationFinished = true; // True is the last anim is over
												// (used to launch another
												// animation after)
	private boolean waiting = false;
	private String waitingAnim;
	private boolean waitingRepeat = true;

	/**
	 * Constructor.
	 * 
	 * @param bitmap
	 *            the bitmap sprite
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 * @param nbColum
	 *            the number of column in the spritesheet (the bitmap)
	 * @param nbRows
	 *            the number of rows in the spritesheet
	 * @param frameCount
	 *            the number of frames
	 */
	public AnimatedSprite(Bitmap bitmap, int x, int y, int nbColum, int nbRows) {
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		currentFrame = 0;
		framePerRow = nbColum;
		width = bitmap.getWidth() / nbColum;
		height = bitmap.getHeight() / nbRows;
		sourceRect = new Rect(0, 0, width, height);
		frameTicker = 0l;

		animations = new HashMap<String, Animation>();

		// Create the flipped image
		Matrix matrix = new Matrix();
		matrix.preScale(-1, 1);
		flippedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}

	public AnimatedSprite() {
		x = 0;
		y = 0;
	}

	/**
	 * Update.
	 * 
	 * @param gameTime
	 *            current time
	 */
	public void update(long gameTime, int direction) {
		if (bitmap == null || sourceRect == null) {
			return;
		}

		// If there is no animation
		if (animations == null || animations.size() == 0 || currentAnimation == null) {
			currentFrame = 0;

			// there is an animation running
		} else if (animating) {
			Animation currAnim = animations.get(currentAnimation);
			if (gameTime > frameTicker + (1000 / currAnim.getFps())) {
				frameTicker = gameTime;

				currentIndex++;
				// If it was the last frame of the animation
				if (currentIndex >= currAnim.getFrameList().length) {
					if(currentIndex > 0) {
						currentIndex --;
					}
					endAnimation();
				} else {
					currentFrame = currAnim.getFrameList()[currentIndex];
				}
			}
		} else {
			currentFrame = animations.get(currentAnimation).getFrameList()[currentIndex];
		}

		// define the rectangle to cut out sprite
		int row = 0;
		row = currentFrame / framePerRow;
		int frameOfTheRow = currentFrame % framePerRow;

		// Place the rect to catch the correct frame
		this.sourceRect.top = row * height;
		this.sourceRect.bottom = this.sourceRect.top + height;
		if (direction == DirectionConstants.LEFT) {
			this.sourceRect.left = (framePerRow - 1 - frameOfTheRow) * width;
			this.sourceRect.right = this.sourceRect.left + width;
		} else {
			this.sourceRect.left = (frameOfTheRow * width);
			this.sourceRect.right = this.sourceRect.left + width;
		}
	}

	/**
	 * Handle the end of the current animation.
	 */
	private void endAnimation() {
		if (!repeat) {
			if (waiting) {
				currentIndex = 0;
				currentAnimation = waitingAnim;
				repeat = waitingRepeat;
				animating = true;
			} else {
				animating = false;
				setAnimationFinished(true);
			}
		} else {
			currentIndex = 0;
		}
	}

	/**
	 * Draw.
	 * 
	 * @param canvas
	 */
	public void draw(Canvas canvas, int direction) {
		if (bitmap != null && sourceRect != null) {
			if (direction == DirectionConstants.RIGHT) {
				Rect destRect = new Rect(getX(), getY(), getX() + width, getY() + height);
				canvas.drawBitmap(bitmap, sourceRect, destRect, null);
			} else {
				Rect destRect = new Rect(getX(), getY(), getX() + width, getY() + height);
				canvas.drawBitmap(flippedBitmap, sourceRect, destRect, null);
			}
		}
	}

	/**
	 * Add an animation.
	 * 
	 * @param name
	 *            the name of the animation
	 * @param frameList
	 *            the frame list. Ex: {0,1,2,3,2,1,0}
	 * @param _fps
	 *            the fps of the animation (fluent around 15 usually)
	 */
	public void addAnimation(String _name, int[] _frameList, int _fps) {
		Animation anim = new Animation(_name, _frameList, _fps);
		animations.put(_name, anim);
	}

	/**
	 * Launch an animation.
	 * 
	 * @param _name
	 *            the name of the animation
	 * @param _repeat
	 *            true is the animation must loop
	 * @param _forceStop
	 *            if true, the animation will be launched immediately. If false,
	 *            it waits for the previous animation to end.
	 */
	public void play(String _name, boolean _repeat, boolean _forceStop) {
		if (currentAnimation != null && currentAnimation.equals(_name)) {
			currentIndex = 0;
			return;
		}
		if (_forceStop || currentAnimation == null || !animating) {
			animating = true;
			currentAnimation = _name;
			currentIndex = 0;
			repeat = _repeat;
			setAnimationFinished(false);
			waiting = false;
		} else {
			waitingAnim = _name;
			waitingRepeat = _repeat;
			waiting = true;
		}
	}

	/**
	 * Stop the current animation;
	 */
	public void stop() {
		animating = false;
		setAnimationFinished(true);
		currentIndex = 0;
	}

	/**
	 * Translate the sprite.
	 * 
	 * @param _x
	 *            horizontal translation
	 * @param _y
	 *            vertical translation
	 */
	public void offset(int _x, int _y) {
		x += _x;
		y += _y;
	}

	// ----------------------------------------------------------------------------
	// Getters & Setters
	// ----------------------------------------------------------
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
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
	 * @param y
	 *            the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the currentFrame
	 */
	public int getCurrentFrame() {
		return currentFrame;
	}

	/**
	 * @param currentFrame
	 *            the currentFrame to set
	 */
	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public boolean isAnimationFinished() {
		return animationFinished;
	}

	public void setAnimationFinished(boolean animationFinished) {
		this.animationFinished = animationFinished;
	}

}
