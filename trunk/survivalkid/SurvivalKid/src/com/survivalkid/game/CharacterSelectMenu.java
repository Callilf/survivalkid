package com.survivalkid.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.survivalkid.R;
import com.survivalkid.game.core.Constants.PersonageConstants;
import com.survivalkid.game.thread.ActiveLoadingThread;
import com.survivalkid.game.util.BitmapUtil;
import com.survivalkid.game.util.MoveUtil;
import com.survivalkid.menu.CharacterSelectMenuActivity;

public class CharacterSelectMenu extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = CharacterSelectMenu.class.getSimpleName();

	private Bitmap background;

	private CharacterSelectMenuActivity activity;
//	private Paint paint;
//	private Paint paint2;
	private boolean active;

	public CharacterSelectMenu(Context _context) {
		super(_context);

		// Initialize the bitmapUtil
//		BitmapUtil.initialize(getResources());

		// initialize of the context singleton
//		GameContext.getSingleton().setContext(_context);
//		GameContext.getSingleton().initSingleton();

		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);

		activity = (CharacterSelectMenuActivity) _context;
		setWillNotDraw(false);
		
		background = BitmapUtil.createBitmap(R.drawable.character_select);

//		paint = DesignUtil.createTextPaint(Color.BLACK, 40, DesignUtil.TYPEFACE_MELOBDO, false);
//		paint2 = DesignUtil.createTextPaint(Color.BLACK, 30, DesignUtil.TYPEFACE_MELOBDO, false);

		active = true;
		
		ActiveLoadingThread threadLoader = new ActiveLoadingThread();
		threadLoader.start();
		
		Log.d(TAG, "Start the game !");
		create();
	}

	/**
	 * Start the game.
	 * 
	 * @param context
	 */
	public void create() {

	}

	/**
	 * Restart the game
	 * 
	 * @return true if the game restart
	 */
	public void restart() {

	}

	@Override
	public void onDraw(Canvas canvas) {
		if (canvas != null) {
			// fills the canvas with black
			canvas.drawBitmap(background, MoveUtil.BACKGROUND_LEFT, MoveUtil.BACKGROUND_TOP, null);		
//			canvas.drawText("Select your character", MoveUtil.SCREEN_WIDTH / 6, MoveUtil.SCREEN_HEIGHT / 2 - 10, paint);
//			canvas.drawText("Play with Yugo", MoveUtil.SCREEN_WIDTH * 0.1f, MoveUtil.SCREEN_HEIGHT * 0.7f, paint2);
//			canvas.drawText("Play with Yuna", MoveUtil.SCREEN_WIDTH * 0.6f, MoveUtil.SCREEN_HEIGHT * 0.7f, paint2);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!active) {
			return false;
		}

		active = false;
		if (event.getX() <= MoveUtil.SCREEN_WIDTH / 2) {
			activity.launchGame(PersonageConstants.PERSO_YUGO);
		} else {
			activity.launchGame(PersonageConstants.PERSO_YUNA);
		}

		return true;
	}

	// ------------------------------------------------------------------------
	// Surface managing
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		active = true;
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		//setWillNotDraw(true);
	}

	/** Action when clicking on the BACK button. */
	public void unpause() {
		//setWillNotDraw(false);
	}

	/** Action when clicking on the BACK button. */
	public void stop() {
		//setWillNotDraw(true);
		// ((Activity) getContext()).finish();
	}
}
