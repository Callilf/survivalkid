package com.survivalkid.game.util;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Display;

import com.survivalkid.R;
import com.survivalkid.game.core.ActionButton;
import com.survivalkid.game.singleton.GameContext;

public final class MoveUtil {
	
	private MoveUtil() {
		// static class, the constructor can't be called
	};
		
	/** the display of the activity */
	private static final Display display = GameContext.getSingleton().getDisplay();

	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static int GROUND;
	
	static {
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		SCREEN_WIDTH = metrics.widthPixels;
		SCREEN_HEIGHT = metrics.heightPixels;
		GROUND = SCREEN_HEIGHT - 40;
		
		/* new API not used
		Point size = new Point();
		display.getSize(size);
		SCREEN_WIDTH = size.x;
		SCREEN_HEIGHT = size.y;
		 */
		
	}

	//Buttons
	public static ActionButton btn_left;
	public static ActionButton btn_right;
	public static ActionButton btn_up;
	
	public static void initializeButton(Resources resources) {
		btn_left = new ActionButton(BitmapFactory.decodeResource(resources, R.drawable.arrow_left), BitmapFactory.decodeResource(resources, R.drawable.arrow_left_pressed));
		btn_right = new ActionButton(BitmapFactory.decodeResource(resources, R.drawable.arrow_right), BitmapFactory.decodeResource(resources, R.drawable.arrow_right_pressed));
		btn_up = new ActionButton(BitmapFactory.decodeResource(resources, R.drawable.arrow_up), BitmapFactory.decodeResource(resources, R.drawable.arrow_up_pressed));
		int LEFT_X = (int) (SCREEN_WIDTH*0.03);
		btn_left.setPosition(LEFT_X, SCREEN_HEIGHT - btn_left.getHeight());
		btn_right.setPosition(LEFT_X + btn_left.getWidth()*2, SCREEN_HEIGHT - btn_right.getHeight());
		btn_up.setPosition(SCREEN_WIDTH - btn_up.getWidth() - btn_up.getWidth()/2, SCREEN_HEIGHT - btn_up.getHeight());		
	}
	
}
