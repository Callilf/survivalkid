package com.survivalkid.game.entity.enemy.impl;

import android.graphics.Rect;

import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.enemy.EnemyEntity;

public class ConcreteEnemyA extends EnemyEntity {

	/**
	 * Create enemy
	 */
	public ConcreteEnemyA() {
		super("EnnemiA", new Rect(), 10, 3);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void collide(GameEntity _gameEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub
		
	}

}
