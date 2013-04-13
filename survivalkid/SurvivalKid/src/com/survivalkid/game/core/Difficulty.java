package com.survivalkid.game.core;

/**
 * Contains differents variables dependant of the difficulty 
 * 
 * @author Thomas
 */
public class Difficulty {

	/** enum of the difficulty */
	public enum DifficultyEnum {
		EASY(1), NORMAL(2), HARD(3), HARDER(4), HARDEST(5);
		
		private int value;
		private DifficultyEnum(int value) {
			this.value = value;  
		}
		public int getValue() {
			return value;
		}
		public static DifficultyEnum valueOf(int value) {
			switch(value) {
			case 1:return EASY;
			case 2:return NORMAL;
			case 3:return HARD;
			case 4:return HARDER;
			case 5:return HARDEST;
			default:return EASY;
			}
		}
	}
	
	private DifficultyEnum difficulty;
	
	// generation enemy configuration
	
	/** Started generation frequency */
	private int startedGenFreq;
	
	/** Increase the generation frequency, in milliseconds */
	private int periodIncreasingFreq;
	
	/** Minimum generation frequency */
	private int minGenFreq;
	
	/** Step of decreasing the period frequency */
	private int decreaseStepPeriodFreq;
	
	// Initialization
	
	public Difficulty(DifficultyEnum difficulty) {
		init(difficulty);
	}

	public void init(DifficultyEnum difficulty) {
		this.difficulty = difficulty;  
		initEnemyGenerationFrequency(difficulty);
		// initItemGenerationFrequency
	}
	
	/**
	 * Initialize the enemy generation frequency variables
	 * 
	 * @param difficulty the difficulty
	 */
	private void initEnemyGenerationFrequency(DifficultyEnum difficulty) {
		switch (difficulty) {
		case NORMAL:
			startedGenFreq = 1250;
			periodIncreasingFreq = 1000;
			minGenFreq = 400;
			decreaseStepPeriodFreq = 20;
			break;	
		case HARD:
		case HARDER:
		case HARDEST:
			startedGenFreq = 1000;
			periodIncreasingFreq = 1000;
			minGenFreq = 350;
			decreaseStepPeriodFreq = 20;
			break;	
		case EASY:
		default:
			startedGenFreq = 1500;
			periodIncreasingFreq = 3000;
			minGenFreq = 500;
			decreaseStepPeriodFreq = 20;
			break;
		}		
	}

	
	// GETTER AND SETTER

	public DifficultyEnum getDifficulty() {
		return difficulty;
	}
	
	public int getStartedGenFreq() {
		return startedGenFreq;
	}

	public void setStartedGenFreq(int startedGenFreq) {
		this.startedGenFreq = startedGenFreq;
	}

	public int getPeriodIncreasingFreq() {
		return periodIncreasingFreq;
	}

	public void setPeriodIncreasingFreq(int periodIncreasingFreq) {
		this.periodIncreasingFreq = periodIncreasingFreq;
	}

	public int getMinGenFreq() {
		return minGenFreq;
	}

	public void setMinGenFreq(int minGenFreq) {
		this.minGenFreq = minGenFreq;
	}

	public int getDecreaseStepPeriodFreq() {
		return decreaseStepPeriodFreq;
	}

	public void setDecreaseStepPeriodFreq(int decreaseStepPeriodFreq) {
		this.decreaseStepPeriodFreq = decreaseStepPeriodFreq;
	}
}
