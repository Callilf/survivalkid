package com.survivalkid.game.entity.item.impl;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Animation;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.item.ItemEntity;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.util.CollisionUtil;
import com.survivalkid.game.util.DesignUtil;
import com.survivalkid.game.util.MoveUtil;

public class BalloonCrate extends ItemEntity {

	private Rect balloonTouchBox;

	private AnimatedSprite deathAnim;
	private boolean dying;
	private ItemEntity containedItem;

	// Used to check when the crate leaves the screen
	private boolean wasOnScreen;

	private boolean pierced = false;
	private boolean explosionBegin = false;
	private boolean exploding = false;
	private boolean falling = false;
	private boolean releaseObject = false;

	/** Default constructor. */
	public BalloonCrate() {
		super("Balloon", SpriteEnum.BALLOON_CRATE, 0, 0, 0);
	};

	/** Default constructor. */
	public BalloonCrate(int _x, int _y) {
		super("Balloon", SpriteEnum.BALLOON_CRATE, _x, _y, 0);
		init();
	};

	/** Initialize the enemy. */
	private void init() {
		timeToLive = 15000;

		gravity = 0;
		affectedByFloor = true;
		affectedByCeiling = false;
		affectedByWalls = false;

		deathAnim = new AnimatedSprite(SpriteEnum.CRATE_EXPLOSION, 0, 0);
		dying = false;

		redefineHitBoxForItems();

		setBalloonTouchBox(new Rect(sprite.getX() + (sprite.getWidth() * 20) / 100, sprite.getY() + sprite.getHeight()
				/ 100, sprite.getX() + (sprite.getWidth() * 20) / 100 + (sprite.getWidth() * 60) / 100, sprite.getY()
				+ sprite.getHeight() / 100 + (sprite.getHeight() * 50) / 100));

		new Animation("test", new int[] { 2 }, 8);

		play("swing", true, true);
	}
	
	@Override
	public void redefineHitBoxForItems() {
		redefineHitBox((sprite.getWidth() * 30) / 100, (sprite.getHeight() * 65) / 100, (sprite.getWidth() * 36) / 100,
				(sprite.getHeight() * 30) / 100);
	}

	@Override
	public void update(long gameDuration) {
		if (explosionBegin) {
			play("explode", false, true);
			exploding = true;
			explosionBegin = false;
			super.update(gameDuration);
			return;
		} else if (exploding) {
			if (sprite.isAnimationFinished()) {
				falling = true;
				exploding = false;
				play("exploded", true, true);
			}
			super.update(gameDuration);
			return;
		} else if (falling) {
			stop();
			gravity = 1;
		}

		if (dying) {
			deathAnim.update(gameDuration, direction);
			if (deathAnim.isAnimationFinished()) {
				dead = true;
			}
			return;
		}

		setBalloonTouchBox(new Rect(sprite.getX() + (sprite.getWidth() * 20) / 100, sprite.getY() + sprite.getHeight()
				/ 100, sprite.getX() + (sprite.getWidth() * 20) / 100 + (sprite.getWidth() * 60) / 100, sprite.getY()
				+ sprite.getHeight() / 100 + (sprite.getHeight() * 50) / 100));

		if (onFloor() && onScreen()) {
			releaseObject = true;
			explode();
		}

		if (onScreen() && !wasOnScreen) {
			wasOnScreen = true;
		} else if (wasOnScreen && !onScreen()) {
			dead = true;
			containedItem.setDead(true);
		}

		super.update(gameDuration);
	}

	@Override
	public void die() {
		dead = true;
	}

	@Override
	public void draw(Canvas canvas) {
		if (dying && !dead) {
			deathAnim.draw(canvas, direction);
		} else {
			super.draw(canvas);
			if (CollisionUtil.displayHitBoxes) {
				canvas.drawRect(balloonTouchBox, DesignUtil.PAINT_HITBOX_BALLON);
			}
		}
	}

	// Launch the explosion of the crate
	public void explode() {
		dying = true;
		falling = false;

		deathAnim.setX((sprite.getX() + sprite.getWidth() / 2) - deathAnim.getWidth() / 2);
		deathAnim.setY(sprite.getY() + sprite.getHeight() - deathAnim.getHeight());
		deathAnim.play("explode", false, true);
	}

	// When the balloon is touched by the user
	public void touched() {
		pierced = true;
		explosionBegin = true;
		setSpeedX(0);
		setSpeedY(0);
	}

	/**
	 * Initialize the position, speed and misc. attributes of the enemy.
	 */
	@Override
	public void initRandomPositionAndSpeed(Point playerPosition) {

		int randomSide = (int) (Math.random() * 2);
		int randomSpeed = (int) (Math.random() * 3);

		int randomY = (int) (MoveUtil.getRandomPositionY(5)) - sprite.getHeight();
		sprite.setY(randomY);

		if (randomSide < 1) {
			// LEFT
			sprite.setX(MoveUtil.BACKGROUND_LEFT - sprite.getWidth());
			setSpeedX(4 + randomSpeed);
		} else {
			// RIGHT
			sprite.setX(MoveUtil.BACKGROUND_RIGHT);
			setSpeedX(-4 - randomSpeed);
		}

		if (sprite.getY() < (MoveUtil.GROUND - MoveUtil.BACKGROUND_TOP) / 2) {
			float randomFallSpeed = 1;
			setSpeedY(randomFallSpeed);
		} else {
			float randomFallSpeed = 1;
			setSpeedY(-randomFallSpeed);
		}

		init();
	}

	@Override
	public void collide(GameEntity _gameEntity) {
		// Nothing
	}
	
	@Override
	public void collideWithPerso(Personage _perso) {
		// Nothing
	}

	/**
	 * @return the balloonTouchBox
	 */
	public Rect getBalloonTouchBox() {
		return balloonTouchBox;
	}

	/**
	 * @param balloonTouchBox
	 *            the balloonTouchBox to set
	 */
	public void setBalloonTouchBox(Rect balloonTouchBox) {
		this.balloonTouchBox = balloonTouchBox;
	}

	/**
	 * @return the releaseObject
	 */
	public boolean isReleaseObject() {
		return releaseObject;
	}

	/**
	 * @return the pierced
	 */
	public boolean isPierced() {
		return pierced;
	}

	/**
	 * @return the containedItem
	 */
	public ItemEntity getContainedItem() {
		return containedItem;
	}

	/**
	 * @param containedItem
	 *            the containedItem to set
	 */
	public void setContainedItem(ItemEntity containedItem) {
		this.containedItem = containedItem;
	}

}
