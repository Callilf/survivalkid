package com.survivalkid.game.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.graphics.Canvas;
import android.graphics.Point;

import com.survivalkid.game.algo.enemy.ThreeStepEnemyGenerator;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.enemy.impl.Caterpillar;
import com.survivalkid.game.entity.enemy.impl.FredCircularSaw;

public class EnemyManager extends ObjectManager {

	private List<EnemyEntity> enemyList;
	private List<EnemyEntity> deadEnemies;
	private long lastEnemy;
	/**
	 * Counter used to see when to generate.
	 */
	private long generationCounter;
	/**
	 * Generation frequency in ms.
	 */
	private static final long GENERATION_FREQUENCY = 2000;

	private static final Map<Integer, List<Class<? extends EnemyEntity>>> enemyDifficultyMap;

	static {
		enemyDifficultyMap = new TreeMap<Integer, List<Class<? extends EnemyEntity>>>();
		ArrayList<Class<? extends EnemyEntity>> zeroDiff = new ArrayList<Class<? extends EnemyEntity>>();
		zeroDiff.add(Caterpillar.class);
		enemyDifficultyMap.put(0, zeroDiff);
		ArrayList<Class<? extends EnemyEntity>> oneDiff = new ArrayList<Class<? extends EnemyEntity>>();
		oneDiff.add(FredCircularSaw.class);
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
		for (EnemyEntity enemy : enemyList) {
			if (enemy.isDead()) {
				deadEnemies.add(enemy);
			} else {
				enemy.update(gameTime);
			}
		}

		// remove the dead enemis from the list so that they are removed from
		// the game
		if (deadEnemies.size() > 0) {
			for (EnemyEntity enemy : deadEnemies) {
				enemyList.remove(enemy);
			}
			deadEnemies.clear();
		}

		// Do we have to generate?
		if (gameTime - generationCounter >= GENERATION_FREQUENCY) {
			generationCounter = gameTime;
			generate();
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
