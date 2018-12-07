package com.calliltbn.param;

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

    /** Started generation delay */
    private float startedGenDelay;

    /** Increase the generation delay, in milliseconds */
    private float periodIncreasingDelay;

    /** Minimum generation delay */
    private float minGenDelay;

    /** Step of decreasing the period delay */
    private float decreaseStepPeriodDelay;

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
     * Initialize the enemy generation delays variables
     *
     * @param difficulty the difficulty
     */
    private void initEnemyGenerationFrequency(DifficultyEnum difficulty) {
        switch (difficulty) {
            case NORMAL:
                startedGenDelay = 1.350f;
                periodIncreasingDelay = 2f;
                minGenDelay = 0.4f;
                decreaseStepPeriodDelay = 0.02f;
                break;
            case HARD:
            case HARDER:
            case HARDEST:
                startedGenDelay = 1f;
                periodIncreasingDelay = 1.6f;
                minGenDelay = 0.3f;
                decreaseStepPeriodDelay = 0.02f;
                break;
            case EASY:
            default:
                startedGenDelay = 1.5f;
                periodIncreasingDelay = 3f;
                minGenDelay = 0.4f;
                decreaseStepPeriodDelay = 0.02f;
                break;
        }
    }


    // GETTER AND SETTER

    public DifficultyEnum getDifficulty() {
        return difficulty;
    }

    public float getStartedGenDelay() {
        return startedGenDelay;
    }

    public void setStartedGenDelay(float startedGenDelay) {
        this.startedGenDelay = startedGenDelay;
    }

    public float getPeriodIncreasingDelay() {
        return periodIncreasingDelay;
    }

    public void setPeriodIncreasingDelay(float periodIncreasingDelay) {
        this.periodIncreasingDelay = periodIncreasingDelay;
    }

    public float getMinGenDelay() {
        return minGenDelay;
    }

    public void setMinGenDelay(float minGenDelay) {
        this.minGenDelay = minGenDelay;
    }

    public float getDecreaseStepPeriodDelay() {
        return decreaseStepPeriodDelay;
    }

    public void setDecreaseStepPeriodDelay(float decreaseStepPeriodDelay) {
        this.decreaseStepPeriodDelay = decreaseStepPeriodDelay;
    }
}
