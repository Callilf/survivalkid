package com.survivalkid.game.singleton;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.survivalkid.game.core.Constants.PreferencesConstants;
import com.survivalkid.game.core.Difficulty.DifficultyEnum;
import com.survivalkid.game.data.DataSave;
import com.survivalkid.game.util.PrefsUtil;
import com.survivalkid.menu.MainMenuActivity;

public class GameContext {

	/** The instance. */
	private static GameContext instance;
	
	private MainMenuActivity mainMenuActivity;
	
	/** the context */
	private Context context;	
	
	/** save date */
	private DataSave dataSave = null;
	
	/** duration of the game */
	public long gameDuration;
	
	/** difficulty of the game */
	public DifficultyEnum gameDifficulty;
	
	/** Movement mode */
	public boolean slideMoveEnabled;
	
	/** The time when the game started. */ // use gameDuration instead of initialTime for all handling
	public long initialTime;
	/** The current on-screen difficulty. */
	public int currentDifficulty;
	/** The current score. */
	public int score;
	
	/** The current time in milliseconds - do not be use anymore or for statistic only */
	private long currentTimeMillis;
	
	// alteration of the game
	private float alterationSpeedEnemy;
	
	/** Get the singleton. */
	public static GameContext getSingleton() {
		if (instance == null) {
			instance = new GameContext();
		}
		return instance;
	}
	
	/**
	 * Initialize the singleton
	 */
	public void initSingleton() {
		instance.initialTime=System.currentTimeMillis();
		currentDifficulty = 0;
		gameDuration = 0;
		gameDifficulty = DifficultyEnum.valueOf(PrefsUtil.getPrefs().getInt(PreferencesConstants.DIFFICULTY, 1));
		slideMoveEnabled = PrefsUtil.getPrefs().getBoolean(PreferencesConstants.SLIDE_MOVE_ENABLED, false);
		score = 0;
		alterationSpeedEnemy = 1f;
		if (dataSave == null) {
			dataSave = DataSave.getSaveData(context);
		}
	}

	/**
	 * Call at the beginning to initialize the context
	 * @param _context the game context
	 */
	public void setContext(Context _context) {
		context = _context;
	}
	public Context getContext() {
		return context;
	}
	
	public DataSave getDataSave() {
		return dataSave;
	}

	/**
	 * @return the display
	 */
	public Display getDisplay() {
		return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	}

	/**
	 * This function should be use only for statistics, and not for the game (gameDuration should be use)
	 * 
	 * @return the currentTimeMillis
	 */
	public long getCurrentTimeMillis() {
		return currentTimeMillis;
	}

	/**
	 * @param currentTimeMillis the currentTimeMillis to set
	 */
	public void setCurrentTimeMillis(long currentTimeMillis) {
		this.currentTimeMillis = currentTimeMillis;
	}


	public float getAlterationSpeedEnemy() {
		return alterationSpeedEnemy;
	}

	public void setAlterationSpeedEnemy(float alterationSpeedEnemy) {
		this.alterationSpeedEnemy = alterationSpeedEnemy;
	}

	/**
	 * @return the mainMenuActivity
	 */
	public MainMenuActivity getMainMenuActivity() {
		return mainMenuActivity;
	}

	/**
	 * @param mainMenuActivity the mainMenuActivity to set
	 */
	public void setMainMenuActivity(MainMenuActivity mainMenuActivity) {
		this.mainMenuActivity = mainMenuActivity;
	}
	
}
