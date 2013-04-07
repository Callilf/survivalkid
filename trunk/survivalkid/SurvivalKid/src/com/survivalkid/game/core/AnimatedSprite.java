package com.survivalkid.game.core;

import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.enums.SpriteEnum;

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

	// recovery after hit attributes
	private boolean recovery;
	
	// Management blink
	private boolean blinking;
	private int blinkNbFrameVisible = 2;
	private int blinkNbFrameInvisible = 1;
	private int currentBlinkFrame;
	
	private boolean visible;


	/**
	 * Constructor
	 * @param spriteEnum the spriteEnum
	 * @param x the position X
	 * @param y the position Y
	 */
	public AnimatedSprite(SpriteEnum spriteEnum, int x, int y) {
		this.x = x;
		this.y = y;
		
		this.bitmap = spriteEnum.getBitmap();
		this.flippedBitmap = spriteEnum.getFlippedBitmap();
		currentFrame = 0;
		framePerRow = spriteEnum.getNbColumn();
		width = bitmap.getWidth() / spriteEnum.getNbColumn();
		height = bitmap.getHeight() / spriteEnum.getNbRows();
		sourceRect = new Rect(0, 0, width, height);
		frameTicker = 0l;

		animations = spriteEnum.getAnimations();

		recovery = false;
		currentBlinkFrame = 0;
	}

	public AnimatedSprite() {
		x = 0;
		y = 0;
	}
	
	public AnimatedSprite(SpriteEnum spriteEnum) {
		this.bitmap = spriteEnum.getBitmap();
		this.flippedBitmap = spriteEnum.getFlippedBitmap();
		currentFrame = 0;
		framePerRow = spriteEnum.getNbColumn();
		width = bitmap.getWidth() / spriteEnum.getNbColumn();
		height = bitmap.getHeight() / spriteEnum.getNbRows();
		sourceRect = new Rect(0, 0, width, height);
		frameTicker = 0l;

		animations = spriteEnum.getAnimations();
	}

	/**
	 * Update.
	 * 
	 * @param gameDuration
	 *            current time
	 */
	public void update(long gameDuration, int direction) {
		if (bitmap == null || sourceRect == null) {
			if (Constants.DEBUG) {
				Log.d(TAG, "Update on a sprite with a bitmap or a sourceRect null");
			}
			return;
		}

		// If there is no animation
		if (animations == null || animations.size() == 0 || currentAnimation == null) {
			currentFrame = 0;

			// there is an animation running
		} else if (animating) {
			Animation currAnim = animations.get(currentAnimation);
			if (gameDuration > frameTicker + (1000 / currAnim.getFps())) {
				frameTicker = gameDuration;

				currentIndex++;
				// If it was the last frame of the animation
				if (currentIndex >= currAnim.getFrameList().length) {
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
				if (currentIndex > 0) {
					currentIndex--;
				}
				animating = false;
				setAnimationFinished(true);
			}
		} else {
			currentIndex = 0;
			currentFrame = animations.get(currentAnimation).getFrameList()[currentIndex];
		}
	}

	/**
	 * Draw.
	 * 
	 * @param canvas
	 */
	public void draw(Canvas canvas, int direction) {
		draw(canvas, direction, null, 0);
	}
	

	/**
	 * Draw.
	 * 
	 * @param canvas
	 */
	public void draw(Canvas canvas, int direction, Paint paint) {
		draw(canvas, direction, paint, 0);
	}
	
	/**
	 * Draw.
	 * 
	 * @param canvas
	 */
	public void draw(Canvas canvas, int direction, Paint paint, float rotation) {
		if (bitmap != null && sourceRect != null) {
			Rect destRect = new Rect(getX(), getY(), getX() + width, getY() + height);
			Bitmap bitmapToUse = null;
			if (direction == DirectionConstants.RIGHT) {
				bitmapToUse = bitmap;
			} else {
				bitmapToUse = flippedBitmap;
			}

			if (blinking) {
				//In recovery state after a hit
				currentBlinkFrame++;
				if (visible) {
					if (currentBlinkFrame % blinkNbFrameVisible == 0) {
						visible = !visible;
						currentBlinkFrame = 0;
					}
				}
				else {
					if (currentBlinkFrame % blinkNbFrameInvisible == 0) {
						visible = !visible;
						currentBlinkFrame = 0;
					}					
				}

				if (visible) {
					if(rotation != 0) { canvas.rotate(rotation, x + width/2, y + height/2); }
					canvas.drawBitmap(bitmapToUse, sourceRect, destRect, paint);
					if(rotation != 0) { canvas.rotate(-rotation, x + width/2, y + height/2); }
				}
			} else {
				if(rotation != 0) { canvas.rotate(rotation, x + width/2, y + height/2); }
				canvas.drawBitmap(bitmapToUse, sourceRect, destRect, paint);
				if(rotation != 0) { canvas.rotate(-rotation, x + width/2, y + height/2); }
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
		play(_name, _repeat, _forceStop, false);
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
	public void play(String _name, boolean _repeat, boolean _forceStop, boolean _replaySameAnim) {
		if (currentAnimation != null && currentAnimation.equals(_name) && !_replaySameAnim) {
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

	public void setBlinkPeriod(int blinkNbFrameVisible, int blinkNbFrameInvisible) {
		this.blinkNbFrameVisible = blinkNbFrameVisible;
		this.blinkNbFrameInvisible = blinkNbFrameInvisible;
	}

	/**
	 * @return the recovery
	 */
	public boolean isRecovery() {
		return recovery;
	}

	/**
	 * @param recovery the recovery to set
	 */
	public void setRecovery(boolean recovery) {
		blinking = recovery;
		this.recovery = recovery;
	}
	
	/**
	 * @param recovery the recovery to set
	 */
	public void setBlinking(boolean blinking) {
		this.blinking = blinking;
	}

	/**
	 * @return the bitmap
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}

	/**
	 * @param bitmap the bitmap to set
	 */
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	

}
