package com.survivalkid.game.entity.enemy;

import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

import com.survivalkid.game.core.Constants;
import com.survivalkid.game.core.Constants.CollisionConstants;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.manager.EnemyManager;

public abstract class EnemyEntity extends GameEntity {
	private static final String TAG = EnemyEntity.class.getSimpleName();

	/** Defense by default of all enemy. */
	protected static final int DEFAULT_DEFENSE = 4;

	/** damage cause to the player the enemy hit */
	protected int dammage;

	/** index of difficulty */
	protected int difficulty;

	/**
	 * attack against other ennemy. If this value is > 0, it can destroy enemy
	 * with less defense
	 */
	protected int attack;
	/**
	 * defense against other enemy. If this value is > of the attack of the
	 * other enemy, it don't die
	 */
	protected int defense;

	// For collisions with the character :
	/** The id of the character colliding. -1 if no one is colliding. */
	private int collidingCharacter;
	/** The number of frames the character has been colliding this enemy. */
	private int collidingFrames;

	/**
	 * When dying is true, the enemy isn't already disappear but don't collide
	 * anymore
	 */
	protected boolean dying;

	/** The creator of the enemy */
	protected EnemyManager creator;

	/** Whether the enemy is active and can collide. */
	protected boolean active;

	/**
	 * 
	 * Constructor called by parents class
	 * 
	 * @param _name
	 *            name of the entity
	 * @param spriteEnum
	 *            the sprite
	 * @param x
	 *            position x initial
	 * @param y
	 *            position y initial
	 * @param _dammage
	 *            how much the perso loose life when he hit the enemy
	 * @param _difficulty
	 *            the index of difficulty of the enemy
	 */
	public EnemyEntity(String _name, SpriteEnum spriteEnum, int _x, int _y, int _dammage, int _difficulty) {
		super(_name, spriteEnum, _x, _y);
		dying = false;
		dammage = _dammage;
		difficulty = _difficulty;
		attack = 0;
		defense = DEFAULT_DEFENSE;
		active = true;
	}

	public EnemyEntity() {
		super();
	}

	@Override
	public void collide(GameEntity _gameEntity) {
		if (!dying && active) {
			if (_gameEntity instanceof Personage) {
				if (_gameEntity.getId() == collidingCharacter) {
					collidingFrames++;
				} else {
					collidingCharacter = _gameEntity.getId();
					collidingFrames = 1;
				}

				if (Constants.DEBUG) {
					Log.d(TAG, this.getName() + " colliding with character " + _gameEntity.getId() + " for "
							+ collidingFrames + " frames !");
				}
						
				if (collidingFrames >= CollisionConstants.MAX_FRAMES_OF_COLLISION) {
					if (Constants.DEBUG) {
						Log.i(TAG, this.getName() + " apply collision to character " + _gameEntity.getId());
					}
					applyCollisionCharacter((Personage) _gameEntity);
				}
			} else if (_gameEntity instanceof EnemyEntity) {
				applyCollisionEnemy((EnemyEntity) _gameEntity);
			}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	}

	public void applyCollisionEnemy(EnemyEntity _enemyEntity) {
		if (_enemyEntity.isActive()) {
			// kill the enemy if attack > enemy defense
			if (attack > _enemyEntity.defense) {
				_enemyEntity.die();
			}
			// die if the enemy attack > defense
			if (_enemyEntity.attack > defense) {
				die();
			}
		}
	}

	public abstract void applyCollisionCharacter(Personage _personage);

	public abstract void initRandomPositionAndSpeed(Point playerPosition);

	/**
	 * @return the collidingCharacter
	 */
	public int getCollidingCharacter() {
		return collidingCharacter;
	}

	/**
	 * @param collidingCharacter
	 *            the collidingCharacter to set
	 */
	public void setCollidingCharacter(int collidingCharacter) {
		this.collidingCharacter = collidingCharacter;
	}

	public void setCreator(EnemyManager creator) {
		this.creator = creator;
	}

	public int getAttack() {
		return attack;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

}
