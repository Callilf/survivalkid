package com.survivalkid.game.entity.enemy.impl;

import android.graphics.Canvas;
import android.graphics.Point;

import com.survivalkid.R;
import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.Life.EnumLife;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.util.BitmapUtil;
import com.survivalkid.game.util.MoveUtil;

public class Meteore extends EnemyEntity {

	private AnimatedSprite deathAnim;
	private AnimatedSprite fireAnim;
	private boolean dying;
	private int fall = 1;
	
	public Meteore() {
		super("Meteore", BitmapUtil.createBitmap(R.drawable.meteor), 0, 0, 1, 1, 10, 1);
	}
	
	public void init() {
		affectedByWalls = false;
		affectedByCeiling = false;
		affectedByFloor = false;

		// fire
		fireAnim = new AnimatedSprite(
				BitmapUtil.createBitmap(R.drawable.fire), 0, 0, 6, 1);
		fireAnim.addAnimation("fire", new int[] { 0, 1, 2, 3, 4, 5 }, 10);
		
		// death
		deathAnim = new AnimatedSprite(
				BitmapUtil.createBitmap(R.drawable.dead_anim_small), 0, 0, 7, 1);
		dying = false;
		deathAnim.addAnimation("die", new int[] { 0, 1, 2, 3, 4, 5, 6 }, 25);

		redefineHitBox((sprite.getWidth() * 18) / 100,
				(sprite.getHeight() * 18) / 100,
				(sprite.getWidth() * 74) / 100, (sprite.getHeight() * 74) / 100);
		
		setPositionFire();
		fireAnim.play("fire", true, true);
	}
	
	@Override
	public void initRandomPositionAndSpeed(Point playerPosition) {
		int random = (int) (Math.random() * MoveUtil.SCREEN_WIDTH);
		sprite.setX(random);
		sprite.setY(1-sprite.getHeight());
		direction = (random <= MoveUtil.SCREEN_WIDTH/2)? DirectionConstants.LEFT : DirectionConstants.RIGHT;

		init();
	}

	@Override
	public void collide(GameEntity _gameEntity) {
		if (!dying) {
			if (_gameEntity instanceof Personage) {
				((Personage) _gameEntity).takeDamage(dammage, EnumLife.TAKE_DAMAGE);
				die();
			}
		}
	}

	@Override
	public void die() {
		dying = true;

		deathAnim.setX((sprite.getX() + sprite.getWidth() / 2)
				- deathAnim.getWidth() / 2);
		deathAnim.setY((sprite.getY() + sprite.getHeight() / 2)
				- deathAnim.getHeight() / 2);
		deathAnim.play("die", false, true);
	}
	
	private void setPositionFire() {
		int decalX = (direction == DirectionConstants.LEFT)? 
				sprite.getX() + sprite.getWidth() / 3 : sprite.getX() + sprite.getWidth()* 2/3;
		
		fireAnim.setX(decalX - fireAnim.getWidth() / 2);
		fireAnim.setY(sprite.getY() - fireAnim.getHeight() * 3/4);
	}
	
	@Override
	public void update(long gameTime) {
		if (dying) {
			deathAnim.update(gameTime, DirectionConstants.RIGHT);
			if (deathAnim.isAnimationFinished()) {
				dead = true;
			}

			return;
		}
		
		if (direction == DirectionConstants.LEFT) {
			sprite.offset(1, fall);
		} else {
			sprite.offset(-1, fall);
		}

		super.update(gameTime);
		setPositionFire();
		fireAnim.update(gameTime, direction);

		if (sprite.getX() + sprite.getWidth() < 0	|| sprite.getX() > MoveUtil.SCREEN_WIDTH) {
			dead = true;
		}
		else if (sprite.getY() + sprite.getHeight()*3/4 >= MoveUtil.GROUND) {
			die();
		}
		else if (sprite.getY() + sprite.getHeight()*2/3 > 0) {
			fall = 8;
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (dying) {
			deathAnim.draw(canvas, direction);
		} else {
			fireAnim.draw(canvas, direction);
			super.draw(canvas);
		}
	}

}
