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
import com.survivalkid.game.core.Constants.PreferencesConstants;
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
	
	public static boolean RESCALING_ACTIVE = PrefsUtil.getPrefs().getBoolean(PreferencesConstants.RESCALING_ENABLED, true);;

	// screen size
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	
	// virtual screen size to put the object which need to fit the edge of the screen
	public static int SCREEN_VIRTUAL_WIDTH;
	public static int SCREEN_VIRTUAL_HEIGHT;
	
	// dimension of the background image
	public static int BACKGROUND_WIDTH;
	public static int BACKGROUND_HEIGHT;

	// position of the background image
	public static int BACKGROUND_LEFT;
	public static int BACKGROUND_RIGHT;
	public static int BACKGROUND_TOP;
	public static int BACKGROUND_BOTTOM;
	
	// position of the ground
	public static int GROUND;
	
	// ratio of the game (actual screen size by real screen size)
	public static float RATIO_WIDTH;
	public static float RATIO_HEIGHT;
	
	// ratio of the game to scaling the game to the resolution of the screen (if the screen is greater than the background)
	public static float RATIO_RESCALING_WIDTH;
	public static float RATIO_RESCALING_HEIGHT;
	
	// normalize dimension that determines the speed of the elements as if the screen would have these dimension
	public static final int NORMALIZE_WIDTH = 800;
	public static final int NORMALIZE_HEIGHT = 480;
	
	static {
		buttonPosition = new ButtonPosition();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		
		SCREEN_WIDTH = metrics.widthPixels;
		SCREEN_HEIGHT = metrics.heightPixels;
		
		// in case of the width and height are exchanged
		if (SCREEN_WIDTH < SCREEN_HEIGHT) {
			int tmp = SCREEN_WIDTH;
			SCREEN_WIDTH = SCREEN_HEIGHT;
			SCREEN_HEIGHT = tmp;
		}
		
		Bitmap ground = BitmapUtil.createBitmap(R.drawable.ground);
		BACKGROUND_WIDTH = Math.min(ground.getWidth(), SCREEN_WIDTH);
		BACKGROUND_HEIGHT = Math.min(ground.getHeight(), SCREEN_HEIGHT);	
		
		RATIO_WIDTH = BACKGROUND_WIDTH/(float)NORMALIZE_WIDTH;
		RATIO_HEIGHT = BACKGROUND_HEIGHT/(float)NORMALIZE_HEIGHT;
		
		RATIO_RESCALING_WIDTH = SCREEN_WIDTH/(float)BACKGROUND_WIDTH;
		RATIO_RESCALING_HEIGHT = SCREEN_HEIGHT/(float)BACKGROUND_HEIGHT;
		
		if (RESCALING_ACTIVE) {
			setScreenInCorner();
		}
		else {
			setScreenCenter();
		}

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
	
	public static void setScreenInCorner() {
		BACKGROUND_LEFT = 0;
		BACKGROUND_RIGHT = BACKGROUND_WIDTH;
		BACKGROUND_TOP = 0;
		BACKGROUND_BOTTOM = BACKGROUND_HEIGHT;
		SCREEN_VIRTUAL_WIDTH = BACKGROUND_WIDTH;
		SCREEN_VIRTUAL_HEIGHT = BACKGROUND_HEIGHT;
		GROUND = (int) (BACKGROUND_HEIGHT - 40*RATIO_HEIGHT) + BACKGROUND_TOP;
	}
	
	public static void setScreenCenter() {
		SCREEN_VIRTUAL_WIDTH = SCREEN_WIDTH;
		SCREEN_VIRTUAL_HEIGHT = SCREEN_HEIGHT;
		BACKGROUND_LEFT = (SCREEN_WIDTH - BACKGROUND_WIDTH)/2;
		BACKGROUND_RIGHT = BACKGROUND_LEFT + BACKGROUND_WIDTH;
		BACKGROUND_TOP = (SCREEN_HEIGHT - BACKGROUND_HEIGHT)/2;
		BACKGROUND_BOTTOM = BACKGROUND_TOP + BACKGROUND_HEIGHT;
		GROUND = (int) (BACKGROUND_HEIGHT - 40*RATIO_HEIGHT) + BACKGROUND_TOP;
	}
	
	public static void changeScalingMode() {
		RESCALING_ACTIVE = !RESCALING_ACTIVE;
		if (RESCALING_ACTIVE) {
			setScreenInCorner();
		}
		else {
			setScreenCenter();
		}
		initializePositionButton();
		PrefsUtil.setPrefs(boolean.class, PreferencesConstants.RESCALING_ENABLED, RESCALING_ACTIVE);
	}
	
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
	
	public static int normTouchX(float x) {
		if (RESCALING_ACTIVE) {
			return (int)(x / RATIO_RESCALING_WIDTH);
		}
		return (int) x ;
	}
	
	public static int normTouchY(float y) {
		if (RESCALING_ACTIVE) {
			return (int)(y / RATIO_RESCALING_HEIGHT);
		}
		return (int) y ;
	}
	
	/**
	 * @return a random X position in the game (from left to right)
	 */
	public static int getRandomPositionX() {
		return getRandomPositionX(1);
	}
	
	/**
	 * @return a random Y position in the game (from ground to top)
	 */
	public static int getRandomPositionY() {
		return getRandomPositionY(1);
	}
	
	/**
	 * @param marginPct margin in percentage
	 * @return a random X position in the game (from left to right), excluded the marginPct of each side
	 */
	public static int getRandomPositionX(int marginPct) {
		int margin = BACKGROUND_WIDTH * marginPct / 100;
		return (int)(Math.random() * (BACKGROUND_WIDTH - 2 * margin)) + BACKGROUND_LEFT + margin;
	}
	
	/**
	 * @param marginPct margin in percentage
	 * @return a random Y position in the game (from ground to top), excluded the marginPct of each side
	 */
	public static int getRandomPositionY(int marginPct) {
		int margin = (GROUND - BACKGROUND_TOP) * marginPct / 100;
		return (int)(Math.random() * (GROUND - BACKGROUND_TOP - 2 * margin)) + BACKGROUND_TOP + margin;
	}
	
}
