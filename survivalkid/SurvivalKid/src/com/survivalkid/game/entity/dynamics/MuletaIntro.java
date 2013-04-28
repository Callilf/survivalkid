package com.survivalkid.game.entity.dynamics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.survivalkid.R;
import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.DynamicDrawableObject;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.singleton.SharedVars;
import com.survivalkid.game.util.BitmapUtil;
import com.survivalkid.game.util.MoveUtil;

public class MuletaIntro extends GameEntity implements DynamicDrawableObject  {
	// Intro duration in milliseconds
	private static final int INTRO_DURATION = 1550;
	
	private long expiration;
	private Paint backPaint;
	private Rect blackBackground;
	
	//test MULETA
	private Bitmap muletaText;
	private Rect muletaTextRect;
	
	private boolean slowing;
	
	private AnimatedSprite yugo;
	
	private boolean firstFrame;
	
	public MuletaIntro() {
		expiration = GameContext.getSingleton().gameDuration + INTRO_DURATION;
		blackBackground = new Rect(0,0,MoveUtil.BACKGROUND_RIGHT, MoveUtil.BACKGROUND_BOTTOM);
		backPaint = new Paint();
		backPaint.setColor(Color.BLACK);
//		backPaint.setAlpha(155);
		firstFrame = true;
		slowing = false;
		
		muletaText = BitmapUtil.createBitmap(R.drawable.corrida_intro_text);
		muletaTextRect = BitmapUtil.buildRect(muletaText, -muletaText.getWidth(), MoveUtil.BACKGROUND_HEIGHT / 3);
		
		yugo = new AnimatedSprite(SpriteEnum.YUGO_CORRIDA);
		yugo.play("prepare", true, true);
		yugo.setX(MoveUtil.BACKGROUND_WIDTH);
		yugo.setY(MoveUtil.GROUND - yugo.getHeight());
	}

	public void update(long gameDuration) {
		// TODO Auto-generated method stub
		if(gameDuration >= expiration) {
			die();
		}
		
		if (firstFrame) {
			SharedVars.getSingleton().setFrozen(true);
		}
		
		slowing = yugo.getX() < MoveUtil.BACKGROUND_WIDTH/3;
		
		if (slowing) {
			muletaTextRect.offset(3, 0);
			yugo.setX(yugo.getX()-3);
		} else {
			muletaTextRect.offset(20, 0);
			yugo.setX(yugo.getX()-20);
		}
		
		yugo.update(gameDuration, DirectionConstants.RIGHT);
		
		firstFrame = false;
	}

	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawRect(blackBackground, backPaint);
		canvas.drawBitmap(muletaText, muletaTextRect.left, muletaTextRect.top, null);
		yugo.draw(canvas, DirectionConstants.RIGHT);
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
		SharedVars.getSingleton().setFrozen(false);
		SharedVars.getSingleton().setCorridaPosition(new Point(yugo.getX(), yugo.getY()));
		this.setDead(true);
	}

}
