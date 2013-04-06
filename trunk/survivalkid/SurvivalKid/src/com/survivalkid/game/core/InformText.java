package com.survivalkid.game.core;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.survivalkid.game.util.DesignUtil;
import com.survivalkid.game.util.MoveUtil;

public class InformText implements DynamicDrawableObject {

	private enum Situation {
		FADEIN, STABLE, FADEOUT, END;
	}
	
	private Paint paint;
	private String text;
	
	private int changeAlpha;
	private int fullAlpha;
	private int currentAlpha;
	
	private float relativePosition;
	private Situation situation;
	private long timeStartStable;
	
	/**
	 * Create an inform text which fadein and fadeout in front on the top of the screen
	 * 
	 * @param text the text
	 * @param relativePosition the relative position (between 0 and 1)
	 */
	public InformText(String text, float relativePosition) {
		this(text, relativePosition, DesignUtil.createTextPaint(Color.BLACK, 20));
	}
	
	/**
	 * Create an inform text
	 * 
	 * @param text the text
	 * @param relativePosition the relative position (between 0 and 1)
	 * @param paint the paint
	 */
	public InformText(String text, float relativePosition, Paint paint) {
		this.text = text;
		this.paint = paint;
		this.relativePosition = relativePosition;
		situation = Situation.FADEIN;
		currentAlpha = 0;
	}
	
	/**
	 * Init the text Informe
	 * 
	 * @param speedChangeAlpha the augmentation of alpha on each frame
	 * @param durationFullAlpha the duration in milliseconds of the text with full alpha
	 */
	public void initInform(int speedChangeAlpha, int durationFullAlpha) {
		changeAlpha = speedChangeAlpha;
		fullAlpha = durationFullAlpha;
	}
	
	public void update(long gameDuration) {
		switch (situation) {
		case FADEIN:
			currentAlpha += changeAlpha;
			if (currentAlpha >= 255) {
				currentAlpha = 255;
				situation = Situation.STABLE;
				timeStartStable = gameDuration;
			}
			break;
		case FADEOUT:
			currentAlpha -= changeAlpha;
			if (currentAlpha <= 0) {
				currentAlpha = 0;
				situation = Situation.END;
			}
			break;
		case STABLE:
			if (gameDuration - fullAlpha >= timeStartStable) {
				situation = Situation.FADEOUT;
			}
			break;
		default:
			break;
		}
		paint.setAlpha(currentAlpha);
	}
	
	public void draw(Canvas canvas) {
		canvas.drawText(text, MoveUtil.BACKGROUND_LEFT + MoveUtil.BACKGROUND_WIDTH * relativePosition, MoveUtil.BACKGROUND_TOP + MoveUtil.BACKGROUND_HEIGHT * 0.05f, paint);
	}

	public boolean isDead() {
		return situation == Situation.END;
	}
	
}
