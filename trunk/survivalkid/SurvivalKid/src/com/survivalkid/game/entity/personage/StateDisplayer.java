package com.survivalkid.game.entity.personage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.util.MoveUtil;

public class StateDisplayer {
	private static final String TAG = StateDisplayer.class.getSimpleName();

	private static final int GAP_SIZE_X = 40;
	private static final int GAP_SIZE_Y = 20;
	
	private List<AnimatedSprite> sprites;
	private List<Long> expirations;
	private List<String> remainingTimesStr;
	private Paint paint;
	
	//Used to know where to draw the state
	private int offsetX;

	public StateDisplayer() {
		sprites = new ArrayList<AnimatedSprite>();
		expirations = new ArrayList<Long>();
		remainingTimesStr = new ArrayList<String>();
		
		//Text
		paint = new Paint(); 
		paint.setTextSize(20);
		paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
		paint.setColor(Color.WHITE);

		Log.d(TAG, "State Displayer created");
	}

	public void update(long gameDuration) {
		remainingTimesStr.clear();
		
		// When a value in remainingTimes is over, remove it and the sprite
		// related.
		Iterator<AnimatedSprite> spriteIterator = sprites.iterator();
		for (Iterator<Long> timesIterator = expirations.iterator(); timesIterator.hasNext();) {
			spriteIterator.next();
			long rt = timesIterator.next();
			if (rt < GameContext.getSingleton().gameDuration) {
				timesIterator.remove();
				spriteIterator.remove();
			} else {
				Date date = new Date(rt - gameDuration);
				DateFormat formatter = new SimpleDateFormat("ss,SSS");
				remainingTimesStr.add(formatter.format(date));
			}
		}
		
		//Set the positions of the sprites
		offsetX = GAP_SIZE_X;
		for(AnimatedSprite as : sprites) {
			as.setX(MoveUtil.SCREEN_WIDTH - offsetX - as.getWidth());
			as.setY(GAP_SIZE_Y);
			offsetX += as.getWidth() + GAP_SIZE_X;
		}

	}

	public void draw(Canvas canvas) {
		if(sprites.size() != remainingTimesStr.size()) {
			return;
		}
		
		for(int i=0 ; i<sprites.size() ; i++) {
			AnimatedSprite currentSprite = sprites.get(i);
			currentSprite.draw(canvas, 0);
			canvas.drawText( remainingTimesStr.get(i), currentSprite.getX(), currentSprite.getY() + currentSprite.getHeight() + 20, paint);
		}
	}

	public void addState(AnimatedSprite sprite, Long remainingTime) {
		sprites.add(sprite);
		expirations.add(remainingTime);
	}

	/**
	 * @return the sprites
	 */
	public List<AnimatedSprite> getSprites() {
		return sprites;
	}

	/**
	 * @param sprites
	 *            the sprites to set
	 */
	public void setSprites(List<AnimatedSprite> sprites) {
		this.sprites = sprites;
	}

	/**
	 * @return the remainingTimes
	 */
	public List<Long> getRemainingTimes() {
		return expirations;
	}

	/**
	 * @param remainingTimes
	 *            the remainingTimes to set
	 */
	public void setRemainingTimes(List<Long> remainingTimes) {
		this.expirations = remainingTimes;
	}

	/**
	 * @return the remainingTimesStr
	 */
	public List<String> getRemainingTimesStr() {
		return remainingTimesStr;
	}

	/**
	 * @param remainingTimesStr
	 *            the remainingTimesStr to set
	 */
	public void setRemainingTimesStr(List<String> remainingTimesStr) {
		this.remainingTimesStr = remainingTimesStr;
	}

}
