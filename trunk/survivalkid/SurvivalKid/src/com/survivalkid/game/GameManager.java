package com.survivalkid.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.survivalkid.game.manager.CharacterManager;
import com.survivalkid.game.manager.ItemManager;
import com.survivalkid.game.manager.ObjectManager;
import com.survivalkid.game.thread.MainThread;

public class GameManager extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = GameManager.class.getSimpleName();

	/** The thread corresponding to the game loop. */
	private MainThread thread;
	
	private CharacterManager characterManager;
	private ObjectManager enemyManager;
	private ObjectManager itemManager;


	public GameManager(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// create the game loop thread
		thread = new MainThread(getHolder(), this);
		
		characterManager = new CharacterManager();
		enemyManager = new EnemyManager();
		itemManager = new ItemManager();

		// make the GamePanel focusable so it can handle events
		setFocusable(true);
	}
	
	/**
	 * This is the game update method. It iterates through all the objects
	 * and calls their update method if they have one or calls specific
	 * engine's update method.
	 */
	public void update() {
		enemyManager.update();
		itemManager.update();
		characterManager.update();
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (canvas != null) {
			// fills the canvas with black
			canvas.drawColor(Color.BLACK);

			enemyManager.draw();
			itemManager.draw();
			characterManager.draw();
		}
	}

	
	//------------------------------------------------------------------------
	// Surface managing
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and
		// we can safely start the game loop
		thread.setRunning(true);
		thread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}
	
	/** Action when clicking on the BACK button. */
	public void stop() {
		thread.setRunning(false);
		((Activity) getContext()).finish();
	}
}
