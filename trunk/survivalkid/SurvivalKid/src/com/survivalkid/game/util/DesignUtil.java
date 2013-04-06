package com.survivalkid.game.util;

import static com.survivalkid.game.util.MoveUtil.RESCALING_ACTIVE;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

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
	
	private static Resources resources;
	
	static {
		PAINT_HITBOX_ENTITY.setARGB(128, 255, 0, 0);
		PAINT_HITBOX_ACTIONBUTTON.setARGB(50, 0, 0, 128);
		PAINT_HITBOX_OVERLAPPING.setARGB(255, 200, 20, 20);
		PAINT_HITBOX_BALLON.setARGB(200, 0, 0, 255);
		PAINT_LIFE.setARGB(255, 200, 20, 20);
		
		PAINT_CIRCULARSAW_POINT.setARGB(128, 0, 0, 0);
		
		resources = GameContext.getSingleton().getContext().getResources();
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
	
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
	
	public static void applyScaleRatio(Canvas canvas) {
		if (RESCALING_ACTIVE) {
			canvas.scale(MoveUtil.RATIO_RESCALING_WIDTH, MoveUtil.RATIO_RESCALING_HEIGHT);
		}
	}
	
	/**
	 * Draw an background image and center the image in the screen
	 * 
	 * @param canvas the canvas
	 * @param image the image
	 * @param isVerticalCenter true to center the image in vertical position. False to put the floor on the bottom of the screen
	 */
	public static void drawBackgroundImage(Canvas canvas, Bitmap image, boolean isVerticalCenter) {
		canvas.drawColor(Color.BLUE);
		int left = MoveUtil.BACKGROUND_LEFT;
		int top = MoveUtil.BACKGROUND_TOP;
		
		// put the bottom at the bottom and hide the top
		if (image.getHeight() > MoveUtil.BACKGROUND_HEIGHT) {
			if (isVerticalCenter) {
				top = (MoveUtil.BACKGROUND_HEIGHT - image.getHeight()) / 2;
			}
			else {
				top = MoveUtil.BACKGROUND_HEIGHT - image.getHeight();
			}
		}
		
		// center the image in horizontal
		if (image.getWidth() > MoveUtil.BACKGROUND_WIDTH) {
			left = (MoveUtil.BACKGROUND_WIDTH - image.getWidth())/2;
		}
		
		canvas.drawBitmap(image, left, top, null);				
	}
	
	/**
	 * Get a string from the resources
	 * 
	 * @param id the id of the string
	 * @return the textual string
	 */
	public static String getString(int id) {
		return resources.getString(id);
	}
}
