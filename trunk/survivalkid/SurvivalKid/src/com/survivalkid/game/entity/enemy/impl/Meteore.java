package com.survivalkid.game.entity.enemy.impl;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Point;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.Life.EnumLife;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.particle.ParticleEmitter;
import com.survivalkid.game.singleton.SharedVars;
import com.survivalkid.game.util.MoveUtil;

public class Meteore extends EnemyEntity {

	protected int fallInit = 1;
	protected int fallNext = 8;
	protected int shiftX = 1;
	
	protected AnimatedSprite deathAnim;
	private AnimatedSprite fireAnim;
	
	private int directionFire;
	protected ParticleEmitter emitter;
	
	/** duration of the recovery when the player is hit */
	private static final int RECOVERY_TIME = 500;
	
	public Meteore() {
		super("Meteor", SpriteEnum.METEORE, 0, 0, 10, 1);
		description = "Falls on the ground quite frequently but not very powerful.";
	}
	
	/**
	 * 
	 * Constructor use for the fireMeteor
	 * @param name name
	 * @param sprite the sprite
	 * @param x position x
	 * @param y position y
	 * @param damage the damages
	 * @param difficulty the difficulty
	 */
	protected Meteore(String _name, SpriteEnum _sprite, int _x, int _y, int _damage, int _difficulty) {
		super(_name, _sprite, _x, _y, _damage, _difficulty);
	}
	
	private void init() {
		affectedByWalls = false;
		affectedByCeiling = false;
		affectedByFloor = false;

		// fire
		fireAnim = new AnimatedSprite(SpriteEnum.FIRE_TRAIL, 0, 0);
		
		// death
		initDeathAnim();

		redefineHitBox((sprite.getWidth() * 18) / 100,
				(sprite.getHeight() * 18) / 100,
				(sprite.getWidth() * 74) / 100, (sprite.getHeight() * 74) / 100);
		
		createEmitter();
		
		setPositionFire();
		fireAnim.play("fire", true, true);

	}
	
	private void createEmitter() {
		emitter = new ParticleEmitter(SpriteEnum.PARTICLE_SMOKE_WHITE_ROUND, 50);
		emitter.setParticleSpeedXMin(0);
		emitter.setParticleSpeedXMax(0);
		emitter.setParticleSpeedYMin(0);
		emitter.setParticleSpeedYMax(0);
		emitter.setSpeedChange(0, 1, 0, 1);
		emitter.setParticleFadeOut(true);
		emitter.setParticleTimeOut(250);
		emitter.setGenerate(true);
		emitter.setNumberOfParticlePerGeneration(1);
		emitter.setParticleAlpha(120);
		SharedVars.getSingleton().getParticleManager().addEmitter(emitter);
	}
	
	protected void initDeathAnim() {
		deathAnim = new AnimatedSprite(SpriteEnum.METEORE_EXPLOSION, 0, 0);
	}
	
	@Override
	public void initRandomPositionAndSpeed(Point playerPosition) {
		int random = MoveUtil.getRandomPositionX();
		sprite.setX(random);
		sprite.setY(MoveUtil.BACKGROUND_TOP + 1 - sprite.getHeight());
		if (random <= MoveUtil.BACKGROUND_WIDTH/2 + MoveUtil.BACKGROUND_LEFT) {
			direction = DirectionConstants.RIGHT;
			directionFire = DirectionConstants.LEFT;
		}
		else {
			direction = DirectionConstants.LEFT;
			directionFire = DirectionConstants.RIGHT;
		}

		// init speed
		setSpeedY(fallInit);
		if (direction == DirectionConstants.LEFT) {
			setSpeedX(-shiftX/(float)fallNext);
		} else {
			setSpeedX(shiftX/(float)fallNext);
		}
		
		init();
	}

	@Override
	public void applyCollisionCharacter(Personage _personage) {
		if (_personage.takeDamage(dammage, EnumLife.TAKE_DAMAGE, RECOVERY_TIME)) {
			die();
		}
	}

	@Override
	public void die() {
		dying = true;

		deathAnim.setX((sprite.getX() + sprite.getWidth() / 2)
				- deathAnim.getWidth() / 2);
		deathAnim.setY(sprite.getY() - deathAnim.getHeight()/2);
		deathAnim.play("explode", false, true);
	}
	
	private void setPositionFire() {
		int decalX = (direction == DirectionConstants.LEFT)? 
				sprite.getX() + sprite.getWidth()* 2/3 : sprite.getX() + sprite.getWidth() / 3;
		
		fireAnim.setX(decalX - fireAnim.getWidth() / 2);
		fireAnim.setY(sprite.getY() - fireAnim.getHeight() * 3/4);
		
		Random rand = new Random();
		emitter.setX(fireAnim.getX() + rand.nextInt(fireAnim.getWidth()));
		emitter.setY(fireAnim.getY() + fireAnim.getHeight()/2);
	}
	
	@Override
	public void update(long gameDuration) {
		if (dying) {
			deathAnim.update(gameDuration, DirectionConstants.RIGHT);
			if (deathAnim.isAnimationFinished()) {
				dead = true;
				terminate();
			}

			return;
		}

		if (sprite.getX() + sprite.getWidth() < MoveUtil.BACKGROUND_LEFT || sprite.getX() > MoveUtil.BACKGROUND_RIGHT) {
			dead = true;
		}
		else if (sprite.getY() + sprite.getHeight()*3/4 >= MoveUtil.GROUND) {
			die();
		}
		else if (sprite.getY() + sprite.getHeight()*2/3 > 0) {
			if (direction == DirectionConstants.LEFT) {
				setSpeedX(-shiftX);
			} else {
				setSpeedX(shiftX);
			}
			setSpeedY(fallNext);		
		}
		
		super.update(gameDuration);
		// update of the fire after the meteor is moved
		setPositionFire();
		fireAnim.update(gameDuration, directionFire);
	}
	
	protected void terminate() {
		// nothing to do : use by the fire meteor to pop the ground fire
		emitter.setStopping(true);
	}

	@Override
	public void draw(Canvas canvas) {
		if (dying) {
			deathAnim.draw(canvas, DirectionConstants.RIGHT);
		} else {
			fireAnim.draw(canvas, directionFire);
			super.draw(canvas);
		}
	}

}
