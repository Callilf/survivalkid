package com.survivalkid.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.survivalkid.EndActivity;
import com.survivalkid.R;
import com.survivalkid.game.util.BitmapUtil;

@SuppressLint("HandlerLeak")
// TODO DELETE WHEN THE HANDLER WOULDN'T BE USE ANYMORE
public class EndMenu extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = EndMenu.class.getSimpleName();

	private EndActivity activity;
	private Bitmap panel;

	public EndMenu(Context _context) {
		super(_context);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);

		activity = (EndActivity) _context;
		setWillNotDraw(false);

		Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/MELODBO.TTF");
		
		panel = BitmapUtil.createBitmap(R.drawable.test_end_panel2);
		
		Log.d(TAG, "End menu displayed !");
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
		canvas.drawBitmap(panel, 0,0, null);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return true;
	}

	/**
	 * Show an event in the LogCat view, for debugging private void
	 * dumpEvent(MotionEvent event) { String names[] = { "DOWN", "UP", "MOVE",
	 * "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
	 * StringBuilder sb = new StringBuilder(); int action = event.getAction();
	 * int actionCode = action & MotionEvent.ACTION_MASK;
	 * sb.append("event ACTION_").append(names[actionCode]); if (actionCode ==
	 * MotionEvent.ACTION_POINTER_DOWN || actionCode ==
	 * MotionEvent.ACTION_POINTER_UP) { sb.append("(pid ").append(action >>
	 * MoveUtil.ACTION_POINTER_INDEX_SHIFT); sb.append(")"); } sb.append("[");
	 * for (int i = 0; i < event.getPointerCount(); i++) {
	 * sb.append("#").append(i);
	 * sb.append("(pid ").append(event.getPointerId(i));
	 * sb.append(")=").append((int) event.getX(i)); sb.append(",").append((int)
	 * event.getY(i)); if (i + 1 < event.getPointerCount()) sb.append(";"); }
	 * sb.
	 * append("]").append(characterManager.getCharacterList(0).getMoveManager(
	 * ).lastEnabledLeft); Log.d(TAG, sb.toString()); }
	 */

	// ------------------------------------------------------------------------
	// Surface managing
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
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

	/**
	 * @return the panel
	 */
	public Bitmap getPanel() {
		return panel;
	}

	/**
	 * @param panel the panel to set
	 */
	public void setPanel(Bitmap panel) {
		this.panel = panel;
	}
	
	
}
