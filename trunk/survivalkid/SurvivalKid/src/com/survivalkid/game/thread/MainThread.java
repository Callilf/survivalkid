package com.survivalkid.game.thread;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import com.survivalkid.game.GameManager;

public class MainThread extends Thread {
	/** Tag for logs. */
	private static final String TAG = MainThread.class.getSimpleName();

	// flag to hold game state
	private boolean running;
	private boolean pause = false;
	private SurfaceHolder surfaceHolder;
	private GameManager gameManager;
	private Canvas canvas;

	public MainThread(SurfaceHolder surfaceHolder, GameManager gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gameManager = gamePanel;
		this.canvas = null;
	}


	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public boolean isPause() {
		return pause;
	}	
	
	public void setPause(boolean _pause) {
		if (pause && !_pause) {
			pause = _pause;
			synchronized (this) {
				notify();
			}
		}
		else {
			this.pause = _pause;
		}
	}

	@Override
	public void run() {
		Log.d(TAG, "Starting game loop");
		while (running) {
			while(!pause && running) {
				// try locking the canvas for exclusive pixel editing on the surface
				try {
					canvas = this.surfaceHolder.lockCanvas();
					synchronized (surfaceHolder) {
						// update game state 
						this.gameManager.update();
						// draws the canvas on the panel
						this.gameManager.onDraw(canvas);
					}
				}
				catch (IllegalMonitorStateException e) {
					Log.d(TAG, e.getMessage());
					throw e;
				} finally {
					// in case of an exception the surface is not left in
					// an inconsistent state
					if (canvas != null) {
						surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}	// end finally
			}
			if (running) {
				try {
					synchronized (this) {
						wait();
					}
				} catch (InterruptedException e) {
					Log.d(TAG, e.getMessage());
				}
			}
		}
	}

}