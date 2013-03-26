package com.survivalkid.game.entity;

import static com.survivalkid.game.util.MoveUtil.BACKGROUND_BOTTOM;
import static com.survivalkid.game.util.MoveUtil.BACKGROUND_LEFT;
import static com.survivalkid.game.util.MoveUtil.BACKGROUND_RIGHT;
import static com.survivalkid.game.util.MoveUtil.BACKGROUND_TOP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.core.enums.StateEnum;
import com.survivalkid.game.entity.enemy.impl.Bull;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.singleton.SharedVars;
import com.survivalkid.game.util.CollisionUtil;
import com.survivalkid.game.util.DesignUtil;
import com.survivalkid.game.util.MoveUtil;
import com.survivalkid.game.util.TimerUtil;

public abstract class GameEntity implements Cloneable {

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
	protected float gravity = 0;
	protected int maxSpeedDown = 20;

	/** States. */
	protected Map<StateEnum,StateObject> states;
	protected boolean dead;

	// for test
	// private int cptTest = 0;
	protected boolean overlaping = false;

	// ----------------------------------------------------
	// ---- Constructor
	// ----------------------------------------------------

	/**
	 * Constructor called by parents class
	 * 
	 * @param _name
	 *            name of the entity
	 * @param SpriteEnum
	 *            the sprite of the entity
	 * @param x
	 *            position x initial
	 * @param y
	 *            position y initial
	 */
	public GameEntity(String _name, SpriteEnum spriteEnum, int x, int y) {
		id = lastId++;
		name = _name;
		sprite = new AnimatedSprite(spriteEnum, x, y);
		offsets = new Rect(0, 0, sprite.getWidth(), sprite.getHeight());
		hitBox = new Rect(sprite.getX() + offsets.left, sprite.getY() + offsets.top, sprite.getX() + offsets.left
				+ offsets.right, sprite.getY() + offsets.top + offsets.bottom);
		direction = DirectionConstants.RIGHT;
		states = new HashMap<StateEnum,StateObject>();

		isMovingHorizontally = false;
		isJumpingUp = false;
		isJumpingDown = false;
		isOnFloor = false;

		affectedByFloor = true;
		affectedByWalls = true;
		affectedByCeiling = true;

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

	public GameEntity() {
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
		updateHitBox();
	}

	protected void updateHitBox() {
		if (direction == DirectionConstants.LEFT) {
			hitBox = new Rect(sprite.getX() + sprite.getWidth() - offsets.left - offsets.right, sprite.getY()
					+ offsets.top, sprite.getX() + sprite.getWidth() - offsets.left, sprite.getY() + offsets.top
					+ offsets.bottom);
		} else {
			hitBox = new Rect(sprite.getX() + offsets.left, sprite.getY() + offsets.top, sprite.getX() + offsets.left
					+ offsets.right, sprite.getY() + offsets.top + offsets.bottom);
		}
	}

	public void update(long gameDuration) {
		move();

		sprite.update(gameDuration, direction);
		updateHitBox();

		// update states
		if (!states.isEmpty()) {
			List<StateEnum> stateToRemove = new ArrayList<StateEnum>();
			for (Entry<StateEnum, StateObject> state : states.entrySet()) {
				long expiration = state.getValue().getExpiration();
				if (expiration < GameContext.getSingleton().gameDuration)
				{
					stateToRemove.add(state.getKey());
				}
			}
			for (StateEnum stateEnum : stateToRemove) {
				processEndState(stateEnum);
				states.remove(stateEnum);
			}
		}

	}
	
	/**
	 * Process the action to do when a state is gone.
	 * 
	 * @param stateEnum the state
	 */
	protected void processEndState(StateEnum stateEnum) {
		switch(stateEnum) {
		case STATE_RECOVERY: 
			sprite.setRecovery(false);break;
		case STATE_CORRIDA : 
			SharedVars.getSingleton().setPersoDrawnInBackground(true);
			addState(StateEnum.STATE_RECOVERY, 1000);
		case STATE_HIGH_SPEED_ENEMIES:
		case STATE_LOW_SPEED_ENEMIES:
			GameContext.getSingleton().setAlterationSpeedEnemy(1f);
			break;
		default:
			break;
		}
	}

	/**
	 * Process the action to do when a state is gone.
	 * @param stateObject All information of the state
	 */
	protected void processStartState(StateObject stateObject) {
		switch(stateObject.getState()) {
		case STATE_RECOVERY: 
			sprite.setRecovery(true);break;
		case STATE_CORRIDA :
			SharedVars.getSingleton().setPersoDrawnInBackground(true);
			Bull bull = new Bull();
			bull.initBullForCorrida();
			SharedVars.getSingleton().getEnemyManager().addEnemyAsync(bull);
			break;
		case STATE_LOW_SPEED_ENEMIES:
			states.remove(StateEnum.STATE_HIGH_SPEED_ENEMIES);
			GameContext.getSingleton().setAlterationSpeedEnemy(0.51f);
			break;
		case STATE_HIGH_SPEED_ENEMIES:
			states.remove(StateEnum.STATE_LOW_SPEED_ENEMIES);
			GameContext.getSingleton().setAlterationSpeedEnemy(2f);
			break;
		default:
			break;
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
			if (this instanceof Personage && overlaping) {
				canvas.drawRect(hitBox, DesignUtil.PAINT_HITBOX_OVERLAPPING);
			} else {
				canvas.drawRect(hitBox, DesignUtil.PAINT_HITBOX_ENTITY);
			}
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
		// return sprite.getY() == (MoveUtil.GROUND - sprite.getHeight());
		return hitBox.bottom == MoveUtil.GROUND;
	}

	public boolean onScreen() {
		return (sprite.getX() + sprite.getWidth() >= BACKGROUND_LEFT) && (sprite.getY() + sprite.getHeight() >= BACKGROUND_TOP)
				&& sprite.getX() < BACKGROUND_RIGHT && sprite.getY() < BACKGROUND_BOTTOM;
	}

	/**
	 * Whether the entity is colliding the left border
	 * 
	 * @return true or false
	 */
	public boolean touchLeft() {
		return hitBox.left == MoveUtil.BACKGROUND_LEFT;
	}

	/**
	 * Whether the entity is colliding the right border
	 * 
	 * @return true or false
	 */
	public boolean touchRight() {
		return hitBox.right == MoveUtil.BACKGROUND_RIGHT;
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
		_dy = MoveUtil.normY(_dy);

		isOnFloor = false;
		isJumpingUp = false;
		isJumpingDown = false;

		int newY = hitBox.top + _dy;
		int actualDY = _dy;
		if (affectedByCeiling && newY < MoveUtil.BACKGROUND_TOP) {
			actualDY = MoveUtil.BACKGROUND_TOP - hitBox.top;
			speedY = 0;
		} else if (affectedByFloor && hitBox.bottom + _dy > MoveUtil.GROUND) {
			actualDY = MoveUtil.GROUND - hitBox.bottom;
			speedY = 0;
			isOnFloor = true;
		}
		// Now set the new Y
		sprite.offset(0, actualDY);

		if (!isOnFloor && actualDY >= 0) {
			isJumpingDown = true;
		} else if (!isOnFloor && actualDY < 0) {
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
		_dx = MoveUtil.normX(_dx);

		isMovingHorizontally = false;
		// calculate the actual translation
		int newX = hitBox.left + _dx;
		int actualDX = 0;
		if (affectedByWalls && newX < BACKGROUND_LEFT) {
			// if left > 0, set dx to go near the wall. if left < 0, the perso
			// is out of the screen, give a positive dx to return in the screen
			actualDX = BACKGROUND_LEFT - hitBox.left;
			speedX = 0;
		} else if (affectedByWalls && newX + hitBox.width() > BACKGROUND_RIGHT) {
			actualDX = BACKGROUND_RIGHT - hitBox.right;
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

	// for timer

	public void updateTimed(long gameDuration) {
		if (!TimerUtil.TIMER_ACTIVE) {
			update(gameDuration);
			return;
		}
		String object = "u-" + name;
		TimerUtil.start(object);
		update(gameDuration);
		TimerUtil.end(object);
	}

	public void drawTimed(Canvas canvas) {
		if (!TimerUtil.TIMER_ACTIVE) {
			draw(canvas);
			return;
		}
		String object = "d-" + name;
		TimerUtil.start(object);
		draw(canvas);
		TimerUtil.end(object);
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
	public float getGravity() {
		return gravity;
	}

	/**
	 * @param _isSubjectToGravity
	 *            the gravity to set
	 */
	public void setGravity(float _gravity) {
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
	
	public void addState(StateEnum state, long duration) {
		long expiration = duration + GameContext.getSingleton().gameDuration;
		StateObject newState = new StateObject(state, expiration);
		processStartState(newState);
		states.put(state, newState);
	}
	
	public boolean hasState(StateEnum state) {
		return states.containsKey(state);
	}

	public Map<StateEnum, StateObject> getStates() {
		return states;
	}

	/**
	 * @return the direction
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	
}
