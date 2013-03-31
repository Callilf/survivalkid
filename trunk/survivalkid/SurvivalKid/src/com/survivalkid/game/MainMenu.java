package com.survivalkid.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.survivalkid.MainMenuActivity;
import com.survivalkid.R;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.thread.ActiveLoadingThread;
import com.survivalkid.game.util.BitmapUtil;
import com.survivalkid.game.util.DesignUtil;
import com.survivalkid.game.util.MoveUtil;

public class MainMenu extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = MainMenu.class.getSimpleName();

	private Bitmap background;

	private MainMenuActivity activity;
	private Paint paint;
	private boolean active;

	public MainMenu(Context _context) {
		super(_context);

		// Initialize the bitmapUtil
		BitmapUtil.initialize(getResources());

		// initialize of the context singleton
		GameContext.getSingleton().setContext(_context);
		GameContext.getSingleton().initSingleton();

		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);

		activity = (MainMenuActivity) _context;
		setWillNotDraw(false);
		
		background = BitmapUtil.createBitmap(R.drawable.menu_background);

		paint = DesignUtil.createTextPaint(Color.BLACK, 40, DesignUtil.TYPEFACE_MELOBDO, false);

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
			canvas.drawText("Touch to start", MoveUtil.SCREEN_WIDTH / 3, MoveUtil.SCREEN_HEIGHT / 2 - 10, paint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!active) {
			return false;
		}

		activity.goToCharacterSelect();
		active = false;

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
		setWillNotDraw(true);
	}

	/** Action when clicking on the BACK button. */
	public void unpause() {
		setWillNotDraw(false);
	}

	/** Action when clicking on the BACK button. */
	public void stop() {
		setWillNotDraw(true);
		// ((Activity) getContext()).finish();
	}
}
