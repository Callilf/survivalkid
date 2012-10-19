package com.survivalkid.game.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.enums.StateEnum;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.util.CollisionUtil;
import com.survivalkid.game.util.MoveUtil;

public abstract class GameEntity {

	/** TAG for the logs. */
	private static final String TAG = GameEntity.class.getSimpleName();

	/** Last affected id. */
	public static int lastId;

	/** The id of the gameEntity. */
	private int id;
	/** The name of the gameEntity. */
	private String name;
	/** The sprite (will be an animation in the future). */
	protected AnimatedSprite sprite;

	/** The hit box. */
	protected Rect offsets;
	protected Rect hitBox;

	// Speed attributes
	private float speedX;
	private float speedY;

	/** Whether it is affected by walls */
	protected boolean affectedByWalls;
	/** Whether it is affected by the floor */
	protected boolean affectedByFloor;
	/** Whether it is affected by the ceiling */
	protected boolean affectedByCeiling;

	/** Direction. */
	protected int direction;

	/** actions. */
	protected boolean isMovingHorizontally;
	protected boolean isJumpingUp;
	protected boolean isJumpingDown;
	protected boolean isOnFloor;
	protected int gravity = 0;
	protected int maxSpeedDown = 20;

	/** States. */
	private boolean changedState;
	protected StateEnum state;
	protected boolean dead;

	// for test
	private int cptTest = 0;
	protected boolean overlaping = false;

	// ----------------------------------------------------
	// ---- Constructor
	// ----------------------------------------------------

	/**
	 * Constructor called by parents class
	 * 
	 * @param _name
	 *            name of the entity
	 * @param _sprite
	 *            sprite of the entity
	 */
	public GameEntity(String _name, Bitmap bitmap, int x, int y, int nbColum, int nbRows) {
		id = lastId++;
		name = _name;
		sprite = new AnimatedSprite(bitmap, x, y, nbColum, nbRows);
		offsets = new Rect(0, 0, sprite.getWidth(), sprite.getHeight());
		hitBox = new Rect(sprite.getX() + offsets.left, sprite.getY() + offsets.top, sprite.getX() + offsets.left
				+ offsets.right, sprite.getY() + offsets.top + offsets.bottom);
		direction = DirectionConstants.RIGHT;

		isMovingHorizontally = false;
		isJumpingUp = false;
		isJumpingDown = false;
		isOnFloor = false;

		affectedByFloor = true;
		affectedByWalls = true;
		affectedByCeiling = true;

		changedState = false;
		dead = false;

		// check the correspondence between sprite and hitbox
		Log.d(TAG, "init Sprite : X=" + sprite.getX() + ", Y=" + sprite.getY() + ", width=" + sprite.getWidth()
				+ ", height=" + sprite.getHeight());
		Log.d(TAG, "init hitbox : left=" + hitBox.left + ", right=" + hitBox.right + ", top=" + hitBox.top
				+ ", bottom=" + hitBox.bottom);
	}

	/**
	 * Constructor called by parents class
	 * 
	 * @param _name
	 *            name of the entity
	 * @param _sprite
	 *            sprite of the entity
	 */
	public GameEntity(String _name, AnimatedSprite _anim) {
		id = lastId++;
		name = _name;
		sprite = _anim;
	}

	// ----------------------------------------------------
	// ---- Public methods
	// ----------------------------------------------------

	/**
	 * Abstract collide.
	 * 
	 * @param _gameEntity
	 *            the entity with which it collides
	 */
	public abstract void collide(GameEntity _gameEntity);

	/**
	 * When the entity dies.
	 */
	public abstract void die();

	/**
	 * Redefine the hitBox of the entity.
	 * 
	 * @param x
	 *            the offset x
	 * @param y
	 *            the offset y
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public void redefineHitBox(int x, int y, int width, int height) {
		offsets = new Rect(x, y, width, height);
	}

	public void update(long gameTime) {
		move();

		sprite.update(gameTime, direction);

		if (direction == DirectionConstants.LEFT) {
			hitBox = new Rect(sprite.getX() + sprite.getWidth() - offsets.left - offsets.right, sprite.getY()
					+ offsets.top, sprite.getX() + sprite.getWidth() - offsets.left, sprite.getY() + offsets.top
					+ offsets.bottom);
		} else {
			hitBox = new Rect(sprite.getX() + offsets.left, sprite.getY() + offsets.top, sprite.getX() + offsets.left
					+ offsets.right, sprite.getY() + offsets.top + offsets.bottom);
		}

		//Handle the changes of state
		if (changedState) {
			if (StateEnum.STATE_RECOVERY.equals(state)) {
				sprite.setRecovery(true);
			} else if (StateEnum.STATE_NORMAL.equals(state)) {
				sprite.setRecovery(false);
			}
			changedState = false;
		}
	}

	/**
	 * DRAW !
	 * 
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
		sprite.draw(canvas, direction);

		if (CollisionUtil.displayHitBoxes) {
			final Paint paint = new Paint();
			paint.setARGB(128, 255, 0, 0);

			if (this instanceof Personage && overlaping) {
				paint.setARGB(128, 100, 255, 100);
			}
			canvas.drawRect(hitBox, paint);
		}
	}

	// ----------------------------- Move functions begin
	public void setX(int _x) {
		sprite.setX(_x);
	}

	public void setY(int _y) {
		sprite.setY(_y);
	}

	/**
	 * Move the sprite according to its inertia : its current {@link #speedX}
	 * and {@link #speedY}, and also gravity if {@link #gravity()} is true.
	 */
	public void move() {
		addX(getRoundSpeedX());
		addY(getRoundSpeedY());
		speedY = Math.min(speedY + gravity, maxSpeedDown);
	}

	/**
	 * Whether the entity is on the floor or in the air.
	 * 
	 * @return true if on the floor
	 */
	public boolean onFloor() {
		return sprite.getY() == (MoveUtil.GROUND - sprite.getHeight());
	}

	/**
	 * Whether the entity is colliding the left border
	 * 
	 * @return true or false
	 */
	public boolean touchLeft() {
		return hitBox.left == 0;
	}

	/**
	 * Whether the entity is colliding the right border
	 * 
	 * @return true or false
	 */
	public boolean touchRight() {
		return hitBox.right == MoveUtil.SCREEN_WIDTH;
	}

	// ---------------------------- Move functions end

	// ----- Animations functions begin
	/**
	 * Add an animation.
	 * 
	 * @param name
	 *            the name of the animation
	 * @param frameList
	 *            the frame list. Ex: {0,1,2,3,2,1,0}
	 * @param _fps
	 *            the fps of the animation (fluent around 15 usually)
	 */
	public void addAnimation(String _name, int[] _frameList, int _fps) {
		sprite.addAnimation(_name, _frameList, _fps);
	}

	/**
	 * Launch an animation.
	 * 
	 * @param _name
	 *            the name of the animation
	 * @param _forceStop
	 *            true to force the previous animation to stop, false to wait
	 */
	public void play(String _name, boolean _repeat, boolean _forceStop) {
		sprite.play(_name, _repeat, _forceStop);
	}

	/**
	 * Stop the current animation;
	 */
	public void stop() {
		sprite.stop();
	}

	// ----- Animations functions end

	// --------------------------------------------
	// ---- private functions
	// --------------------------------------------
	private void addY(int _dy) {
		isOnFloor = false;
		isJumpingUp = false;
		isJumpingDown = false;

		int newY = sprite.getY() + _dy;
		if (affectedByCeiling && newY < 0) {
			newY = 0;
			speedY = 0;
		} else if (affectedByFloor && newY + sprite.getHeight() > MoveUtil.GROUND) {
			newY = MoveUtil.GROUND - sprite.getHeight();
			speedY = 0;
			isOnFloor = true;
		}
		// Now set the new X
		sprite.offset(0, newY - sprite.getY());

		if (!isOnFloor && _dy >= 0) {
			isJumpingDown = true;
		} else if (!isOnFloor && _dy < 0) {
			isJumpingUp = true;
		}
	}

	/**
	 * Adds _dx to the position of the sprite, checking if it goes out of
	 * screen. If it does reset speed to 0.
	 * 
	 * @param _dx
	 */
	private void addX(int _dx) {
		isMovingHorizontally = false;
		// calculate the actual translation
		int newX = hitBox.left + _dx;
		int actualDX = 0;
		if (affectedByWalls && newX < 0) {
			// if left > 0, set dx to go near the wall. if left < 0, the perso
			// is out of the screen, give a positive dx to return in the screen
			actualDX = -hitBox.left;
			speedX = 0;
		} else if (affectedByWalls && newX + hitBox.width() > MoveUtil.SCREEN_WIDTH) {
			actualDX = MoveUtil.SCREEN_WIDTH - hitBox.right;
			speedX = 0;
		} else {
			actualDX = _dx;
		}

		// Now set the new X
		sprite.offset(actualDX, 0);

		// if (cptTest++%40==0) // avoid to have 50 000 000 logs display
		// Log.d(TAG, "dx= " + _dx + ", ActualDX=" + actualDX + ", sprite= "
		// + sprite.getX() + ", newX= " +
		// newX+", hitbox(l,r)="+hitBox.left+"/"+hitBox.right);

		// Set the direction of the sprite
		if (speedX != 0 && _dx != 0) {
			isMovingHorizontally = true;

			if (!(this instanceof Personage)) {
				direction = (_dx > 0) ? DirectionConstants.RIGHT : DirectionConstants.LEFT;
			}
		}
	}

	// --------------------------------------------
	// ---- Getters and Setters
	// --------------------------------------------
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AnimatedSprite getSprite() {
		return sprite;
	}

	public void setSprite(AnimatedSprite sprite) {
		this.sprite = sprite;
	}

	/**
	 * @return the speedX
	 */
	public int getRoundSpeedX() {
		return (int) speedX;
	}

	public float getSpeedX() {
		return speedX;
	}

	/**
	 * @param _speedX
	 *            the speedX to set
	 */
	public void setSpeedX(float _speedX) {
		this.speedX = _speedX;
	}

	/**
	 * @return the speedY
	 */
	public int getRoundSpeedY() {
		return (int) speedY;
	}

	public float getSpeedY() {
		return speedY;
	}

	/**
	 * @param _speedY
	 *            the speedY to set
	 */
	public void setSpeedY(float _speedY) {
		this.speedY = _speedY;
	}

	/**
	 * @return the gravity
	 */
	public int getGravity() {
		return gravity;
	}

	/**
	 * @param _isSubjectToGravity
	 *            the gravity to set
	 */
	public void setGravity(int _gravity) {
		this.gravity = _gravity;
	}

	/**
	 * @return the hitBox
	 */
	public Rect getHitBox() {
		return hitBox;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	/**
	 * @return the state
	 */
	public StateEnum getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(StateEnum state) {
		changedState = true;
		this.state = state;
	}

}
