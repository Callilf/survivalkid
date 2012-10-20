package com.survivalkid.game.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;

public class LifeChangeDisplayer {

	private Paint paint;
	private Paint recoverPaint;
	private int damageTaken;
	private int displayDamagesDuration = 1000;
	private long displayDamagesBeginTime;
	private boolean over;
	
	private Point position;
	
	public LifeChangeDisplayer(int _damages, long _beginTime, int _x, int _y) {
		//Text
		paint = new Paint(); 
		paint.setTextSize(20);
		paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
		
		
		displayDamagesBeginTime = _beginTime;
		damageTaken = _damages;
		position = new Point(_x,_y);
		setOver(false);
	}
	
	public void update(long gameTime) {
		if(gameTime - displayDamagesBeginTime >= displayDamagesDuration) {
			over = true;
		} else {
			position.offset(0, -1);
		}
	}
	
	public void draw(Canvas canvas) {
		paint.setColor( damageTaken < 0 ? Color.RED : Color.GREEN);
		canvas.drawText( (damageTaken > 0) ? "+" + damageTaken : String.valueOf(damageTaken), position.x, position.y, paint);
	}

	public boolean isOver() {
		return over;
	}

	public void setOver(boolean over) {
		this.over = over;
	}
}
