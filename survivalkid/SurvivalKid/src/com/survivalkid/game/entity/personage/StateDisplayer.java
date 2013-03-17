package com.survivalkid.game.entity.personage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.enums.StateEnum;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.StateObject;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.util.MoveUtil;

public class StateDisplayer {
	private static final String TAG = StateDisplayer.class.getSimpleName();

	private static final int GAP_SIZE_X = 40;
	private static final int GAP_SIZE_Y = 20;

	private static final DateFormat formatter = new SimpleDateFormat("ss,SSS");

	private Map<StateEnum, StateObject> map;

	private Paint paint;

	// Used to know where to draw the state
	private int offsetX;

	public StateDisplayer(GameEntity entity) {
		map = entity.getStates();
		// Text
		paint = new Paint();
		paint.setTextSize(20);
		paint.setTypeface(GameContext.getSingleton().getFont());
		paint.setColor(Color.WHITE);

		Log.d(TAG, "State Displayer created");
	}

	public void update(long gameDuration) {
		offsetX = GAP_SIZE_X;
		for (StateEnum stateEnum : map.keySet()) {
			StateObject objectState = map.get(stateEnum);
			AnimatedSprite as = objectState.getSprite();
			if (as != null) {
				Date date = new Date(objectState.getExpiration() - gameDuration);
				String timeInString = formatter.format(date);
				if (timeInString.startsWith("0")) {
					timeInString = timeInString.substring(1);
				}
				map.get(stateEnum).setRemainingTimeStr(timeInString.substring(0, timeInString.length() - 2));

				as.setX(MoveUtil.SCREEN_WIDTH - offsetX - as.getWidth());
				as.setY(GAP_SIZE_Y);
				offsetX += as.getWidth() + GAP_SIZE_X;
			}
		}
	}

	public void draw(Canvas canvas) {
		for (StateEnum stateEnum : map.keySet()) {
			AnimatedSprite currentSprite = map.get(stateEnum).getSprite();
			if (currentSprite != null && map.get(stateEnum) != null && map.get(stateEnum).getRemainingTimeStr() != null
					&& !"".equals(map.get(stateEnum).getRemainingTimeStr())) {
				currentSprite.draw(canvas, DirectionConstants.RIGHT);
				canvas.drawText(map.get(stateEnum).getRemainingTimeStr(), currentSprite.getX() + 5,
						currentSprite.getY() + currentSprite.getHeight() + 20, paint);
			}
		}
	}

}
