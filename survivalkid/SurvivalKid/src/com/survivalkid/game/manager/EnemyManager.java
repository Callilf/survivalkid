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
import com.survivalkid.game.entity.enemy.impl.FireMeteor;
import com.survivalkid.game.entity.enemy.impl.FredCircularSaw;
import com.survivalkid.game.entity.enemy.impl.Meteore;
import com.survivalkid.game.util.TimerUtil;

public class EnemyManager extends ObjectManager {
	private static final String TAG = EnemyManager.class.getSimpleName();

	private List<EnemyEntity> enemyList;
	private List<EnemyEntity> deadEnemies;
	private List<EnemyEntity> enemiesToAdd;
	
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
	private long generationFrequency = 1500;
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
		ArrayList<Class<? extends EnemyEntity>> twoDiff = new ArrayList<Class<? extends EnemyEntity>>();
		twoDiff.add(FireMeteor.class);
		enemyDifficultyMap.put(2, twoDiff);
	}

	public EnemyManager() {
		enemyList = new ArrayList<EnemyEntity>();
		deadEnemies = new ArrayList<EnemyEntity>();
		enemiesToAdd = new ArrayList<EnemyEntity>();
	}

	public void create() {
		enemyList = new ArrayList<EnemyEntity>();
	}

	public void update(long gameDuration) {
		
		// Do we have to generate?
		if (gameDuration - generationCounter >= generationFrequency) {
			generationCounter = gameDuration;
			generateTimed();
		}
		
		if(generationFrequency != 500 && gameDuration - difficultyIncreasingCounter >= difficultyIncreasingPeriod) {
			difficultyIncreasingCounter = gameDuration;
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
				enemy.updateTimed(gameDuration);
			}
		}

		// remove the dead enemies from the list so that they are removed from the game
		if (!deadEnemies.isEmpty()) {
			for (EnemyEntity enemy : deadEnemies) {
				enemyList.remove(enemy);
			}
			deadEnemies.clear();
		}
		
		// add the enemies create during the update to the game
		if (!enemiesToAdd.isEmpty()) {
			for (EnemyEntity enemy : enemiesToAdd) {
				enemyList.add(enemy);
			}
			enemiesToAdd.clear();
		}
	}

	public void draw(Canvas canvas) {
		for (EnemyEntity enemy : enemyList) {
			enemy.drawTimed(canvas);
		}
	}

	private void generateTimed() {
		if (!TimerUtil.TIMER_ACTIVE) {
			generate();
			return;
		}
		TimerUtil.start("generation");
		generate();
		TimerUtil.end("generation");
	}
	
	@Override
	public void generate() {
		EnemyEntity newEnemyEntity = ThreeStepEnemyGenerator
				.generateRandomEnemy(1, enemyDifficultyMap.size(), enemyDifficultyMap, new Point(100,
						100));
		if (newEnemyEntity != null) {
			newEnemyEntity.setCreator(this);
			enemyList.add(newEnemyEntity);
		}
	}
	
	/**
	 * Create a new Enemy
	 * @param <T> type of the enemy
	 * @param _enemyClass class of the enemy
	 * @param _isInUpdate true if the enemy is create in the update loop of EnemyManager
	 * @return the enemy which was created
	 */
	public <T extends EnemyEntity> T createEnemy(Class<T> _enemyClass, boolean _isInUpdate) {
		T newEnemy = null;
		try {
			newEnemy = _enemyClass.newInstance();
			newEnemy.initRandomPositionAndSpeed(new Point(100,100));
			newEnemy.setCreator(this);
			if (_isInUpdate) {
				enemiesToAdd.add(newEnemy);
			}
			else {
				enemyList.add(newEnemy);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return newEnemy;
	}

	public List<EnemyEntity> getEnemyList() {
		return enemyList;
	}

	public EnemyEntity getEnemyList(int num) {
		return enemyList.get(num);
	}

	@Override
	public void addEntity(GameEntity ge) {
		enemyList.add((EnemyEntity) ge);
	}

}
