package com.survivalkid.game.manager;

import com.survivalkid.game.entity.GameEntity;

public abstract class ObjectManager implements IManager {

	/**
	 * Define the way and frequency the entities are generated 
	 */
	public abstract void generate();
	
	
	public abstract void addEntity(GameEntity ge);
}
