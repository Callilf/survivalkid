package com.survivalkid.game.entity.personage;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.item.ItemEntity;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.util.CollisionUtil;
import com.survivalkid.game.util.MoveUtil;

public class Bag {

	private static final int FREEZE_TIME_DURATION = 50;
	private ItemEntity slot;
	private boolean locked;
	private AnimatedSprite sprite;
	private Rect touchBox;
	private Personage perso;

	private long freezeExctinction;

	/** Balloon hitbox for debug. */
	public Paint touchBoxPainter;

	public Bag(Personage _perso) {
		perso = _perso;
		sprite = new AnimatedSprite(SpriteEnum.BAG_SLOT);
		sprite.setX(MoveUtil.myBag.x);
		sprite.setY(MoveUtil.myBag.y);
		sprite.play("unlocked", false, true);

		touchBoxPainter = new Paint();
		touchBoxPainter.setColor(Color.MAGENTA);
		touchBoxPainter.setAlpha(200);

		initTouchBox();
	}

	public void initTouchBox() {
		touchBox = new Rect(sprite.getX() - sprite.getWidth() / 3, sprite.getY() - sprite.getHeight()/3, sprite.getX() + sprite.getWidth()
				+ sprite.getWidth() / 3, sprite.getY() + sprite.getHeight());
	}

	public void addItem(ItemEntity item) {
		if (slot == null) {
			slot = item;
			slot.setX(sprite.getX() + sprite.getWidth()/2 - slot.getSprite().getWidth()/2);
			slot.setY(sprite.getY() + sprite.getHeight()/2 - slot.getSprite().getHeight()/2);
			slot.getSprite().setBlinking(false);
		}
	}

	public void checkTouched(MotionEvent event) {
		int action =  event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		if (actionCode == MotionEvent.ACTION_DOWN || actionCode == MotionEvent.ACTION_POINTER_DOWN) {
			int pid = action >> MoveUtil.ACTION_POINTER_INDEX_SHIFT;
			if (freezeExctinction < GameContext.getSingleton().getCurrentTimeMillis()
					&& touchBox.contains((int) event.getX(pid), (int) event.getY(pid))) {
				if(slot == null) {
					toggleLocked();
					freezeExctinction = GameContext.getSingleton().getCurrentTimeMillis() + FREEZE_TIME_DURATION;
				} else {
					slot.collide(perso);
					slot = null;
				}
			}
		}
	}

	public void update(long gameDuration) {
		sprite.update(gameDuration, DirectionConstants.RIGHT);
	}

	public void draw(Canvas canvas) {
		sprite.draw(canvas, DirectionConstants.RIGHT);
		
		if(slot != null) {
			slot.draw(canvas);
		}
		
		if (CollisionUtil.displayHitBoxes) {
			canvas.drawRect(touchBox, touchBoxPainter);
		}
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

	/**
	 * @return the slot
	 */
	public ItemEntity getSlot() {
		return slot;
	}

	/**
	 * @param slot the slot to set
	 */
	public void setSlot(ItemEntity slot) {
		this.slot = slot;

	}
	
	

}
