package com.survivalkid.game.entity;

import com.survivalkid.game.entity.Life.EnumLife;

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
	private EnumLife typeChange;
	
	private Point position;
	
	public LifeChangeDisplayer(int _damages, EnumLife _typeChange, long _beginTime, int _x, int _y) {
		//Text
		paint = new Paint(); 
		paint.setTextSize(20);
		paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
		
		typeChange = _typeChange;
		displayDamagesBeginTime = _beginTime;
		damageTaken = _damages * typeChange.getFacteur();
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
		String damageSring = damageTaken + typeChange.getUnite();
		canvas.drawText( (damageTaken > 0) ? "+" + damageSring : damageSring, position.x, position.y, paint);
	}

	public boolean isOver() {
		return over;
	}

	public void setOver(boolean over) {
		this.over = over;
	}
}
