package com.survivalkid.game.entity.item.impl;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.item.ItemEntity;

public class ConcreteItemA extends ItemEntity {

	/**
	 * Create item
	 */
	public ConcreteItemA() {
		super("ItemA", new AnimatedSprite(), 3);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void collide(GameEntity _gameEntity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void die() {
		// TODO Auto-generated method stub

	}

}
