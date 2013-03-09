package com.survivalkid.game.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.survivalkid.game.singleton.GameContext;

public class ChronoDisplayer {

	private Paint paint;
	private String stringToDisplay;
	private Point position;
	
	public ChronoDisplayer(int _x, int _y) {
		//Text
		paint = new Paint(); 
		paint.setTextSize(20);
		paint.setTypeface(GameContext.getSingleton().getFont());
		paint.setColor(Color.WHITE);

		position = new Point(_x,_y);
	}

	public void setTime(long timeInMillis) {
		Date date = new Date(timeInMillis);
		DateFormat formatter = new SimpleDateFormat("mm:ss,SSS");
		stringToDisplay = formatter.format(date);
	}
	
	public void draw(Canvas canvas) {
		canvas.drawText( stringToDisplay, position.x, position.y, paint);
	}
	
	public void setSize(float size) {
		paint.setTextSize(size);
	}

}
