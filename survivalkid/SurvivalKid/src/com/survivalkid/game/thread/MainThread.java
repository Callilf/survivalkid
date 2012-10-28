package com.survivalkid.game.thread;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import com.survivalkid.game.GameManager;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.util.TimerUtil;

public class MainThread extends Thread {
	/** Tag for logs. */
	private static final String TAG = MainThread.class.getSimpleName();

	/** indicative FPS to compute the speed */
	private static final int FPS_INDICATIVE = 60;
	
	/** real minimum FPS of the game */	
	private static final int FPS_REAL = 30;
	
	/** ratio of the indicative fps and the real fps */
	public static final float FPS_RATIO = FPS_INDICATIVE/(float)FPS_REAL;
	
	private static final int FRAME_DURATION = 1000/FPS_REAL;
	
	// flag to hold game state
	private boolean running;
	private boolean pause = false;
	private boolean endGame = false;
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
	
	public void setEndGame(boolean _endGame) {
		endGame = _endGame;
		setPause(_endGame);
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
			if (!endGame) {
				while(!pause && running) {
					// try locking the canvas for exclusive pixel editing on the surface
					try {
						long time = System.currentTimeMillis();
						canvas = this.surfaceHolder.lockCanvas();
						synchronized (surfaceHolder) {
							launchUpdateAndDrawTimed();
						}
						// normalization of the duration of a frame
						long timePassed = System.currentTimeMillis() - time;
						if (timePassed < FRAME_DURATION) {
							synchronized (this) {
								wait(FRAME_DURATION-timePassed);
							}
						}
						Log.d("TEST", ""+timePassed);
						GameContext.getSingleton().gameDuration += FRAME_DURATION;
					}
					catch (IllegalMonitorStateException e) {
						Log.d(TAG, e.getMessage());
						throw e;
					} catch (InterruptedException e) {
						// for the wait
						Log.d(TAG, e.getMessage());
					} finally {
						// in case of an exception the surface is not left in
						// an inconsistent state
						if (canvas != null) {
							surfaceHolder.unlockCanvasAndPost(canvas);
						}
					}	// end finally
				}
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
	
	private void launchUpdateAndDraw() {
		// update game state 
		this.gameManager.update();
		// draws the canvas on the panel
		this.gameManager.onDraw(canvas);
	}
	
	private void launchUpdateAndDrawTimed() {
		if (!TimerUtil.TIMER_ACTIVE) {
			launchUpdateAndDraw();
			return;
		}
		// update game state 
		TimerUtil.start("_globalupdate");
		this.gameManager.update();
		TimerUtil.end("_globalupdate");
		// draws the canvas on the panel
		TimerUtil.start("_globaldraw");
		this.gameManager.onDraw(canvas);
		TimerUtil.end("_globaldraw");
	}	

}
