package com.survivalkid.game.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

import com.survivalkid.game.algo.enemy.ThreeStepEnemyGenerator;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.enemy.impl.Caterpillar;
import com.survivalkid.game.entity.enemy.impl.FredCircularSaw;
import com.survivalkid.game.entity.enemy.impl.Meteore;

public class EnemyManager extends ObjectManager {
	private static final String TAG = EnemyManager.class.getSimpleName();

	private List<EnemyEntity> enemyList;
	private List<EnemyEntity> deadEnemies;
	
	/* Save the last time a enemy was created */
	//private long lastEnemy; TODO to use or to deleted
	
	/**
	 * Counter used to see when to generate.
	 */
	private long generationCounter;
	private long difficultyIncreasingCounter;
	/**
	 * Generation frequency in ms.
	 */
	private long generationFrequency = 2000;
	private long difficultyIncreasingPeriod = 3000;

	private static final Map<Integer, List<Class<? extends EnemyEntity>>> enemyDifficultyMap;

	static {
		enemyDifficultyMap = new TreeMap<Integer, List<Class<? extends EnemyEntity>>>();
		ArrayList<Class<? extends EnemyEntity>> zeroDiff = new ArrayList<Class<? extends EnemyEntity>>();
		zeroDiff.add(Caterpillar.class);
		enemyDifficultyMap.put(0, zeroDiff);
		ArrayList<Class<? extends EnemyEntity>> oneDiff = new ArrayList<Class<? extends EnemyEntity>>();
		oneDiff.add(FredCircularSaw.class);
		oneDiff.add(Meteore.class);
		enemyDifficultyMap.put(1, oneDiff);
	}

	public EnemyManager() {
		enemyList = new ArrayList<EnemyEntity>();
		deadEnemies = new ArrayList<EnemyEntity>();
	}

	public void create() {
		// TODO Auto-generated method stub
		enemyList = new ArrayList<EnemyEntity>();
	}

	public void update(long gameTime) {
		
		// Do we have to generate?
		if (gameTime - generationCounter >= generationFrequency) {
			generationCounter = gameTime;
			generate();
		}
		
		if(generationFrequency != 500 && gameTime - difficultyIncreasingCounter >= difficultyIncreasingPeriod) {
			difficultyIncreasingCounter = gameTime;
			generationFrequency -= 20;
			Log.i(TAG, "Generation frequency : " + generationFrequency);
			if(generationFrequency <= 500) {
				generationFrequency = 500;
			}
		}
		
		// update the existing enemies
		for (EnemyEntity enemy : enemyList) {
			if (enemy.isDead()) {
				deadEnemies.add(enemy);
			} else {
				enemy.update(gameTime);
			}
		}

		// remove the dead enemies from the list so that they are removed from
		// the game
		if (deadEnemies.size() > 0) {
			for (EnemyEntity enemy : deadEnemies) {
				enemyList.remove(enemy);
			}
			deadEnemies.clear();
		}
	}

	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		for (EnemyEntity enemy : enemyList) {
			enemy.draw(canvas);
		}
	}

	@Override
	public void generate() {
		EnemyEntity newEnemyEntity = ThreeStepEnemyGenerator
				.generateRandomEnemy(1, 2, enemyDifficultyMap, new Point(100,
						100));
		if (newEnemyEntity != null) {
			enemyList.add(newEnemyEntity);
		}
	}

	public List<EnemyEntity> getEnemyList() {
		return enemyList;
	}

	public EnemyEntity getEnemyList(int num) {
		return enemyList.get(num);
	}

	@Override
	public void addEntity(GameEntity ge) {
		// TODO Auto-generated method stub
		enemyList.add((EnemyEntity) ge);
	}

}
