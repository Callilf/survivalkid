package com.survivalkid.game.thread;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import com.survivalkid.game.GameManager;
import com.survivalkid.game.core.ChronoDisplayer;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.util.MoveUtil;
import com.survivalkid.game.util.TimerUtil;

public class MainThread extends Thread {

	private static final String TAG = MainThread.class.getSimpleName();

	/** indicative FPS to compute the speed */
	private static final int FPS_INDICATIVE = 70;
	
	/** real maximum FPS of the game */	
	private static final int FPS_REAL = 60;
	
	/** ratio of the indicative fps and the real fps */
	public static final float FPS_RATIO = FPS_INDICATIVE/(float)FPS_REAL;
	
	private static final int FRAME_DURATION = 1000/FPS_REAL;
	
	/** Countdown in ms for restoring the game */
	private static final long COUNTDOWN_RESTORING = 2000;
	
	// flag to hold game state
	private boolean running;
	private boolean pause = false;
	private boolean endGame = false;
	private boolean restoring = false;
	private SurfaceHolder surfaceHolder;
	private GameManager gameManager;
	private Canvas canvas;
	private ChronoDisplayer chronoRestore;

	public MainThread(SurfaceHolder surfaceHolder, GameManager gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gameManager = gamePanel;
		this.canvas = null;
		this.chronoRestore = new ChronoDisplayer(MoveUtil.BACKGROUND_WIDTH*1/3, MoveUtil.BACKGROUND_HEIGHT/2);
		chronoRestore.setSize(60f);
	}


	public void setRunning(boolean running) {
		this.running = running;
		synchronized (this) {
			notify();
		}
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

	/**
	 * Call when the application is put in background to prepare the restoration
	 */
	public void setRestoring() {
		setPause(true);
		if (!endGame) {
			restoring = true;
		}
	}


	@Override
	public void run() {
		Log.d(TAG, "Starting game loop");
		// wait for surface to be created before starting the game
		if (waitAndTestForSurfaceActive()) {
			while (running) {
				
				// try to progress in the game.
				if (!endGame) {
					while(!pause && running) {
						doOneFrame();
					}
				}
				
				// the game is end or the pause is enable
				if (running) {
					Log.d(TAG, "Wait for notify");
					waitSecure();
				}
				
				// after the pause is disabled, if the game just pop up (restoration), display a timer before starting
				if (restoring) {
					Log.d(TAG, "Restoring screen");
					restoring = false;
					// wait for surface to be created before restarting the game
					if (waitAndTestForSurfaceActive()) {
						Log.d(TAG, "Start countdown");
						long endTime = System.currentTimeMillis() + COUNTDOWN_RESTORING;
						while(running) {
							long timeRemain = endTime - System.currentTimeMillis();
							// if the chrono is end or the pause is set again, end the chrono
							if (timeRemain < 0 || pause) {
								drawScreen();
								break;
							}
							chronoRestore.setTime(timeRemain);
							drawRestore();
						}
					}
				} // end restoring
			}
		}
	}

	/**
	 * Execute one frame of the game
	 */
	private void doOneFrame() {
		try {
			// try locking the canvas for exclusive pixel editing on the surface
			long time = System.currentTimeMillis();
			launchUpdateAndDrawTimed();
			
			// normalization of the duration of a frame
			long timePassed = System.currentTimeMillis() - time;
			if (timePassed < FRAME_DURATION) {
				synchronized (this) {
					wait(FRAME_DURATION-timePassed);
				}
			}
			//Log.d("DURATIONFRAME", ""+timePassed);
			if(!endGame) {
				GameContext.getSingleton().gameDuration += FRAME_DURATION;
			}
		}
		catch (IllegalMonitorStateException e) {
			Log.d(TAG, e.getMessage());
			throw e;
		} catch (InterruptedException e) {
			// for the wait
			Log.d(TAG, e.getMessage());
		}
	}

	/**
	 * Make an update and a draw
	 */
	private void launchUpdateAndDraw() {
		// update game state 
		this.gameManager.update();
		// draws the canvas on the panel
		drawScreen();
	}
	
	/**
	 * Make an update and a draw with timer if it is enabled
	 */
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
		drawScreen();
		TimerUtil.end("_globaldraw");
	}
	
	/**
	 * Do a draw of the game
	 */
	private void drawScreen() {
		doDraw(false);
	}
	
	/**
	 * Do a draw of the game with a count down in foreground.
	 */	
	private void drawRestore() {
		doDraw(true);
	}
	
	/**
	 * Do a draw of the game. If withChrono is true, display a count down in foreground.
	 */
	private void doDraw(boolean withChrono) {
		try {
			canvas = this.surfaceHolder.lockCanvas();
			if (canvas != null) {
				synchronized (surfaceHolder) {
					this.gameManager.onDraw(canvas);
					if (withChrono) {
						chronoRestore.draw(canvas);
					}
				}
			}
		}
		finally {
			// in case of an exception the surface is not left in
			// an inconsistent state
			if (canvas != null) {
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}	// end finally		
	}
	

	/**
	 * Wait for the surface to be created every 200ms. Return true when this is the case. 
	 * Or false if the demand is aborted (like the game is put in back again, or the game end)
	 * 
	 * @return if the surface is activated
	 */
	private boolean waitAndTestForSurfaceActive() {
		while (!gameManager.isSurfaceActive() && running) {
			Log.d(TAG, "Waiting for surface to be created");
			waitSecure(200);
			// stop the waiting if the restoring is set again of the thread is stopping
			if (!running || restoring) {
				return false;
			}
		}
		return running;
	}
	
	/**
	 * Wait with catch to not throw exception
	 */
	private void waitSecure() { 
		waitSecure(null); 
	}
	
	/**
	 * Wait with catch to not throw exception
	 * 
	 * @param timeMs waiting time in ms
	 */
	private void waitSecure(Integer timeMs) {
		synchronized (this) {
			try {
				if (timeMs == null) {
					wait();
				}
				else {
					wait(timeMs);
				}
			} catch (InterruptedException e) {
				Log.e(TAG, "Error in waiting, timeMs="+timeMs, e);
			}
		}
	}

}
