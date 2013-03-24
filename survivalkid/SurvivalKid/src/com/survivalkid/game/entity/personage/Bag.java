package com.survivalkid.game.entity.personage;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.TouchHandler;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.item.ItemEntity;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.util.CollisionUtil;
import com.survivalkid.game.util.MoveUtil;

public class Bag {

	private static final int FREEZE_TIME_DURATION = 50;
	private ItemEntity storedItem;
	private boolean locked;
	private AnimatedSprite slotSprite;
	private AnimatedSprite bagSprite;
	private Rect touchBox;
	private Personage perso;
	private Point position;

	private long freezeExctinction;

	public Paint slotPainter;
	/** Balloon hitbox for debug. */
	public Paint touchBoxPainter;

	public Bag(Personage _perso) {
		perso = _perso;
		slotSprite = new AnimatedSprite(SpriteEnum.BAG_SLOT);
		slotSprite.play("unlocked", false, true);
		
		bagSprite = new AnimatedSprite(SpriteEnum.BAG);


		touchBoxPainter = new Paint();
		touchBoxPainter.setColor(Color.MAGENTA);
		touchBoxPainter.setAlpha(200);

		slotPainter = new Paint();
		slotPainter.setAlpha(180);
		
		initPosition();
	}
	
	public void initPosition() {
		position = MoveUtil.buttonPosition.getBag();
		slotSprite.setX(position.x);
		slotSprite.setY(position.y);
		bagSprite.setX((int) (position.x - bagSprite.getWidth() * 0.6f));
		bagSprite.setY(position.y  + slotSprite.getHeight() - bagSprite.getHeight());
		touchBox = new Rect(slotSprite.getX() - slotSprite.getWidth() / 3, slotSprite.getY() - slotSprite.getHeight()/3, slotSprite.getX() + slotSprite.getWidth()
				+ slotSprite.getWidth() / 3, slotSprite.getY() + slotSprite.getHeight());
	}

	public void addItem(ItemEntity item) {
		if (storedItem == null) {
			storedItem = item;
			storedItem.setX(slotSprite.getX() + slotSprite.getWidth()/2 - storedItem.getSprite().getWidth()/2);
			storedItem.setY(slotSprite.getY() + slotSprite.getHeight()/2 - storedItem.getSprite().getHeight()/2);
			storedItem.getSprite().setBlinking(false);
			storedItem.redefineHitBoxForItems();
		}
	}

	public void checkTouched(MotionEvent event) {
		TouchHandler touchHandler = new TouchHandler(event);
		if (touchHandler.getActionCode() == MotionEvent.ACTION_DOWN || touchHandler.getActionCode() == MotionEvent.ACTION_POINTER_DOWN) {
			if (freezeExctinction < GameContext.getSingleton().getCurrentTimeMillis()
					&& touchBox.contains(touchHandler.getX(), touchHandler.getY())) {
				if(storedItem == null) {
					toggleLocked();
					freezeExctinction = GameContext.getSingleton().getCurrentTimeMillis() + FREEZE_TIME_DURATION;
				} else {
					storedItem.collide(perso);
					storedItem = null;
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
		
		if(storedItem != null) {
			storedItem.draw(canvas);
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
	public ItemEntity getStoredItem() {
		return storedItem;
	}

	/**
	 * @param slot the slot to set
	 */
	public void setStoredItem(ItemEntity slot) {
		this.storedItem = slot;

	}
	
	/**
	 * @return the touchBox
	 */
	public Rect getTouchBox() {
		return touchBox;
	}
	
}
