package com.survivalkid.game.util;

import android.graphics.Paint;
import android.graphics.Typeface;

import com.survivalkid.game.core.Constants.PreferencesConstants;
import com.survivalkid.game.singleton.GameContext;

public final class DesignUtil {

	public static final Typeface TYPEFACE_MELOBDO = Typeface.createFromAsset(GameContext.getSingleton().getContext().getAssets(), "fonts/MELODBO.TTF");
	
	public static final boolean ANTIALIAS_ENABLED = PrefsUtil.getPrefs().getBoolean(PreferencesConstants.ANTIALIAS_TEXT, true);
	
	public static final Paint PAINT_HITBOX_ENTITY = new Paint();
	public static final Paint PAINT_HITBOX_ACTIONBUTTON = new Paint();
	public static final Paint PAINT_HITBOX_OVERLAPPING = new Paint();
	public static final Paint PAINT_HITBOX_BALLON = new Paint();
	public static final Paint PAINT_LIFE = new Paint();
	public static final Paint PAINT_CIRCULARSAW_POINT = new Paint();
	
	static {
		PAINT_HITBOX_ENTITY.setARGB(128, 255, 0, 0);
		PAINT_HITBOX_ACTIONBUTTON.setARGB(50, 0, 0, 128);
		PAINT_HITBOX_OVERLAPPING.setARGB(255, 200, 20, 20);
		PAINT_HITBOX_BALLON.setARGB(200, 0, 0, 255);
		PAINT_LIFE.setARGB(255, 200, 20, 20);
		
		PAINT_CIRCULARSAW_POINT.setARGB(128, 0, 0, 0);
	}
	
	private DesignUtil() {
		// static class util
	}

	/**
	 * Initialize a new paint with the default typeFace
	 * 
	 * @param textColor the color (use Color.X)
	 * @param textSize the text size
	 * 
	 * @return a new paint
	 */
	public static Paint createTextPaint(int color, int size) {
		return createTextPaint(color, size, TYPEFACE_MELOBDO, true);
	}
	
	/**
	 * Initialize a new paint
	 * 
	 * @param textColor the color (use Color.X)
	 * @param textSize the text size
	 * @param typeFace the typeface
	 * @param withScaling true to active the scaling with the game. False otherwise (exemple : for the menu)
	 * 
	 * @return a new paint
	 */
	public static Paint createTextPaint(int color, int size, Typeface typeFace, boolean withScaling) {
		Paint paint = new Paint();
		paint.setAntiAlias(ANTIALIAS_ENABLED);
		paint.setTypeface(typeFace);
		paint.setColor(color);
		setPaintTextSize(paint, size, withScaling);
		return paint;
	}
	
	public static void setPaintTextSize(Paint paint, float size, boolean withScaling) {
		if (withScaling) {
			paint.setTextSize(size * MoveUtil.RATIO_WIDTH);		
		}
		else {
			paint.setTextSize(size * MoveUtil.RATIO_WIDTH * MoveUtil.RATIO_RESCALING_WIDTH);
		}		
	}
}
