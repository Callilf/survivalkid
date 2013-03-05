package com.survivalkid.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.survivalkid.MainActivity;
import com.survivalkid.game.core.Constants.PersonageConstants;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.util.BitmapUtil;
import com.survivalkid.game.util.MoveUtil;

@SuppressLint("HandlerLeak") // TODO DELETE WHEN THE HANDLER WOULDN'T BE USE ANYMORE
public class StartMenu extends SurfaceView implements SurfaceHolder.Callback {
	
	private static final String TAG = StartMenu.class.getSimpleName();
	
	private MainActivity activity;
	private Paint paint;
	private Paint paint2;
	private boolean active;
	
	public StartMenu(Context _context) {
		super(_context);
		
		//Initialize the bitmapUtil
		BitmapUtil.initialize(getResources());
		
		// initialize of the context singleton
		GameContext.getSingleton().setContext(_context);
		GameContext.getSingleton().initSingleton();

		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);
		
		// make the GamePanel focusable so it can handle events
		setFocusable(true);
		
		activity = (MainActivity) _context;
		setWillNotDraw(false);
		
		paint = new Paint();
		paint.setTextSize(40);
		paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
		paint.setColor(Color.BLACK);
		
		paint2 = new Paint();
		paint2.setTextSize(30);
		paint2.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
		paint2.setColor(Color.BLACK);
		
		active = true;
		
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
			canvas.drawText("Touch to start", MoveUtil.SCREEN_WIDTH/3, MoveUtil.SCREEN_HEIGHT/2 - 10, paint);
			canvas.drawText("Play with Yugo", MoveUtil.SCREEN_WIDTH * 0.1f, MoveUtil.SCREEN_HEIGHT * 0.7f, paint2);
			canvas.drawText("Play with Yuna", MoveUtil.SCREEN_WIDTH * 0.7f, MoveUtil.SCREEN_HEIGHT * 0.7f, paint2);
		}
	}

	
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!active) {
			return false;
		}
		
		active = false;
		if(event.getX() <= MoveUtil.SCREEN_WIDTH/2) {
			activity.launchGame(PersonageConstants.PERSO_YUGO);
		} else {
			activity.launchGame(PersonageConstants.PERSO_YUNA);
		}

		return true;
	}

	/** Show an event in the LogCat view, for debugging
	private void dumpEvent(MotionEvent event) {
		String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_").append(names[actionCode]);
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
			sb.append("(pid ").append(action >> MoveUtil.ACTION_POINTER_INDEX_SHIFT);
			sb.append(")");
		}
		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++) {
			sb.append("#").append(i);
			sb.append("(pid ").append(event.getPointerId(i));
			sb.append(")=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if (i + 1 < event.getPointerCount())
				sb.append(";");
		}
		sb.append("]").append(characterManager.getCharacterList(0).getMoveManager().lastEnabledLeft);
		Log.d(TAG, sb.toString());
	} */

	
	
	
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
		//((Activity) getContext()).finish();
	}
}
