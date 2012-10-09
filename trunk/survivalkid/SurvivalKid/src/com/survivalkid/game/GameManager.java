package com.survivalkid.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.survivalkid.R;
import com.survivalkid.game.core.Constants.PersonageConstants;
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
	
	public static int OWN_PERSO = 0;

	/** The thread corresponding to the game loop. */
	private MainThread thread;
	
	private CharacterManager characterManager;
	private ObjectManager enemyManager;
	private ObjectManager itemManager;
	
	private Bitmap ground;
	
	//DEBUG - TEST
	private boolean displayHitBoxes = false;


	@SuppressLint("NewApi")
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
		
		ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
		MoveUtil.initializeButton(getResources());

		// TEST ------------------
		Personage yugo = new Personage(PersonageConstants.PERSO_YUGO,BitmapFactory.decodeResource(getResources(), R.drawable.yugo2),150,150,
				6,12);
		
		Personage yuna = new Personage(PersonageConstants.PERSO_YUNA, BitmapFactory.decodeResource(getResources(), R.drawable.yuna),250,150,
				6,12);
		
		characterManager.addCharacter(yugo);
		characterManager.addCharacter(yuna);
		// END TESTS --------------

		
		
	}
	
	/**
	 * This is the game update method. It iterates through all the objects
	 * and calls their update method if they have one or calls specific
	 * engine's update method.
	 */
	public void update() {
		long gameTime = System.currentTimeMillis();
		enemyManager.update(gameTime);
		itemManager.update(gameTime);
		characterManager.update(gameTime);
		
		MoveUtil.btn_left.setPressed(characterManager.getCharacterList(OWN_PERSO).getMoveManager().isLeftEnabled);
		MoveUtil.btn_right.setPressed(characterManager.getCharacterList(OWN_PERSO).getMoveManager().isRightEnabled);
		MoveUtil.btn_up.setPressed(characterManager.getCharacterList(OWN_PERSO).getMoveManager().isTopEnabled);
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (canvas != null) {
			// fills the canvas with black
			canvas.drawColor(Color.BLUE);
			canvas.drawBitmap(ground, 0, 0, null);

			enemyManager.draw(canvas, displayHitBoxes);
			itemManager.draw(canvas, displayHitBoxes);
			characterManager.draw(canvas, displayHitBoxes);
			
			MoveUtil.btn_left.draw(canvas);
			MoveUtil.btn_right.draw(canvas);
			MoveUtil.btn_up.draw(canvas);
		}
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Personage currentChar = characterManager.getCharacterList(0);
		characterManager.getCharacterList(OWN_PERSO).getMoveManager().calculMove(event);
		
		//if (event.getAction() != MotionEvent.ACTION_MOVE)	dumpEvent(event);
		return true;
	}
	
	/** Show an event in the LogCat view, for debugging */
	private void dumpEvent(MotionEvent event) {
		String names[] = { "DOWN" , "UP" , "MOVE" , "CANCEL" , "OUTSIDE" ,
		"POINTER_DOWN" , "POINTER_UP" , "7?" , "8?" , "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_" ).append(names[actionCode]);
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN
		|| actionCode == MotionEvent.ACTION_POINTER_UP) {
			sb.append("(pid " ).append(
			action >> MotionEvent.ACTION_POINTER_INDEX_SHIFT);
			sb.append(")" );
		}
		sb.append("[" );
		for (int i = 0; i < event.getPointerCount(); i++) {
			sb.append("#" ).append(i);
			sb.append("(pid " ).append(event.getPointerId(i));
			sb.append(")=" ).append((int) event.getX(i));
			sb.append("," ).append((int) event.getY(i));
			if (i + 1 < event.getPointerCount())
				sb.append(";" );
		}
		sb.append("]" ).append(characterManager.getCharacterList(0).getMoveManager().lastEnabledLeft);
		Log.d(TAG, sb.toString());
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

	/** Hide or show the hit boxes */
	public boolean hideShowDisplayHitBoxes() {
		return (displayHitBoxes = !displayHitBoxes);
	}
}
