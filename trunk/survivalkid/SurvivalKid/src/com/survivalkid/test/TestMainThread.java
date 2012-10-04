package com.survivalkid.test;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class TestMainThread extends Thread {
	/** Tag for logs. */
	private static final String TAG = TestMainThread.class.getSimpleName();

	// flag to hold game state
	private boolean running;
	private SurfaceHolder surfaceHolder;
	private TestGamePanel gameManager;

	public TestMainThread(SurfaceHolder surfaceHolder, TestGamePanel gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gameManager = gamePanel;
	}


	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void run() {
		Canvas canvas;
		Log.d(TAG, "Starting game loop");
		while (running) {
			canvas = null;
			// try locking the canvas for exclusive pixel editing on the surface
			try {
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					// update game state 
					this.gameManager.update();
					// draws the canvas on the panel
					this.gameManager.onDraw(canvas);
				}
			} finally {
				// in case of an exception the surface is not left in
				// an inconsistent state
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}	// end finally
		}
	}

}
