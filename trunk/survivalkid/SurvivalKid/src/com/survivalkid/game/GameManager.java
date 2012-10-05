package com.survivalkid.game;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.survivalkid.R;
import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.manager.CharacterManager;
import com.survivalkid.game.manager.EnemyManager;
import com.survivalkid.game.manager.ItemManager;
import com.survivalkid.game.manager.ObjectManager;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.thread.MainThread;
import com.survivalkid.game.util.MoveUtil;

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
		
		// initialize of the context singleton
		GameContext.getSingleton().setContext(context);
		
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// create the game loop thread
		thread = new MainThread(getHolder(), this);
		
		characterManager = new CharacterManager();
		enemyManager = new EnemyManager();
		itemManager = new ItemManager();

		// make the GamePanel focusable so it can handle events
		setFocusable(true);
		
		// create Elaine and load bitmap
		AnimatedSprite persoSprite = new AnimatedSprite(BitmapFactory.decodeResource(getResources(), R.drawable.yugo),150,150,
				6,12, 15);
		
		persoSprite.addAnimation("run", new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15}, 15);
		Personage perso = new Personage(persoSprite);
		characterManager.addCharacter(perso);
		
		
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

			enemyManager.draw(canvas);
			itemManager.draw(canvas);
			characterManager.draw(canvas);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		
		Personage currentChar = characterManager.getCharacterList(0);
		MoveUtil.calculMove(x, y, event.getAction(), currentChar);

		return true;
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