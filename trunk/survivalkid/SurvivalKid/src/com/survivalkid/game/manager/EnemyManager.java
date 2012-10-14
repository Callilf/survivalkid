package com.survivalkid.game.manager;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.personage.Personage;

public class EnemyManager extends ObjectManager {

	private List<EnemyEntity> enemyList;
	private List<EnemyEntity> deadEnemies;
	private long lastEnemy;
	
	public EnemyManager() {
		enemyList = new ArrayList<EnemyEntity>();
		deadEnemies = new ArrayList<EnemyEntity>();
	}
	
	public void create() {
		// TODO Auto-generated method stub
		enemyList = new ArrayList<EnemyEntity>();
	}

	public void update(long gameTime) {
		for(EnemyEntity enemy : enemyList) {
			if(enemy.isDead()) {
				deadEnemies.add(enemy);
			} else {
				enemy.update(gameTime);
			}
		}
		
		//remove the dead enemis from the list so that they are removed from the game
		if (deadEnemies.size() > 0) {
			for (EnemyEntity enemy : deadEnemies) {
				enemyList.remove(enemy);
			}
			deadEnemies.clear();
		}
	}

	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		for(EnemyEntity enemy : enemyList) {
			enemy.draw(canvas);
		}
	}

	@Override
	public void generate() {
		// TODO Auto-generated method stub

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
		enemyList.add((EnemyEntity)ge);
	}

}
