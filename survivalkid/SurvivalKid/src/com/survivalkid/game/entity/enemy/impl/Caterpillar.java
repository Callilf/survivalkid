package com.survivalkid.game.entity.enemy.impl;

import android.graphics.Bitmap;

import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.personage.Personage;

public class Caterpillar extends EnemyEntity {

	/**
	 * Create enemy
	 */
	public Caterpillar(Bitmap _bitmap, int _x, int _y, int _nbColums, int _nbRows) {
		super("Caterpillar", _bitmap, _x, _y, _nbColums, _nbRows, 10, 3);
		
		redefineHitBox( (sprite.getWidth()*18) / 100, (sprite.getHeight()*65) / 100, (sprite.getWidth()*74) / 100, (sprite.getHeight()*35) / 100);
		addAnimation("crawl", new int[]{0,1,2,3,2,1}, 13);
		play("crawl", true, true);
	}
	
	@Override
	public void update(long gameTime) {
		super.update(gameTime);
		
		if(touchLeft()) {
			direction = DirectionConstants.RIGHT;
		} else if(touchRight()) {
			direction = DirectionConstants.LEFT;
		}
		
		if(direction == DirectionConstants.LEFT) {
			sprite.offset(-2, 0);
		} else {
			sprite.offset(2, 0);
		}
	}

	@Override
	public void collide(GameEntity _gameEntity) {
		if (_gameEntity instanceof Personage) {
			((Personage) _gameEntity).getLife().looseLife(1);
		}
		// the method die should be called after x touch (or at the first touch)
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub

	}

}
