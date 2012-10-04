package com.survivalkid.game;

import com.survivalkid.R;
import com.survivalkid.game.thread.MainThread;
import com.survivalkid.test.Character;
import com.survivalkid.test.Yugo;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameManager extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = GameManager.class.getSimpleName();

	private MainThread thread;
	private Character character;
	private Yugo yugo;

	public GameManager(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// create droid and load bitmap
		character = new Character(BitmapFactory.decodeResource(getResources(),
				R.drawable.character1), 550, 250);
		
		// create Elaine and load bitmap
		yugo = new Yugo(
				BitmapFactory.decodeResource(getResources(), R.drawable.yugo)
				, 150, 150	// initial position
				, 6, 12	// width and height of sprite
				, 20, 45);	// FPS and number of frames in the animation


		// create the game loop thread
		thread = new MainThread(getHolder(), this);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);
	}
	
	/**
	 * This is the game update method. It iterates through all the objects
	 * and calls their update method if they have one or calls specific
	 * engine's update method.
	 */
	public void update() {
		yugo.update(System.currentTimeMillis());
	}


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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// delegating event handling to the droid
			character.handleActionDown((int) event.getX(), (int) event.getY());

			Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// the gestures
			if (character.isTouched()) {
				// the droid was picked up and is being dragged
				character.setX((int) event.getX());
				character.setY((int) event.getY());
			}
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			// touch was released
			if (character.isTouched()) {
				character.setTouched(false);
			}
		}
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (canvas != null) {
			// fills the canvas with black
			canvas.drawColor(Color.BLACK);
			character.draw(canvas);
			yugo.draw(canvas);
		}
	}
}
