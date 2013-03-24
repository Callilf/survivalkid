package com.survivalkid.game.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.survivalkid.game.util.DesignUtil;
import com.survivalkid.game.util.MoveUtil;

public class ChronoDisplayer {
	private static final DateFormat formatter = new SimpleDateFormat("mm:ss,SSS");

	private Paint paint;
	private String stringToDisplay;
	private Point position;
	
	public ChronoDisplayer(int _x, int _y) {
		//Text
		paint = DesignUtil.createTextPaint(Color.WHITE, 20); 
		stringToDisplay = "";

		position = new Point(_x,_y);
	}

	public void setTime(long timeInMillis) {
		Date date = new Date(timeInMillis);
		stringToDisplay = formatter.format(date);
	}
	
	public void draw(Canvas canvas) {
		canvas.drawText(stringToDisplay, position.x, position.y, paint);
	}
	
	public void setSize(float size) {
		paint.setTextSize(size / MoveUtil.RATIO_RESCALING_WIDTH);
	}

}
