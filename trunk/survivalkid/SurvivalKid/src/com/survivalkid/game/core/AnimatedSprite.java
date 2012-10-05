package com.survivalkid.game.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class AnimatedSprite {

		private static final String TAG = AnimatedSprite.class.getSimpleName();
		
		//Spritesheet and relatives attributes
		private Bitmap bitmap;		// the animation sequence
		private Rect sourceRect;	// the rectangle to be drawn from the animation bitmap
		private int frameNr;		// number of frames in animation
		private int framePerRow;	// number of frames in a single row of the png
		private long frameTicker;	// the time of the last frame update
		private int framePeriod;	// milliseconds between each frame (1000/fps)

		//Position
		private int x;
		private int y;
		//Height and Width
		private int width;
		private int height;
		
		//Map of animations
		private Map<String,Animation> animations;
		
		//Current animation
		private String currentAnimation;	//name of the current anim
		private int currentIndex;			//Index of the current anim on the current frameList
		private int currentFrame;			//Current frame

		private boolean animating = false;

		/**
		 * Constructor.
		 * @param bitmap the bitmap sprite
		 * @param x the x position
		 * @param y the y position
		 * @param nbColum the number of column in the spritesheet (the bitmap)
		 * @param nbRows the number of rows in the spritesheet
		 * @param fps the fps of the animation (usually fluent around 20)
		 * @param frameCount the number of frames
		 */
		public AnimatedSprite(Bitmap bitmap, int x, int y, int nbColum, int nbRows, int fps, int frameCount) {
			this.bitmap = bitmap;
			this.x = x;
			this.y = y;
			currentFrame = 0;
			frameNr = frameCount;
			framePerRow = nbColum;
			width = bitmap.getWidth() / nbColum;
			height = bitmap.getHeight() / nbRows;
			sourceRect = new Rect(0, 0, width, height);
			framePeriod = 1000 / fps;
			frameTicker = 0l;
			
			animations = new HashMap<String,Animation>();
		}
		
		
		public AnimatedSprite() {
			x = 0;
			y = 0;
		}


		/**
		 * Update.
		 * @param gameTime current time
		 */
		public void update(long gameTime) {
			if(bitmap == null || sourceRect == null) {
				return;
			}
			
			//If there is an animation running
			if(animations.size() == 0 ) {
				currentFrame = 0;
			} else if (animating) {
				if (gameTime > frameTicker + framePeriod) {
					frameTicker = gameTime;
					
					//Select the correct frame
					currentIndex++;
					if(currentIndex >=  animations.get(currentAnimation).getFrameList().size()) {
						currentIndex = 0;
					}
					currentFrame = animations.get(currentAnimation).getFrameList().get(currentIndex);
				}
			} else {
				currentFrame = animations.get(currentAnimation).getFrameList().get(currentIndex);
			}
			
			// define the rectangle to cut out sprite
			int row = 0;
			row = currentFrame/framePerRow;
			int frameOfTheRow = currentFrame%framePerRow;
				
			this.sourceRect.left = (frameOfTheRow * width);
			this.sourceRect.right = this.sourceRect.left + width;
			this.sourceRect.top = row*height;
		    this.sourceRect.bottom = this.sourceRect.top + height;
		}

		
		/**
		 * Draw.
		 * @param canvas
		 */
		public void draw(Canvas canvas) {
			// where to draw the sprite
			if(bitmap != null && sourceRect != null) {
				Rect destRect = new Rect(getX(), getY(), getX() + width, getY() + height);
				canvas.drawBitmap(bitmap, sourceRect, destRect, null);
			}
		}
		
		/**
		 * Add an animation.
		 * @param name the name of the animation
		 * @param frameList the frame list. Ex: {0,1,2,3,2,1,0}
		 * @param _fps the fps of the animation (fluent around 15 usually)
		 */
		public void addAnimation(String _name, List<Integer> _frameList, int _fps) {
			Animation anim = new Animation(_name, _frameList, _fps);
			animations.put(_name, anim);
		}
		
		/**
		 * Launch an animation.
		 * @param _name the name of the animation
		 */
		public void play(String _name) {
			animating = true;
			currentAnimation = _name;
			currentIndex = 0;
		}
		
		/**
		 * Stop the current animation;
		 */
		public void stop() {
			animating = false;
			currentIndex = 0;
		}
		
		/**
		 * Translate the sprite.
		 * @param _x horizontal translation 
		 * @param _y vertical translation
		 */
		public void offset(int _x, int _y) {
			x += _x;
			y += _y;
		}
		
		//----------------------------------------------------------------------------
		//Getters & Setters ----------------------------------------------------------
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


		/**
		 * @return the width
		 */
		public int getWidth() {
			return width;
		}


		/**
		 * @param width the width to set
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
		 * @param height the height to set
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
		 * @param currentFrame the currentFrame to set
		 */
		public void setCurrentFrame(int currentFrame) {
			this.currentFrame = currentFrame;
		}
		
		
}


