package com.survivalkid.game.entity.dynamics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.survivalkid.R;
import com.survivalkid.game.core.DynamicDrawableObject;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.util.BitmapUtil;
import com.survivalkid.game.util.MoveUtil;

public class MuletaOle extends GameEntity implements DynamicDrawableObject  {
	// Intro duration in milliseconds
	private static final int INTRO_DURATION = 1000;
	
	private long expiration;
	
	//test MULETA
	private Bitmap ole;
	private Rect oleRect;
	public MuletaOle() {
		expiration = GameContext.getSingleton().gameDuration + INTRO_DURATION;
		
		ole = BitmapUtil.createBitmap(R.drawable.ole);
		oleRect = BitmapUtil.buildRect(ole, MoveUtil.BACKGROUND_WIDTH / 10 , MoveUtil.BACKGROUND_HEIGHT / 3);
	}

	public void update(long gameDuration) {
		// TODO Auto-generated method stub
		if(gameDuration >= expiration) {
			die();
		}
	}

	public void draw(Canvas canvas) {
		canvas.drawBitmap(ole, oleRect.left, oleRect.top, null);
	}

	public boolean isDead() {
		// TODO Auto-generated method stub
		return this.dead;
	}

	@Override
	public void collide(GameEntity _gameEntity) {
		// TODO Auto-generated method stub
	}

	@Override
	public void die() {
		this.setDead(true);
	}

}
