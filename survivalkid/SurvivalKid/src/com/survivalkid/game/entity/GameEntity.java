package com.survivalkid.game.entity;

import android.graphics.Rect;

public abstract class GameEntity {
	/** Last affected id. */
	public static int lastId;
	
	/** The id of the gameEntity. */
	private int id;
	/** The name of the gameEntity. */
	private String name;
	/** The sprite (will be an animation in the future). */
	private Rect sprite;
	
	public abstract void collide(GameEntity _gameEntity);
	public abstract void draw();
	public abstract void die();
	
	/**
	 * Constructor called by parents class
	 * 
	 * @param _name name of the entity
	 * @param _sprite sprite of the entity
	 */
	public GameEntity(String _name, Rect _sprite) {
		id = lastId++;
		name = _name;
		sprite = _sprite;
	}
	
	//---- Getters and Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Rect getSprite() {
		return sprite;
	}
	public void setSprite(Rect sprite) {
		this.sprite = sprite;
	}
}
