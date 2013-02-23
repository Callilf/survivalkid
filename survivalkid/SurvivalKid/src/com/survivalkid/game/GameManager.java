package com.survivalkid.game;

import static com.survivalkid.game.manager.CharacterManager.OWN_PERSO;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.survivalkid.R;
import com.survivalkid.game.core.ChronoDisplayer;
import com.survivalkid.game.core.Constants.PersonageConstants;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.item.ItemEntity;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.manager.CharacterManager;
import com.survivalkid.game.manager.EnemyManager;
import com.survivalkid.game.manager.ItemManager;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.thread.MainThread;
import com.survivalkid.game.util.BitmapUtil;
import com.survivalkid.game.util.CollisionUtil;
import com.survivalkid.game.util.HandlerUtil;
import com.survivalkid.game.util.HandlerUtil.HandlerEnum;
import com.survivalkid.game.util.MoveUtil;

@SuppressLint("HandlerLeak") // TODO DELETE WHEN THE HANDLER WOULDN'T BE USE ANYMORE
public class GameManager extends SurfaceView implements SurfaceHolder.Callback {
	
	private static final String TAG = GameManager.class.getSimpleName();

	/** The thread corresponding to the game loop. */
	private MainThread thread;

	private CharacterManager characterManager;
	private EnemyManager enemyManager;
	private ItemManager itemManager;
	private ChronoDisplayer chrono;

	private Bitmap ground;

	public GameManager(Context context) {
		super(context);
		//Initialize the bitmapUtil
		BitmapUtil.initialize(getResources());
		
		// initialize of the context singleton
		GameContext.getSingleton().setContext(context);
		GameContext.getSingleton().initSingleton();

		// initialize multitouch
		MoveUtil.HAS_MULTITOUCH = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH);
		
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// create the game loop thread
		thread = new MainThread(getHolder(), this);
		
		// make the GamePanel focusable so it can handle events
		setFocusable(true);

		ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
		MoveUtil.initializeButton(getResources());

		Log.d(TAG, "Start the game !");
		create();
		
		HandlerUtil.handlerFin = new Handler() {
				@Override
				public void handleMessage(Message msg) {
				     Toast.makeText(getContext(), "Survival time : " + msg.arg1/1000f + " seconds", Toast.LENGTH_LONG).show();
				}
			};
	}

	/**
	 * Start the game.
	 * 
	 * @param context
	 */
	public void create() {
		
		characterManager = new CharacterManager();
		enemyManager = new EnemyManager();
		itemManager = new ItemManager();
		chrono = new ChronoDisplayer(10, 20);
		
		
		// TEST ------------------
//		Personage character = new Personage(PersonageConstants.PERSO_YUGO, SpriteEnum.YUGO, 300, 250);
		Personage character = new Personage(PersonageConstants.PERSO_YUNA, SpriteEnum.YUNA, 250, 250);

		characterManager.addCharacter(character);
		// END TESTS --------------

		
	}
	
	/**
	 * Restart the game
	 * 
	 * @return true if the game restart
	 */
	public void restart() {
		thread.setPause(true);

		Log.d(TAG, "ReStart the game !");
				
		GameContext.getSingleton().initSingleton();
		create();
		
		// Explicit call of the garbage collector before restarting the game
		System.gc();
		
		thread.setEndGame(false);
	}

	/**
	 * This is the game update method. It iterates through all the objects and
	 * calls their update method if they have one or calls specific engine's
	 * update method.
	 */
	public void update() {
		long gameTime = System.currentTimeMillis();
		long gameDuration = GameContext.getSingleton().gameDuration;
		chrono.setTime(gameDuration);
		GameContext.getSingleton().setCurrentTimeMillis(gameTime);

		// Update the gameEntities
		enemyManager.update(gameDuration);
		itemManager.update(gameDuration);
		characterManager.update(gameDuration);

		// Check the collisions
		for (Personage perso : characterManager.getCharacterList()) {
			perso.setOverlaping(false);
		}
		for (Personage perso : characterManager.getCharacterList()) {
			for (ItemEntity item : itemManager.getItemList()) {
				if (CollisionUtil.Overlaps(perso, item)) {
					perso.setOverlaping(true);
					item.collide(perso);
				}
			}
			for (EnemyEntity enemy : enemyManager.getEnemyList()) {
				if (CollisionUtil.Overlaps(perso, enemy)) {
					perso.setOverlaping(true);
					enemy.collide(perso);
				} else {
					enemy.setCollidingCharacter(-1);
				}
			}
		}

		// Change the buttons sprites when they are pressed.
		if (characterManager.getCharacterList().size() > OWN_PERSO  && characterManager.getCharacterList(OWN_PERSO) != null) {
			MoveUtil.btn_left.setPressed(characterManager.getCharacterList(OWN_PERSO).getMoveManager().isLeftEnabled);
			MoveUtil.btn_right.setPressed(characterManager.getCharacterList(OWN_PERSO).getMoveManager().isRightEnabled);
			MoveUtil.btn_up.setPressed(characterManager.getCharacterList(OWN_PERSO).getMoveManager().isTopEnabled);
		}

		// Restart the game if all players are dead
		if (characterManager.getCharacterList().isEmpty()) {
			endGame();
		}
	}

	private void endGame() {
		GameContext s = GameContext.getSingleton();
		long timePassed = s.gameDuration;
		thread.setEndGame(true);
		HandlerUtil.sendMessage((int)timePassed, HandlerEnum.HANDLER_FIN);
		Log.i(TAG, "Time passed : " + timePassed/1000f + ", Score : " + s.score+", end difficulty : " + s.currentDifficulty);
	}
	
	
	@Override
	public void onDraw(Canvas canvas) {
		if (canvas != null) {
			// fills the canvas with black
			canvas.drawColor(Color.BLUE);
			canvas.drawBitmap(ground, 0, 0, null);

			chrono.draw(canvas);
			enemyManager.draw(canvas);
			itemManager.draw(canvas);
			characterManager.draw(canvas);

			MoveUtil.btn_left.draw(canvas);
			MoveUtil.btn_right.draw(canvas);
			MoveUtil.btn_up.draw(canvas);
		}
	}

	
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (characterManager.getCharacterList().size() > OWN_PERSO && characterManager.getCharacterList(OWN_PERSO) != null) {
			characterManager.getCharacterList(OWN_PERSO).getMoveManager().calculMove(event);
		}
		
		//Check if a balloon has been touched
		if (itemManager.getItemList().size() > 0) {
			itemManager.checkBalloonTouchBox(event);
		}

		// if (event.getAction() != MotionEvent.ACTION_MOVE) dumpEvent(event);
		return true;
	}

	/** Show an event in the LogCat view, for debugging
	private void dumpEvent(MotionEvent event) {
		String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_").append(names[actionCode]);
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
			sb.append("(pid ").append(action >> MoveUtil.ACTION_POINTER_INDEX_SHIFT);
			sb.append(")");
		}
		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++) {
			sb.append("#").append(i);
			sb.append("(pid ").append(event.getPointerId(i));
			sb.append(")=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if (i + 1 < event.getPointerCount())
				sb.append(";");
		}
		sb.append("]").append(characterManager.getCharacterList(0).getMoveManager().lastEnabledLeft);
		Log.d(TAG, sb.toString());
	} */

	
	
	
	// ------------------------------------------------------------------------
	// Surface managing
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
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
		thread.setPause(false);
		((Activity) getContext()).finish();
	}

	/// getter & setter
	public MainThread getThread() {
		return thread;
	}
	
	public int getNbPlayer() {
		return characterManager.getCharacterList().size();
	}
}
