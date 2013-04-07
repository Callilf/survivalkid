package com.survivalkid.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.survivalkid.R;
import com.survivalkid.game.core.Constants;
import com.survivalkid.game.core.TouchHandler;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.thread.ActiveLoadingThread;
import com.survivalkid.game.util.BitmapUtil;
import com.survivalkid.game.util.DesignUtil;
import com.survivalkid.game.util.MoveUtil;
import com.survivalkid.menu.MainMenuActivity;

public class MainMenu extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = MainMenu.class.getSimpleName();

	private Bitmap background;
	
	private Bitmap mainTitle;
	private Rect mainTitleRect;
	
	private Bitmap playButton;
	private Rect playButtonRect;
	
	private Bitmap optionsButton;
	private Rect optionsButtonRect;
	
	private Bitmap htpButton;
	private Rect htpButtonRect;

	private MainMenuActivity activity;
//	private Paint paint;
	private boolean active;

	public MainMenu(Context _context) {
		super(_context);
		activity = (MainMenuActivity) _context;
		
		// Initialize the bitmapUtil
		BitmapUtil.initialize(getResources());

		// initialize of the context singleton
		GameContext.getSingleton().setContext(_context);
		GameContext.getSingleton().initSingleton();
		GameContext.getSingleton().setMainMenuActivity(activity);

		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);
		setWillNotDraw(false);
		
		//Launch the loader thread
		ActiveLoadingThread threadLoader = new ActiveLoadingThread();
		threadLoader.start();
		
		
		background = BitmapUtil.createBitmap(R.drawable.menu_background);
//		paint = DesignUtil.createTextPaint(Color.BLACK, 40, DesignUtil.TYPEFACE_MELOBDO, false);
		active = true;
		
		
		mainTitle = BitmapUtil.createBitmap(R.drawable.menu_main_title);
		playButton = BitmapUtil.createBitmap(R.drawable.menu_play_btn);
		optionsButton = BitmapUtil.createBitmap(R.drawable.menu_options_btn);
		htpButton = BitmapUtil.createBitmap(R.drawable.menu_htp_btn);
		initRectsPosition();
		
		if (Constants.DEBUG) {
			Log.d(TAG, "Start the game !");
		}
		create();
	}
	
	public void initRectsPosition() {
		mainTitleRect = BitmapUtil.buildRect(mainTitle, MoveUtil.BACKGROUND_LEFT + MoveUtil.BACKGROUND_WIDTH/2 - mainTitle.getWidth()/2, MoveUtil.BACKGROUND_TOP + MoveUtil.BACKGROUND_HEIGHT/20);
		playButtonRect = BitmapUtil.buildRect(playButton, MoveUtil.BACKGROUND_LEFT + MoveUtil.BACKGROUND_WIDTH/2 - playButton.getWidth()/2, MoveUtil.BACKGROUND_TOP + MoveUtil.BACKGROUND_HEIGHT/3);
		optionsButtonRect = BitmapUtil.buildRect(optionsButton, MoveUtil.BACKGROUND_LEFT + MoveUtil.BACKGROUND_WIDTH/2 - optionsButton.getWidth()/2, playButtonRect.bottom + MoveUtil.BACKGROUND_HEIGHT/20);
		htpButtonRect = BitmapUtil.buildRect(htpButton, MoveUtil.BACKGROUND_LEFT + MoveUtil.BACKGROUND_WIDTH/2 - htpButton.getWidth()/2, optionsButtonRect.bottom + MoveUtil.BACKGROUND_HEIGHT/20);
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
			DesignUtil.applyScaleRatio(canvas);
			DesignUtil.drawBackgroundImage(canvas, background, true);
			
			canvas.drawBitmap(mainTitle, mainTitleRect.left, mainTitleRect.top, null);
			canvas.drawBitmap(playButton, playButtonRect.left, playButtonRect.top, null);
			canvas.drawBitmap(optionsButton, optionsButtonRect.left, optionsButtonRect.top, null);
			canvas.drawBitmap(htpButton, htpButtonRect.left, htpButtonRect.top, null);
//			canvas.drawText("Touch to start", MoveUtil.SCREEN_WIDTH / 3, MoveUtil.SCREEN_HEIGHT / 2 - 10, paint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!active) {
			return false;
		}

		TouchHandler touchHandler = new TouchHandler(event, true);
		if (playButtonRect.contains(touchHandler.getX(), touchHandler.getY())) {
			activity.goToCharacterSelect();
			active = false;
		} else if (optionsButtonRect.contains(touchHandler.getX(), touchHandler.getY())) {
			activity.goToOptions();
			active = false;
		} else if (htpButtonRect.contains(touchHandler.getX(), touchHandler.getY())) {
			activity.goToHowToPlay();
			active = false;
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
