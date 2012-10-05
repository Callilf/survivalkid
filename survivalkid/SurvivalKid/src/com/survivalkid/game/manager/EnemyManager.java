package com.survivalkid.game.manager;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.personage.Personage;

public class EnemyManager extends ObjectManager {

	private List<EnemyEntity> enemyList;
	private long lastEnemy;
	
	public EnemyManager() {
		enemyList = new ArrayList<EnemyEntity>();
	}
	
	public void create() {
		// TODO Auto-generated method stub

	}

	public void update() {
		// TODO Auto-generated method stub

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

}
