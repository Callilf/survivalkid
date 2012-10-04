package com.survivalkid.game.singleton;

public class GameContext {
	/** The instance. */
	private static GameContext instance;
	
	/** The time when the game started. */
	private int initialTime;
	/** The current on-screen difficulty. */
	private int currentDifficulty;
	/** The current score. */
	private int score;
	
	/** Get the singleton. */
	static GameContext getSingleton() {
		if (instance == null) {
			return new GameContext();
		} else {
			return instance;
		}
	}
	
}
