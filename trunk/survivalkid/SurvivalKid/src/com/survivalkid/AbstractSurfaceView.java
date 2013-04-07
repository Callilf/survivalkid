package com.survivalkid;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.survivalkid.game.core.Constants;

public abstract class AbstractSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = AbstractSurfaceView.class.getSimpleName();
	
	private String tagParent;
	
	public AbstractSurfaceView(Context context) {
		super(context);
	}

	/**
	 * Redefine onDraw as public
	 */
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	// ------------------------------------------------------------------------
	// Surface managing
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (Constants.DEBUG) {
			Log.d(TAG, tagParent + " - Surface is being created");
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		if (Constants.DEBUG) {
			Log.d(TAG, tagParent + " - Surface is being destroyed");
		}
	}
	// ------------------------------------------------------------------------
	
	public void setTagParent(String tagParent) {
		this.tagParent = tagParent;
	}
}
