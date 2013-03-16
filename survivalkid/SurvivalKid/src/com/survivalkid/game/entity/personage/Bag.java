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
	private AnimatedSprite slotSprite;
	private AnimatedSprite bagSprite;
	private Rect touchBox;
	private Personage perso;

	private long freezeExctinction;

	public Paint slotPainter;
	/** Balloon hitbox for debug. */
	public Paint touchBoxPainter;

	public Bag(Personage _perso) {
		perso = _perso;
		slotSprite = new AnimatedSprite(SpriteEnum.BAG_SLOT);
		slotSprite.setX(MoveUtil.myBag.x);
		slotSprite.setY(MoveUtil.myBag.y);
		slotSprite.play("unlocked", false, true);
		
		bagSprite = new AnimatedSprite(SpriteEnum.BAG);
		bagSprite.setX((int) (MoveUtil.myBag.x - bagSprite.getWidth() * 0.6f));
		bagSprite.setY(MoveUtil.myBag.y  + slotSprite.getHeight() - bagSprite.getHeight());

		touchBoxPainter = new Paint();
		touchBoxPainter.setColor(Color.MAGENTA);
		touchBoxPainter.setAlpha(200);

		slotPainter = new Paint();
		slotPainter.setAlpha(180);
		
		initTouchBox();
	}

	public void initTouchBox() {
		touchBox = new Rect(slotSprite.getX() - slotSprite.getWidth() / 3, slotSprite.getY() - slotSprite.getHeight()/3, slotSprite.getX() + slotSprite.getWidth()
				+ slotSprite.getWidth() / 3, slotSprite.getY() + slotSprite.getHeight());
	}

	public void addItem(ItemEntity item) {
		if (slot == null) {
			slot = item;
			slot.setX(slotSprite.getX() + slotSprite.getWidth()/2 - slot.getSprite().getWidth()/2);
			slot.setY(slotSprite.getY() + slotSprite.getHeight()/2 - slot.getSprite().getHeight()/2);
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
		slotSprite.update(gameDuration, DirectionConstants.RIGHT);
	}

	public void draw(Canvas canvas) {
		bagSprite.draw(canvas, DirectionConstants.RIGHT);
		slotSprite.draw(canvas, DirectionConstants.RIGHT, slotPainter);
		
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
			slotSprite.play("locked", false, true);
		} else {
			slotSprite.play("unlocked", false, true);
		}
	}

	/**
	 * @return the sprite
	 */
	public AnimatedSprite getSprite() {
		return slotSprite;
	}

	/**
	 * @param sprite
	 *            the sprite to set
	 */
	public void setSprite(AnimatedSprite sprite) {
		this.slotSprite = sprite;
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
