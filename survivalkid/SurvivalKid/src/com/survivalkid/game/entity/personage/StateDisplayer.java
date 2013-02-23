package com.survivalkid.game.entity.personage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.util.MoveUtil;

public class StateDisplayer {
	private static final String TAG = StateDisplayer.class.getSimpleName();

	private static final int GAP_SIZE_X = 40;
	private static final int GAP_SIZE_Y = 20;

	private Map<SpriteEnum, StateObject> map;

	private Paint paint;

	// Used to know where to draw the state
	private int offsetX;

	private class StateObject {
		private AnimatedSprite sprite;
		private Long expiration;
		private String remainingTimeStr;

		public StateObject(AnimatedSprite as, Long exp) {
			sprite = as;
			expiration = exp;
			remainingTimeStr = "";
		}

		public void setRemainingTimeStr(String str) {
			remainingTimeStr = str;
		}

		/**
		 * @return the sprite
		 */
		public AnimatedSprite getSprite() {
			return sprite;
		}

		/**
		 * @param sprite the sprite to set
		 */
		public void setSprite(AnimatedSprite sprite) {
			this.sprite = sprite;
		}

		/**
		 * @return the expiration
		 */
		public Long getExpiration() {
			return expiration;
		}

		/**
		 * @param expiration the expiration to set
		 */
		public void setExpiration(Long expiration) {
			this.expiration = expiration;
		}

		/**
		 * @return the remainingTimeStr
		 */
		public String getRemainingTimeStr() {
			return remainingTimeStr;
		}
		
		
	}

	public StateDisplayer() {
		map = new HashMap<SpriteEnum, StateObject>();
		// Text
		paint = new Paint();
		paint.setTextSize(20);
		paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
		paint.setColor(Color.WHITE);

		Log.d(TAG, "State Displayer created");
	}

	public void update(long gameDuration) {		
		offsetX = GAP_SIZE_X;
		for(SpriteEnum spriteEnum : map.keySet()) {
			if(map.get(spriteEnum).getExpiration() < GameContext.getSingleton().gameDuration) {
				map.remove(spriteEnum);
			} else {
				Date date = new Date(map.get(spriteEnum).getExpiration() - gameDuration);
				DateFormat formatter = new SimpleDateFormat("ss,SSS");
				map.get(spriteEnum).setRemainingTimeStr(formatter.format(date));
				
				AnimatedSprite as = map.get(spriteEnum).getSprite();
				as.setX(MoveUtil.SCREEN_WIDTH - offsetX - as.getWidth());
				as.setY(GAP_SIZE_Y);
				offsetX += as.getWidth() + GAP_SIZE_X;
			}
		}
	}

	public void draw(Canvas canvas) {
		for(SpriteEnum stateEnum : map.keySet()) {
			AnimatedSprite currentSprite = map.get(stateEnum).getSprite();
			currentSprite.draw(canvas, DirectionConstants.RIGHT);
			canvas.drawText(map.get(stateEnum).getRemainingTimeStr(), currentSprite.getX(),
					currentSprite.getY() + currentSprite.getHeight() + 20, paint);
		}
	}

	public void addState(SpriteEnum _spriteEnum, Long _expiration) {
		StateObject tmp = new StateObject(new AnimatedSprite(_spriteEnum, 0, 0), _expiration);
		if(map.containsKey(_spriteEnum)) {
			map.remove(_spriteEnum);
		}
		map.put(_spriteEnum, tmp);
	}

}
