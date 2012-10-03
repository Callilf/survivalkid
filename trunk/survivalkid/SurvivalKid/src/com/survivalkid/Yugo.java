package com.survivalkid;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Yugo {

		private static final String TAG = Yugo.class.getSimpleName();

		private Bitmap bitmap;		// the animation sequence
		private Rect sourceRect;	// the rectangle to be drawn from the animation bitmap
		private int frameNr;		// number of frames in animation
		private int framePerRow;	// number of frames in a single row of the png
		private int currentFrame;	// the current frame
		private long frameTicker;	// the time of the last frame update
		private int framePeriod;	// milliseconds between each frame (1000/fps)

		private int spriteWidth;	// the width of the sprite to calculate the cut out rectangle
		private int spriteHeight;	// the height of the sprite

		private int x;				// the X coordinate of the object (top left of the image)
		private int y;				// the Y coordinate of the object (top left of the image)

		public Yugo(Bitmap bitmap, int x, int y, int nbColum, int nbRows, int fps, int frameCount) {
			this.bitmap = bitmap;
			this.x = x;
			this.y = y;
			currentFrame = 0;
			frameNr = frameCount;
			framePerRow = nbColum;
			spriteWidth = bitmap.getWidth() / nbColum;
			spriteHeight = bitmap.getHeight() / nbRows;
			sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);
			framePeriod = 1000 / fps;
			frameTicker = 0l;
		}
		
		
		public void update(long gameTime) {
			if (gameTime > frameTicker + framePeriod) {
				frameTicker = gameTime;
				// increment the frame
				currentFrame++;
				if (currentFrame >= frameNr) {
					currentFrame = 0;
				}
			}
			
			// define the rectangle to cut out sprite
			int row = 0;
			row = currentFrame/framePerRow;
			int frameOfTheRow = currentFrame%framePerRow;
				
			this.sourceRect.left = (frameOfTheRow * spriteWidth);
			this.sourceRect.right = this.sourceRect.left + spriteWidth;
			this.sourceRect.top = row*spriteHeight;
		    this.sourceRect.bottom = this.sourceRect.top + spriteHeight;
		}

		public void draw(Canvas canvas) {
			// where to draw the sprite
			Rect destRect = new Rect(getX(), getY(), getX() + spriteWidth, getY() + spriteHeight);
			canvas.drawBitmap(bitmap, sourceRect, destRect, null);
		}


		public int getX() {
			return x;
		}


		public void setX(int x) {
			this.x = x;
		}


		public int getY() {
			return y;
		}


		public void setY(int y) {
			this.y = y;
		}


	
}
