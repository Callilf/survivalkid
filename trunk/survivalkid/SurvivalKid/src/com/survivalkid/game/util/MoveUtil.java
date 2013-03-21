package com.survivalkid.game.util;

import static com.survivalkid.game.thread.MainThread.FPS_RATIO;
import static java.lang.Math.round;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;

import com.survivalkid.R;
import com.survivalkid.game.core.ActionButton;
import com.survivalkid.game.data.ButtonPosition;
import com.survivalkid.game.entity.personage.Bag;
import com.survivalkid.game.singleton.GameContext;

public final class MoveUtil {
	
	private MoveUtil() {
		// static class, the constructor can't be called
	};
		
	/** the display of the activity */
	private static final Display display = GameContext.getSingleton().getDisplay();
	
	// to be compatible with android 2.1
	@SuppressWarnings("deprecation")
	public static final int ACTION_POINTER_INDEX_SHIFT = (Build.VERSION.SDK_INT > Build.VERSION_CODES.ECLAIR_MR1)? 
			MotionEvent.ACTION_POINTER_INDEX_SHIFT:MotionEvent.ACTION_POINTER_ID_SHIFT;
	
	public static boolean HAS_MULTITOUCH;

	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	
	public static int BACKGROUND_WIDTH;
	public static int BACKGROUND_HEIGHT;

	public static int BACKGROUND_LEFT;
	public static int BACKGROUND_RIGHT;
	public static int BACKGROUND_TOP;
	public static int BACKGROUND_BOTTOM;
	
	public static int GROUND;
	
	public static float RATIO_WIDTH;
	public static float RATIO_HEIGHT;
	
	public static final int NORMALIZE_WIDTH = 800;
	public static final int NORMALIZE_HEIGHT = 480;
	
	static {
		buttonPosition = new ButtonPosition();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		SCREEN_WIDTH = metrics.widthPixels;
		SCREEN_HEIGHT = metrics.heightPixels;
		
		Bitmap ground = BitmapUtil.createBitmap(R.drawable.ground);
		BACKGROUND_WIDTH = ground.getWidth();
		BACKGROUND_HEIGHT = ground.getHeight();		
		//BACKGROUND_WIDTH = SCREEN_WIDTH;
		//BACKGROUND_HEIGHT = SCREEN_HEIGHT;
		
		BACKGROUND_LEFT = (SCREEN_WIDTH - BACKGROUND_WIDTH)/2;
		BACKGROUND_RIGHT = BACKGROUND_LEFT + BACKGROUND_WIDTH;
		BACKGROUND_TOP = (SCREEN_HEIGHT - BACKGROUND_HEIGHT)/2;
		BACKGROUND_BOTTOM = BACKGROUND_TOP + BACKGROUND_HEIGHT;
		
		
		
		// in case of the width and height are exchanged
		if (BACKGROUND_WIDTH < BACKGROUND_HEIGHT) {
			int tmp = BACKGROUND_WIDTH;
			BACKGROUND_WIDTH = BACKGROUND_HEIGHT;
			BACKGROUND_HEIGHT = tmp;
		}
		
		RATIO_WIDTH = BACKGROUND_WIDTH/(float)NORMALIZE_WIDTH;
		RATIO_HEIGHT = BACKGROUND_HEIGHT/(float)NORMALIZE_HEIGHT;
		
		GROUND = (int) (BACKGROUND_HEIGHT - 40*RATIO_HEIGHT) + BACKGROUND_TOP;
		
		/* new API not used
		Point size = new Point();
		display.getSize(size);
		BACKGROUND_WIDTH = size.x;
		BACKGROUND_HEIGHT = size.y;
		 */
		
	}

	//Buttons
	public static ButtonPosition buttonPosition;
	public static ActionButton btn_left;
	public static ActionButton btn_right;
	public static ActionButton btn_up;
	public static Bag virtualBag = null;
	
	public static void initializeButton() {
		btn_left = new ActionButton(BitmapUtil.createBitmap(R.drawable.arrow_left), BitmapUtil.createBitmap(R.drawable.arrow_left_pressed));
		btn_right = new ActionButton(BitmapUtil.createBitmap(R.drawable.arrow_right), BitmapUtil.createBitmap(R.drawable.arrow_right_pressed));
		btn_up = new ActionButton(BitmapUtil.createBitmap(R.drawable.arrow_up), BitmapUtil.createBitmap(R.drawable.arrow_up_pressed));
		initializePositionButton();
		virtualBag = new Bag(null);
	}
	
	public static void initializePositionButton() {
		buttonPosition.init(); // do AFTER initialized the other button
		btn_left.setPosition(buttonPosition.getLeftButton());
		btn_right.setPosition(buttonPosition.getRightButton());
		btn_up.setPosition(buttonPosition.getUpButton());
	}
	
	public static int normX(int x) {
		return round(x*RATIO_WIDTH*FPS_RATIO);
	}
	public static int normY(int y) {
		return round(y*RATIO_HEIGHT*FPS_RATIO);
	}
	
	public static int getRandomPositionX() {
		return (int)(Math.random() * BACKGROUND_WIDTH) + BACKGROUND_LEFT;
	}
	
	public static int getRandomPositionY() {
		return (int)(Math.random() * (GROUND - BACKGROUND_TOP)) + BACKGROUND_TOP;
	}	
	
}
