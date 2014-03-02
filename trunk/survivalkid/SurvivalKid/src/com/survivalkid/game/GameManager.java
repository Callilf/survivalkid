package com.survivalkid.game;

import static com.survivalkid.game.manager.CharacterManager.OWN_PERSO;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.survivalkid.AbstractSurfaceView;
import com.survivalkid.GameActivity;
import com.survivalkid.game.core.ChronoDisplayer;
import com.survivalkid.game.core.Constants;
import com.survivalkid.game.core.Constants.PersonageConstants;
import com.survivalkid.game.core.Constants.PreferencesConstants;
import com.survivalkid.game.core.Difficulty;
import com.survivalkid.game.core.Difficulty.DifficultyEnum;
import com.survivalkid.game.core.SurfaceHandler;
import com.survivalkid.game.core.TouchHandler;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.data.DataSave;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.item.ItemEntity;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.manager.CharacterManager;
import com.survivalkid.game.manager.DecorManager;
import com.survivalkid.game.manager.EnemyManager;
import com.survivalkid.game.manager.ItemManager;
import com.survivalkid.game.manager.ParticleManager;
import com.survivalkid.game.particle.ParticleEmitter;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.singleton.SharedVars;
import com.survivalkid.game.thread.MainThread;
import com.survivalkid.game.util.BitmapUtil;
import com.survivalkid.game.util.CollisionUtil;
import com.survivalkid.game.util.DesignUtil;
import com.survivalkid.game.util.MoveUtil;
import com.survivalkid.game.util.PrefsUtil;

public class GameManager extends AbstractSurfaceView {

	private static final String TAG = GameManager.class.getSimpleName();
	
	/** The thread corresponding to the game loop. */
	private MainThread thread;

	private CharacterManager characterManager;
	private EnemyManager enemyManager;
	private ItemManager itemManager;
	private ParticleManager particleManager;
	private DecorManager decorManager;
	private ChronoDisplayer chrono;
	private Difficulty difficulty;
	
	private boolean surfaceActive;

	private int persoSelected;
	private boolean modeEditLocationButton;

	private SurfaceHandler surfaceHandler;
	
	private GameActivity activity;

	public GameManager(Context context) {
		super(context);
		setTagParent(TAG);
		
		modeEditLocationButton = false;
		surfaceActive = false;
		if (Constants.DEBUG) {
			Log.d(TAG, "Create the GameManager!");
		}

		activity = (GameActivity) context;
		
		// Initialize the bitmapUtil
		BitmapUtil.initialize(getResources());

		// initialize of the context singleton
		GameContext.getSingleton().setContext(context);
		GameContext.getSingleton().initSingleton();
		
		SharedVars.getSingleton().initSingleton();
		
		surfaceHandler = new SurfaceHandler(this);
//		SharedPreferences prefs = getContext().getSharedPreferences("SURVIVAL-KID-PREF", Context.MODE_PRIVATE);
//		if (prefs == null) {
//			// todo
//		}

		// initialize multitouch
		MoveUtil.HAS_MULTITOUCH = context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH);

		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// create the game loop thread
		thread = new MainThread(this);
		
		// make the GamePanel focusable so it can handle events
		setFocusable(true);

		MoveUtil.initializeButton();
	}

	/**
	 * Start the game.
	 * 
	 * @param personage
	 *            PersonageConstants
	 */
	public void create() {
		if (Constants.DEBUG) {
			Log.d(TAG, "Start the game !");
		}
		
		SharedVars sharedVars = SharedVars.getSingleton();
		DifficultyEnum diff = DifficultyEnum.valueOf(PrefsUtil.getPrefs().getInt(PreferencesConstants.DIFFICULTY, 1));
		difficulty = new Difficulty(diff);
		sharedVars.setDifficulty(difficulty);
		
		surfaceHandler.setBackground(difficulty);
		characterManager = new CharacterManager();
		enemyManager = new EnemyManager();
		itemManager = new ItemManager();
		particleManager = new ParticleManager();
		decorManager = new DecorManager();
		
		sharedVars.setEnemyManager(enemyManager);
		sharedVars.setParticleManager(particleManager);
		sharedVars.setDecorManager(decorManager);
		
		chrono = new ChronoDisplayer(10, 20);

		Personage character = null;
		switch (persoSelected) {
		case PersonageConstants.PERSO_YUGO:
			character = new Personage(PersonageConstants.PERSO_YUGO, SpriteEnum.YUGO, 250, 250);
			break;
		case PersonageConstants.PERSO_YUNA:
			character = new Personage(PersonageConstants.PERSO_YUNA, SpriteEnum.YUNA, 250, 250);
			break;
		default:
			break;
		}

		characterManager.addCharacter(character);
		
		
		ParticleEmitter emitter = new ParticleEmitter(SpriteEnum.PARTICLE_SMOKE_WHITE_ROUND, 200);
		emitter.setX(MoveUtil.BACKGROUND_LEFT + MoveUtil.BACKGROUND_WIDTH/2);
		emitter.setY(MoveUtil.GROUND);
		emitter.setParticleSpeedXMin(0);
		emitter.setParticleSpeedXMax(1);
		emitter.setParticleSpeedYMin(-1);
		emitter.setParticleSpeedYMax(-1);
		emitter.setSpeedChange(0, 75, 0, 10);
		emitter.setParticleFadeOut(true);
		emitter.setParticleTimeOut(750);
		emitter.setGenerate(true);
		emitter.setNumberOfParticlePerGeneration(1);
		emitter.setDeleteParticleWhenAnimFinished(false);
		//SharedVars.getSingleton().getParticleManager().addEmitter(emitter);
	}

	/**
	 * Restart the game
	 * 
	 * @return true if the game restart
	 */
	public void restart() {
		thread.setPause(true);

		if (Constants.DEBUG) {
			Log.d(TAG, "ReStart the game !");
		}

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
		decorManager.update(gameDuration);
		if(!SharedVars.getSingleton().isFrozen()) {
			enemyManager.update(gameDuration);
			itemManager.update(gameDuration);
			characterManager.update(gameDuration);
			particleManager.update(gameDuration);
		

			// Check the collisions
			for (Personage perso : characterManager.getCharacterList()) {
				perso.setOverlaping(false);
			}
			for (Personage perso : characterManager.getCharacterList()) {
				for (ItemEntity item : itemManager.getItemList()) {
					if (!item.isInBalloon()) {
						if (CollisionUtil.Overlaps(perso, item)) {
							perso.setOverlaping(true);
							item.collide(perso);
						}
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
	
			for (EnemyEntity enemyKiller : enemyManager.getEnemyKillerList()) {
				for (EnemyEntity enemy : enemyManager.getEnemyList()) {
					// also compare the pointer to avoid the enemy to kill itself
					if (enemy != enemyKiller && CollisionUtil.Overlaps(enemyKiller, enemy)) {
						enemy.collide(enemyKiller);
					}
				}
			}
	
			// Change the buttons sprites when they are pressed.
			if (characterManager.getCharacterList().size() > OWN_PERSO
					&& characterManager.getCharacterList(OWN_PERSO) != null) {
				MoveUtil.btn_left.setPressed(characterManager.getCharacterList(OWN_PERSO).getMoveManager().isLeftEnabled);
				MoveUtil.btn_right.setPressed(characterManager.getCharacterList(OWN_PERSO).getMoveManager().isRightEnabled);
				MoveUtil.btn_up.setPressed(characterManager.getCharacterList(OWN_PERSO).getMoveManager().isTopEnabled);
			}
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
//		HandlerUtil.sendMessage((int) timePassed, HandlerEnum.HANDLER_FIN);
//		Log.i(TAG, "Time passed : " + timePassed / 1000f + ", Score : " + s.score + ", end difficulty : "
//				+ s.currentDifficulty);
		
		// save the score
		DataSave data = s.getDataSave();
		if (data != null) {
			// if it's a new highscore, store the file
			if (data.addScore(timePassed)) {
				data.saveData(getContext());
				// TODO tell the people it's a new highscore
			}
		}
		
		if (Constants.DEBUG) {
			Log.i(TAG, "DISPLAY END MENU");
		}
		activity.displayEndMenu();
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (canvas != null) {
			// fills the canvas with black
			surfaceHandler.drawBackgroundGame(canvas);
			decorManager.draw(canvas, false);
			
			if(SharedVars.getSingleton().isPersoDrawnInBackground()) {
				characterManager.draw(canvas);
			}
			enemyManager.draw(canvas);
			itemManager.draw(canvas);
			if(!SharedVars.getSingleton().isPersoDrawnInBackground()) {
				characterManager.draw(canvas);
			}
			
			particleManager.draw(canvas);
			decorManager.draw(canvas, true);
			DesignUtil.drawBlackBand(canvas);
			
			chrono.draw(canvas);
			characterManager.drawSpecialOwnPerso(canvas);
			surfaceHandler.drawButton(canvas);
			
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!modeEditLocationButton) {
			TouchHandler touchHandler = new TouchHandler(event);
			if (characterManager.getCharacterList().size() > OWN_PERSO
					&& characterManager.getCharacterList(OWN_PERSO) != null) {
				characterManager.getCharacterList(OWN_PERSO).getMoveManager().calculMove(event, touchHandler);
				characterManager.getCharacterList(OWN_PERSO).getBag().checkTouched(touchHandler);
			}
	
			// Check if a balloon has been touched
			if (itemManager.getItemList().size() > 0 && 
				(touchHandler.getActionCode() == MotionEvent.ACTION_DOWN || 
				touchHandler.getActionCode() == MotionEvent.ACTION_POINTER_DOWN)) {
				itemManager.checkBalloonTouchBox(touchHandler);
			}
		}
		else {
			if (MoveUtil.buttonPosition.manageEventChangePosition(event)) {
				surfaceHandler.drawBackgroundAndButton();
			}
		}

		// if (event.getAction() != MotionEvent.ACTION_MOVE) dumpEvent(event);
		return true;
	}

	/**
	 * Show an event in the LogCat view, for debugging private void
	 * dumpEvent(MotionEvent event) { String names[] = { "DOWN", "UP", "MOVE",
	 * "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
	 * StringBuilder sb = new StringBuilder(); int action = event.getAction();
	 * int actionCode = action & MotionEvent.ACTION_MASK;
	 * sb.append("event ACTION_").append(names[actionCode]); if (actionCode ==
	 * MotionEvent.ACTION_POINTER_DOWN || actionCode ==
	 * MotionEvent.ACTION_POINTER_UP) { sb.append("(pid ").append(action >>
	 * MoveUtil.ACTION_POINTER_INDEX_SHIFT); sb.append(")"); } sb.append("[");
	 * for (int i = 0; i < event.getPointerCount(); i++) {
	 * sb.append("#").append(i);
	 * sb.append("(pid ").append(event.getPointerId(i));
	 * sb.append(")=").append((int) event.getX(i)); sb.append(",").append((int)
	 * event.getY(i)); if (i + 1 < event.getPointerCount()) sb.append(";"); }
	 * sb.
	 * append("]").append(characterManager.getCharacterList(0).getMoveManager(
	 * ).lastEnabledLeft); Log.d(TAG, sb.toString()); }
	 */

	// ------------------------------------------------------------------------
	// Surface managing

	public void surfaceCreated(SurfaceHolder holder) {
		super.surfaceCreated(holder);
		surfaceActive = true;
		// at this point the surface is created and
		// we can safely start the game loop

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		super.surfaceDestroyed(holder);
		surfaceActive = false;
	}
	
	// ------------------------------------------------------------------------
	
	public void leaveGame() {
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		thread.setRunning(false);
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		if (Constants.DEBUG) {
			Log.d(TAG, "Thread was shut down cleanly");
		}
	}

	/** Action when clicking on the BACK button. */
	public void unpause() {
		thread.setPause(false);
	}

	/** Pause the game and prepare the restoration. */
	public void stop() {
		thread.setRestoring();
	}
	
	public void changeLocationButton() {
		modeEditLocationButton = !modeEditLocationButton;
		if (modeEditLocationButton) {
			CollisionUtil.displayHitBoxes = true;
			surfaceHandler.drawBackgroundAndButton();
		}
		else {
			CollisionUtil.displayHitBoxes = false;
			afterChangeLocationButton(true);
		}
	}
	
	public void afterChangeLocationButton(boolean doSave) {
		if (characterManager.getCharacterList().size() > OWN_PERSO) {
			characterManager.getCharacterList(OWN_PERSO).getBag().initPosition();
		}
		if (modeEditLocationButton) {
			surfaceHandler.drawBackgroundAndButton();
		}
		if (doSave) {
			MoveUtil.buttonPosition.savePosition();		
		}
	}

	// / getter & setter
	public MainThread getThread() {
		return thread;
	}

	public SurfaceHandler getSurfaceHandler() {
		return surfaceHandler;
	}

	public int getNbPlayer() {
		return characterManager.getCharacterList().size();
	}

	/**
	 * @return the persoSelected
	 */
	public int getPersoSelected() {
		return persoSelected;
	}

	/**
	 * @param persoSelected
	 *            the persoSelected to set
	 */
	public void setPersoSelected(int persoSelected) {
		this.persoSelected = persoSelected;
	}

	public boolean isSurfaceActive() {
		return surfaceActive;
	}

	public boolean isModeEditLocationButton() {
		return modeEditLocationButton;
	}
	
	/**
	 * @return the characterManager
	 */
	public CharacterManager getCharacterManager() {
		return characterManager;
	}

	/**
	 * @return the enemyManager
	 */
	public EnemyManager getEnemyManager() {
		return enemyManager;
	}

	/**
	 * @return the itemManager
	 */
	public ItemManager getItemManager() {
		return itemManager;
	}

}
