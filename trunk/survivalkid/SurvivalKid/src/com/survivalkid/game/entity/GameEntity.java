package com.survivalkid.game.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.util.MoveUtil;

public abstract class GameEntity {
	/** Last affected id. */
	public static int lastId;

	/** The id of the gameEntity. */
	private int id;
	/** The name of the gameEntity. */
	private String name;
	/** The sprite (will be an animation in the future). */
	protected AnimatedSprite sprite;
	
	/** The hit box. */
	private Rect offsets;
	private Rect hitBox;

	// Speed attributes
	private int speedX;
	private int speedY;
	private boolean isSubjectToGravity;
	
	//----------------------------------------------------
	// ---- Constructor
	//----------------------------------------------------

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
		offsets = new Rect(0,0,sprite.getWidth(),sprite.getHeight());
		hitBox = new Rect(sprite.getX() + offsets.left, sprite.getY() + offsets.top, sprite.getX() + offsets.left + offsets.right,  sprite.getY() + offsets.top + offsets.bottom);
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
	
	
	//----------------------------------------------------
	// ---- Public methods
	//----------------------------------------------------
	
	/**
	 * Abstract collide.
	 * @param _gameEntity the entity with which it collides
	 */
	public abstract void collide(GameEntity _gameEntity);

	/**
	 * When the entity dies.
	 */
	public abstract void die();
	
	/**
	 * Redefine the hitBox of the entity.
	 * @param x the offset x
	 * @param y the offset y
	 * @param width the width
	 * @param height the height
	 */
	public void redefineHitBox(int x, int y, int width, int height) {
		offsets = new Rect(x, y, width, height);
	}
	
	
	public void update(long gameTime) {
		move();
		sprite.update(gameTime);
		hitBox = new Rect(sprite.getX() + offsets.left, sprite.getY() + offsets.top, sprite.getX() + offsets.left + offsets.right,  sprite.getY() + offsets.top + offsets.bottom);
	}
	
	public void draw(Canvas canvas, boolean displayHitBox) {
		sprite.draw(canvas);
		
		if(displayHitBox) {
			final Paint paint = new Paint();
			paint.setARGB(128, 255, 0, 0);
			canvas.drawRect(hitBox, paint);
		}
	}
	
	
	//----------------------------- Move functions begin
	public void setX(int _x) {
		sprite.setX(_x);
	}
	public void setY(int _y) {
		sprite.setY(_y);
	}
	
	/**
	 * Move the sprite according to its inertia : its current {@link #speedX}
	 * and {@link #speedY}, and also gravity if {@link #isSubjectToGravity()} is
	 * true.
	 */
	public void move() {
		addX(speedX);
		addY(speedY);
		if (isSubjectToGravity) {
			speedY = speedY+MoveUtil.GRAVITY;
		}
	}
	
	/**
	 * Whether the entity is on the floor or in the air.
	 * @return true if on the floor
	 */
	public boolean onFloor() {
		return sprite.getY() == (MoveUtil.MAX_Y - sprite.getHeight());
	}
	//---------------------------- Move functions end
	
	
	//----- Animations functions begin
	/**
	 * Add an animation.
	 * @param name the name of the animation
	 * @param frameList the frame list. Ex: {0,1,2,3,2,1,0}
	 * @param _fps the fps of the animation (fluent around 15 usually)
	 */
	public void addAnimation(String _name, int[] _frameList, int _fps) {
		sprite.addAnimation(_name, _frameList, _fps);
	}
	
	/**
	 * Launch an animation.
	 * @param _name the name of the animation
	 * @param _forceStop true to force the previous animation to stop, false to wait
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
	//----- Animations functions end
	
	//--------------------------------------------
	// ---- private functions
	//--------------------------------------------
	private void addY(int _dy) {
		int newY = sprite.getY() + _dy;
		if (newY < 0) {
			newY = 0;
			speedY = 0;
		} else if (newY + sprite.getHeight() > MoveUtil.MAX_Y) {
			newY = MoveUtil.MAX_Y - sprite.getHeight();
			speedY = 0;
		}
		// Now set the new X
		sprite.offset(0, newY - sprite.getY());
	}

	/**
	 * Adds _dx to the position of the sprite, checking if it goes out of
	 * screen. If it does reset speed to 0.
	 * 
	 * @param _dx
	 */
	private void addX(int _dx) {
		int newX = sprite.getX() + _dx;
		if (newX < 0) {
			newX = 0;
			speedX = 0;
		} else if (newX + sprite.getWidth() > MoveUtil.MAX_X) {
			newX = MoveUtil.MAX_X - sprite.getWidth();
			speedX = 0;
		}
		// Now set the new X
		sprite.offset(newX - sprite.getX(), 0);
	}
	
	
	//--------------------------------------------
	// ---- Getters and Setters
	//--------------------------------------------
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
	public int getSpeedX() {
		return speedX;
	}

	/**
	 * @param _speedX
	 *            the speedX to set
	 */
	public void setSpeedX(int _speedX) {
		this.speedX = _speedX;
	}

	/**
	 * @return the speedY
	 */
	public int getSpeedY() {
		return speedY;
	}

	/**
	 * @param _speedY
	 *            the speedY to set
	 */
	public void setSpeedY(int _speedY) {
		this.speedY = _speedY;
	}

	/**
	 * @return the isSubjectToGravity
	 */
	public boolean isSubjectToGravity() {
		return isSubjectToGravity;
	}

	/**
	 * @param _isSubjectToGravity
	 *            the isSubjectToGravity to set
	 */
	public void setSubjectToGravity(boolean _isSubjectToGravity) {
		this.isSubjectToGravity = _isSubjectToGravity;
	}

}
