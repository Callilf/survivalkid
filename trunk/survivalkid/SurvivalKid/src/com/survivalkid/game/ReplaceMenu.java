package com.survivalkid.game;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.survivalkid.AbstractSurfaceView;
import com.survivalkid.game.core.Constants;
import com.survivalkid.game.core.SurfaceHandler;
import com.survivalkid.game.util.CollisionUtil;
import com.survivalkid.game.util.MoveUtil;

/**
 * Menu to replace the button position
 * 
 * @author Thomas
 */
public class ReplaceMenu extends AbstractSurfaceView {

	private static final String TAG = ReplaceMenu.class.getSimpleName();

	SurfaceHandler surfaceHandler;
	
	private boolean isChanged; 
	
	public ReplaceMenu(Context _context) {
		super(_context);
		setTagParent(TAG);

		getHolder().addCallback(this);

		surfaceHandler = new SurfaceHandler(this);
		// make the GamePanel focusable so it can handle events
		setFocusable(true);

		//setWillNotDraw(false);
		isChanged = false;
		
		if (Constants.DEBUG) {
			Log.d(TAG, "Start the replace button menu !");
		}
		CollisionUtil.showHitbox(true);
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
			surfaceHandler.drawBackgroundAndButton();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (MoveUtil.buttonPosition.manageEventChangePosition(event)) {
			surfaceHandler.drawBackgroundAndButton();
			isChanged = true;
		}

		return true;
	}

	public void drawScreen() {
		surfaceHandler.drawBackgroundAndButton();
	}
	
	public void terminate() {
		if (isChanged) {
			MoveUtil.buttonPosition.savePosition();
		}
		CollisionUtil.showHitbox(null);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		super.surfaceCreated(holder);
		drawScreen();
	}

}
