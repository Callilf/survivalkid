package com.survivalkid.game.singleton;

public class GameContext {
	/** The instance. */
	private static GameContext instance;
	
	/** The time when the game started. */
	public long initialTime;
	/** The current on-screen difficulty. */
	public int currentDifficulty;
	/** The current score. */
	public int score;
	
	/** Get the singleton. */
	public static GameContext getSingleton() {
		if (instance == null) {
			instance = new GameContext();
			instance.initSingleton();
			
		}
		return instance;
	}
	
	/**
	 * Initialize the singleton
	 */
	public void initSingleton() {
		instance.initialTime=System.currentTimeMillis();
		currentDifficulty = 0;
		score = 0;
	}
	
}
