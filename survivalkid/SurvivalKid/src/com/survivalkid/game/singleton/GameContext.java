package com.survivalkid.game.singleton;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.survivalkid.DataSave;

public class GameContext {

	/** The instance. */
	private static GameContext instance;
	
	/** the context */
	private Context context;	

	/** save date */
	private DataSave dataSave = null;
	
	/** duration of the game */
	public long gameDuration;
	
	/** The time when the game started. */ // use gameDuration instead of initialTime for all handling
	public long initialTime;
	/** The current on-screen difficulty. */
	public int currentDifficulty;
	/** The current score. */
	public int score;
	
	/** The current time in milliseconds - do not be use anymore or for statistic only */
	private long currentTimeMillis;
	
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
		score = 0;
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
	
}
