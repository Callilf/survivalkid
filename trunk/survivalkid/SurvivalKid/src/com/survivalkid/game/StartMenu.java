package com.survivalkid.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.survivalkid.MenuActivity;
import com.survivalkid.game.core.Constants.PersonageConstants;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.thread.ActiveLoadingThread;
import com.survivalkid.game.util.BitmapUtil;
import com.survivalkid.game.util.MoveUtil;

public class StartMenu extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = StartMenu.class.getSimpleName();

	private MenuActivity activity;
	private Paint paint;
	private Paint paint2;
	private boolean active;

	public StartMenu(Context _context) {
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

		activity = (MenuActivity) _context;
		setWillNotDraw(false);

		Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/MELODBO.TTF");

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(40);
		paint.setTypeface(tf);
		paint.setColor(Color.BLACK);

		paint2 = new Paint();
		paint2.setAntiAlias(true);
		paint2.setTextSize(30);
		paint2.setTypeface(tf);
		paint2.setColor(Color.BLACK);

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
			canvas.drawColor(Color.GRAY);
			canvas.drawText("Touch to start", MoveUtil.SCREEN_WIDTH / 3, MoveUtil.SCREEN_HEIGHT / 2 - 10, paint);
			canvas.drawText("Play with Yugo", MoveUtil.SCREEN_WIDTH * 0.1f, MoveUtil.SCREEN_HEIGHT * 0.7f, paint2);
			canvas.drawText("Play with Yuna", MoveUtil.SCREEN_WIDTH * 0.6f, MoveUtil.SCREEN_HEIGHT * 0.7f, paint2);
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
