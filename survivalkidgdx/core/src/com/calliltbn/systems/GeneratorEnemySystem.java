package com.calliltbn.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.calliltbn.factory.EntityFactory;
import com.calliltbn.factory.TypeEntity;
import com.calliltbn.param.Difficulty;

import java.util.List;
import java.util.Map;

public class GeneratorEnemySystem extends EntitySystem {

    private EntityFactory entityFactory;
    private int currentDifficulty;
    private int maxDifficulty;
    private float generationDelay;
    private Difficulty difficulty;
    private Map<Integer, List<TypeEntity>> enemyDifficultyMap;

    private float lastGenerationTime = 0;
    private float lastIncreaseDifficulty = 0;

    public GeneratorEnemySystem(EntityFactory entityFactory, Difficulty difficulty) {
        this.entityFactory = entityFactory;
        this.currentDifficulty = 1;
        this.difficulty = difficulty;
        this.generationDelay = difficulty.getStartedGenDelay();
        this.enemyDifficultyMap = TypeEntity.getEnemyDifficultyMap();
        this.maxDifficulty = enemyDifficultyMap.size();
    }

    @Override
    public void update(float deltaTime) {

        // Do we have to generate?
        lastGenerationTime += deltaTime;
        if (lastGenerationTime >= generationDelay) {
            // create an enemy
            lastGenerationTime = 0;

            // generate the difficulty of the enemy to generate
            int difficulty = getRandomComplexity(currentDifficulty, maxDifficulty);
            // get a random enemy with this difficulty
            List<TypeEntity> enemies = enemyDifficultyMap.get(Integer.valueOf(difficulty));
            TypeEntity enemyToGenerate = enemies.get((int) (Math.random() * (enemies.size())));
            // create the entity associated to the enemy
            entityFactory.createEnemy(enemyToGenerate);
        }

        increaseEnemyGenerationRate(deltaTime);
    }

    /**
     * Increase the rate of enemy generation (decrease generation delay)
     *
     * @param deltaTime time spend since last update
     */
    private void increaseEnemyGenerationRate(float deltaTime) {
        if (generationDelay == difficulty.getMinGenDelay()) {
            // minimum
            return;
        }
        lastIncreaseDifficulty += deltaTime;
        if(lastIncreaseDifficulty >= difficulty.getPeriodIncreasingDelay()) {
            lastIncreaseDifficulty = 0;
            generationDelay = Math.max(generationDelay - difficulty.getDecreaseStepPeriodDelay(),
                    difficulty.getMinGenDelay());
        }
    }

    /**
     * Generate an enemy difficulty with the current difficulty
     *
     * @param difficulty the current difficulty
     * @param maxDifficulty max difficulty
     * @return the difficulty used to get a random enemy
     */
    private int getRandomComplexity(int difficulty, int maxDifficulty) {
        // Create list of probabilities
        int[] probas = new int[maxDifficulty];
        probas[0] = (int) (1.0 / (Math.pow(2, difficulty)) * 100);
        for (int i = 1; i < maxDifficulty; i++) {
            probas[i] = (int) (1.0 / (Math.pow(2, Math.abs(i - difficulty))) * 100)
                    + probas[i - 1];
        }

        // Now generate a number between 0 and probas[max].
        int gen = (int) (Math.random() * probas[maxDifficulty - 1]);
        // return the category
        for (int i = 0; i < maxDifficulty; i++) {
            if (gen < probas[i]) {
                return i;
            }
        }
        // should not happen
        return -1;

    }
}
