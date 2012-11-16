package com.survivalkid.game.entity.enemy.impl;

import android.graphics.Canvas;
import android.graphics.Point;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.Life.EnumLife;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.util.MoveUtil;

public class Meteore extends EnemyEntity {

	protected int fallInit = 1;
	protected int fallNext = 8;
	protected int shiftX = 1;
	
	protected AnimatedSprite deathAnim;
	private AnimatedSprite fireAnim;
	
	private int directionFire;
	
	public Meteore() {
		super("Meteore", SpriteEnum.METEORE, 0, 0, 10, 1);
	}
	
	/**
	 * 
	 * Constructor use for the fireMeteor
	 * @param name name
	 * @param sprite the sprite
	 * @param x position x
	 * @param y position y
	 * @param damage the damages
	 * @param difficulty the difficulty
	 */
	protected Meteore(String _name, SpriteEnum _sprite, int _x, int _y, int _damage, int _difficulty) {
		super(_name, _sprite, _x, _y, _damage, _difficulty);
	}
	
	private void init() {
		affectedByWalls = false;
		affectedByCeiling = false;
		affectedByFloor = false;

		// fire
		fireAnim = new AnimatedSprite(SpriteEnum.FIRE_TRAIL, 0, 0);
		
		// death
		initDeathAnim();

		redefineHitBox((sprite.getWidth() * 18) / 100,
				(sprite.getHeight() * 18) / 100,
				(sprite.getWidth() * 74) / 100, (sprite.getHeight() * 74) / 100);
		
		setPositionFire();
		fireAnim.play("fire", true, true);
	}
	
	protected void initDeathAnim() {
		deathAnim = new AnimatedSprite(SpriteEnum.METEORE_EXPLOSION, 0, 0);
	}
	
	@Override
	public void initRandomPositionAndSpeed(Point playerPosition) {
		int random = (int) (Math.random() * MoveUtil.SCREEN_WIDTH);
		sprite.setX(random);
		sprite.setY(1-sprite.getHeight());
		if (random <= MoveUtil.SCREEN_WIDTH/2) {
			direction = DirectionConstants.RIGHT;
			directionFire = DirectionConstants.LEFT;
		}
		else {
			direction = DirectionConstants.LEFT;
			directionFire = DirectionConstants.RIGHT;
		}

		// init speed
		setSpeedY(fallInit);
		if (direction == DirectionConstants.LEFT) {
			setSpeedX(-shiftX/(float)fallNext);
		} else {
			setSpeedX(shiftX/(float)fallNext);
		}
		
		init();
	}

	@Override
	public void applyCollision(GameEntity _gameEntity) {
		if (_gameEntity instanceof Personage) {
			((Personage) _gameEntity).takeDamage(dammage, EnumLife.TAKE_DAMAGE);
			die();
		}
	}

	@Override
	public void die() {
		dying = true;

		deathAnim.setX((sprite.getX() + sprite.getWidth() / 2)
				- deathAnim.getWidth() / 2);
		deathAnim.setY(sprite.getY() - deathAnim.getHeight()/2);
		deathAnim.play("explode", false, true);
	}
	
	private void setPositionFire() {
		int decalX = (direction == DirectionConstants.LEFT)? 
				sprite.getX() + sprite.getWidth()* 2/3 : sprite.getX() + sprite.getWidth() / 3;
		
		fireAnim.setX(decalX - fireAnim.getWidth() / 2);
		fireAnim.setY(sprite.getY() - fireAnim.getHeight() * 3/4);
	}
	
	@Override
	public void update(long gameTime) {
		if (dying) {
			deathAnim.update(gameTime, DirectionConstants.RIGHT);
			if (deathAnim.isAnimationFinished()) {
				dead = true;
				terminate();
			}

			return;
		}

		if (sprite.getX() + sprite.getWidth() < 0	|| sprite.getX() > MoveUtil.SCREEN_WIDTH) {
			dead = true;
		}
		else if (sprite.getY() + sprite.getHeight()*3/4 >= MoveUtil.GROUND) {
			die();
		}
		else if (sprite.getY() + sprite.getHeight()*2/3 > 0) {
			if (direction == DirectionConstants.LEFT) {
				setSpeedX(-shiftX);
			} else {
				setSpeedX(shiftX);
			}
			setSpeedY(fallNext);		
		}
		
		super.update(gameTime);
		// update of the fire after the meteor is moved
		setPositionFire();
		fireAnim.update(gameTime, directionFire);
	}
	
	protected void terminate() {
		// nothing to do : use by the fire meteor to pop the ground fire
	}

	@Override
	public void draw(Canvas canvas) {
		if (dying) {
			deathAnim.draw(canvas, DirectionConstants.RIGHT);
		} else {
			fireAnim.draw(canvas, directionFire);
			super.draw(canvas);
		}
	}

}
