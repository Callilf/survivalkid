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
	
	
	
	//---- Getters and Setters
	public static int getLastId() {
		return lastId;
	}
	public static void setLastId(int lastId) {
		GameEntity.lastId = lastId;
	}
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
