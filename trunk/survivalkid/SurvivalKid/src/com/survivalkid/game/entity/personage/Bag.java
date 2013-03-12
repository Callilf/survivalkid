package com.survivalkid.game.entity.personage;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.item.ItemEntity;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.util.MoveUtil;

public class Bag {

	private static final int FREEZE_TIME_DURATION = 100;
	private ItemEntity slot;
	private boolean locked;
	private AnimatedSprite sprite;
	private Rect touchBox;
	
	private long freezeExctinction;

	public Bag() {
		sprite = new AnimatedSprite(SpriteEnum.BAG_SLOT);
		sprite.setX(MoveUtil.SCREEN_WIDTH - MoveUtil.btn_up.getWidth() - MoveUtil.btn_up.getWidth()/2 - sprite.getWidth()*2);
		sprite.setY(MoveUtil.SCREEN_HEIGHT - sprite.getHeight() - sprite.getHeight()/10);
		sprite.play("unlocked", false, true);
		
		touchBox = new Rect(sprite.getX(), sprite.getY(), sprite.getX()+sprite.getWidth(), sprite.getY()+sprite.getHeight());
	}

	public void addItem(ItemEntity item) {
		if (slot == null) {
			slot = item;
		}
	}
	
	public void checkTouched(MotionEvent event) {
		if(freezeExctinction < GameContext.getSingleton().getCurrentTimeMillis() && touchBox.contains((int)event.getX(), (int)event.getY())) {
			toggleLocked();
			freezeExctinction = GameContext.getSingleton().getCurrentTimeMillis() + FREEZE_TIME_DURATION;
		}
	}

	public void update(long gameDuration) {
		sprite.update(gameDuration, DirectionConstants.RIGHT);
	}
	
	public void draw(Canvas canvas) {
		sprite.draw(canvas, DirectionConstants.RIGHT);
	}
	
	
	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * @param locked
	 *            the locked to set
	 */
	public void toggleLocked() {
		this.locked = !this.locked;
		if (this.locked) {
			sprite.play("locked", false, true);
		} else {
			sprite.play("unlocked", false, true);
		}
	}

	/**
	 * @return the sprite
	 */
	public AnimatedSprite getSprite() {
		return sprite;
	}

	/**
	 * @param sprite
	 *            the sprite to set
	 */
	public void setSprite(AnimatedSprite sprite) {
		this.sprite = sprite;
	}

}
