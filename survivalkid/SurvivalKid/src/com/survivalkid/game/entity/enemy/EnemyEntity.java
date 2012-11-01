package com.survivalkid.game.entity.enemy;

import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

import com.survivalkid.game.core.Constants.CollisionConstants;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.personage.Personage;

public abstract class EnemyEntity extends GameEntity {
	private static final String TAG = EnemyEntity.class.getSimpleName();

	protected int dammage;
	protected int difficulty;
	
	//For collisions with the character :
	/** The id of the character colliding. -1 if no one is colliding. */
	private int collidingCharacter;
	/**The number of frames the character has been colliding this enemy. */
	private int collidingFrames;

	/** When dying is true, the enemy isn't already disappear but don't collide anymore */
	protected boolean dying;

	/**
	 * 
	 * Constructor called by parents class
	 * 
	 * @param _name name of the entity
	 * @param spriteEnum the sprite
	 * @param x position x initial
	 * @param y position y initial
	 * @param _dammage how much the perso loose life when he hit the enemy
	 * @param _difficulty the index of difficulty of the enemy
	 */
	public EnemyEntity(String _name, SpriteEnum spriteEnum, int _x, int _y, int _dammage, int _difficulty) {
		super(_name, spriteEnum, _x, _y);
		dying = false;
		dammage = _dammage;
		difficulty = _difficulty;
	}

	public EnemyEntity() {
		super();
	}

	@Override
	public void collide(GameEntity _gameEntity) {
		if (_gameEntity instanceof Personage && !dying) {
			if(_gameEntity.getId() == collidingCharacter) {
				collidingFrames ++;
			} else {
				collidingCharacter = _gameEntity.getId();
				collidingFrames = 1;
			}
			
			Log.d(TAG, this.getName() + " colliding with character " + _gameEntity.getId() + " for " + collidingFrames + " frames !");
			
			if(collidingFrames >= CollisionConstants.MAX_FRAMES_OF_COLLISION) {
				Log.i(TAG, this.getName() + " apply collision to character " + _gameEntity.getId());
				applyCollision(_gameEntity);
			}
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	}

	public abstract void applyCollision(GameEntity _gameEntity);
	public abstract void initRandomPositionAndSpeed(Point playerPosition);

	/**
	 * @return the collidingCharacter
	 */
	public int getCollidingCharacter() {
		return collidingCharacter;
	}

	/**
	 * @param collidingCharacter the collidingCharacter to set
	 */
	public void setCollidingCharacter(int collidingCharacter) {
		this.collidingCharacter = collidingCharacter;
	}

	

}
