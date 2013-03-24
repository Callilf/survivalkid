package com.survivalkid.game.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.survivalkid.game.algo.enemy.ThreeStepEnemyGenerator;
import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.enemy.impl.Bull;
import com.survivalkid.game.entity.enemy.impl.Caterpillar;
import com.survivalkid.game.entity.enemy.impl.FireMeteor;
import com.survivalkid.game.entity.enemy.impl.CircularSaw;
import com.survivalkid.game.entity.enemy.impl.Meteore;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.singleton.SharedVars;
import com.survivalkid.game.util.DesignUtil;
import com.survivalkid.game.util.TimerUtil;

public class EnemyManager extends ObjectManager {
	private static final String TAG = EnemyManager.class.getSimpleName();

	private List<EnemyEntity> enemyList;
	private List<EnemyEntity> enemyKillerList; // list the enemy with attack which can kill other enemy
	private List<EnemyEntity> deadEnemies;
	private List<EnemyEntity> enemiesToAdd;
	
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
	
	// change of the speed
	private float alterationSpeed = 1f;
	private float counterAlterationSpeed = 1f;

	private static final Map<Integer, List<Class<? extends EnemyEntity>>> enemyDifficultyMap;
	
	//TEMP
	private Paint paint;

	static {
		enemyDifficultyMap = new TreeMap<Integer, List<Class<? extends EnemyEntity>>>();
		ArrayList<Class<? extends EnemyEntity>> zeroDiff = new ArrayList<Class<? extends EnemyEntity>>();
		zeroDiff.add(Caterpillar.class);
		enemyDifficultyMap.put(0, zeroDiff);
		ArrayList<Class<? extends EnemyEntity>> oneDiff = new ArrayList<Class<? extends EnemyEntity>>();
		oneDiff.add(CircularSaw.class);
		oneDiff.add(Meteore.class);
		enemyDifficultyMap.put(1, oneDiff);
		ArrayList<Class<? extends EnemyEntity>> twoDiff = new ArrayList<Class<? extends EnemyEntity>>();
		twoDiff.add(FireMeteor.class);
		twoDiff.add(Bull.class);
		enemyDifficultyMap.put(2, twoDiff);
	}

	public EnemyManager() {
		enemyList = new ArrayList<EnemyEntity>();
		enemyKillerList = new ArrayList<EnemyEntity>();
		deadEnemies = new ArrayList<EnemyEntity>();
		enemiesToAdd = new ArrayList<EnemyEntity>();
		
		//Text
		paint = DesignUtil.createTextPaint(Color.BLACK, 30);
	}

	public void create() {
		enemyList = new ArrayList<EnemyEntity>();
	}

	public void update(long gameDuration) {
		//Reinit the list of bull warnings
		SharedVars.getSingleton().getBullWarningLeftList().clear();
		SharedVars.getSingleton().getBullWarningRightList().clear();
		
		// Do we have to generate?
		if (gameDuration - generationCounter >= generationFrequency/alterationSpeed) {
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
		
		updateEnemiesWithSpeedAlteration(gameDuration);

		// remove the dead enemies from the list so that they are removed from the game
		if (!deadEnemies.isEmpty()) {
			for (EnemyEntity enemy : deadEnemies) {
				if (enemy.getAttack() > 0) {
					enemyKillerList.remove(enemy);
				}
				enemyList.remove(enemy);
			}
			deadEnemies.clear();
		}
		
		// add the enemies create during the update to the game
		if (!enemiesToAdd.isEmpty()) {
			for (EnemyEntity enemy : enemiesToAdd) {
				addEnemy(enemy);
			}
			enemiesToAdd.clear();
		}
	}
	
	/**
	 * do the update of all enemy with potential multiple update
	 * 
	 * @param gameDuration the game duration
	 */
	private void updateEnemiesWithSpeedAlteration(long gameDuration) {
		// test the change of speed
		if (alterationSpeed != GameContext.getSingleton().getAlterationSpeedEnemy()) {
			alterationSpeed = GameContext.getSingleton().getAlterationSpeedEnemy();
			counterAlterationSpeed = 1f;
		}
		
		// depend of the alterationSpeed, do zero, one, or multiple update
		
		// case where the speed is normal
		if (alterationSpeed == 1) {
			updateEnemies(gameDuration);
		}
		// case where alterationSpeed < 1
		else if (alterationSpeed < 1) {
			if (counterAlterationSpeed >= 1) {
				updateEnemies(gameDuration);
				counterAlterationSpeed = alterationSpeed;
			}
			else {
				counterAlterationSpeed += alterationSpeed;
			}
		}
		// case where alterationSpeed > 1
		else {
			while(counterAlterationSpeed >= 1) {
				updateEnemies(gameDuration);
				counterAlterationSpeed -= 1;
			}
			counterAlterationSpeed = alterationSpeed;
		}
	}
	
	/**
	 * do one update of all enemy
	 * 
	 * @param gameDuration the game duration
	 */
	private void updateEnemies(long gameDuration) {
		for (EnemyEntity enemy : enemyList) {
			if (enemy.isDead()) {
				deadEnemies.add(enemy);
			} else {
				enemy.update(gameDuration);
			}
		}
	}

	public void draw(Canvas canvas) {
		for (EnemyEntity enemy : enemyList) {
			enemy.drawTimed(canvas);
		}
		
		if(SharedVars.getSingleton().getBullWarningLeftList().size() > 1) {
			AnimatedSprite temp = SharedVars.getSingleton().getBullWarningLeftList().iterator().next();
			canvas.drawText(String.valueOf(SharedVars.getSingleton().getBullWarningLeftList().size()), temp.getX() + temp.getWidth()/3, temp.getY() + temp.getHeight()/2.5f, paint);
		}
		if(SharedVars.getSingleton().getBullWarningRightList().size() > 1) {
			AnimatedSprite temp = SharedVars.getSingleton().getBullWarningRightList().iterator().next();
			canvas.drawText(String.valueOf(SharedVars.getSingleton().getBullWarningRightList().size()), temp.getX() + temp.getWidth()/3, temp.getY() + temp.getHeight()/2.5f, paint);
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
			addEnemy(newEnemyEntity);
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
				addEnemy(newEnemy);
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

	public List<EnemyEntity> getEnemyKillerList() {
		return enemyKillerList;
	}

	@Override
	public void addEntity(GameEntity ge) {
		enemyList.add((EnemyEntity) ge);
	}
	
	private void addEnemy(EnemyEntity ee) {
		if (ee.getAttack() > 0) {
			enemyKillerList.add(ee);
		}
		enemyList.add(ee);
	}

}
