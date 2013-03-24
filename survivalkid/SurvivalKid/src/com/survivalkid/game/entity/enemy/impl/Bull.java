package com.survivalkid.game.entity.enemy.impl;

import android.graphics.Canvas;
import android.graphics.Point;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.core.enums.StateEnum;
import com.survivalkid.game.entity.Life.EnumLife;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.particle.ParticleEmitter;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.singleton.SharedVars;
import com.survivalkid.game.util.MoveUtil;

public class Bull extends EnemyEntity {

	// avoid to get hit 2 time by the same bull
	private static final int RECOVERY_TIME = 900;

	private static final int VITESSE_BULL = 15;
	private static final int SPEED_COLLISION_X = 20;
	private static final int SPEED_COLLISION_Y = -30;

	private static final int WARNING_DURATION = 2000;

	private AnimatedSprite warning;
	private boolean inWarning;
	private long warningExpiration;
	private AnimatedSprite deathAnim;
	private ParticleEmitter emitter;

	/** Default constructor. */
	public Bull() {
		super("Bull", SpriteEnum.BULL, 0, 0, 30, 0);
		attack = DEFAULT_DEFENSE + 2;
		defense = DEFAULT_DEFENSE + 1;
	};

	/** Initialize the enemy. */
	private void init() {
		gravity = 2;
		affectedByWalls = false;

		warning = new AnimatedSprite(SpriteEnum.BULL_WARNING, 0, 0);
		warning.setBlinking(true);
		warning.setBlinkPeriod(5, 5);
		warning.play("run", true, true);

		inWarning = true;
		active = false;
		warningExpiration = GameContext.getSingleton().gameDuration + WARNING_DURATION;
		if (direction == DirectionConstants.LEFT) {
			warning.setX(MoveUtil.BACKGROUND_RIGHT - warning.getWidth());
		} else {
			warning.setX(MoveUtil.BACKGROUND_LEFT);
		}
		warning.setY((int) (MoveUtil.GROUND * 0.75f));

		redefineHitBox((sprite.getWidth() * 25) / 100, (sprite.getHeight() * 20) / 100, (sprite.getWidth() * 70) / 100,
				(sprite.getHeight() * 80) / 100);
		play("run", true, true);
		deathAnim = new AnimatedSprite(SpriteEnum.SMOKE_WHITE_LARGE, 0, 0);

		createEmitter();
	}
	
	private void createEmitter() {
		emitter = new ParticleEmitter(SpriteEnum.PARTICLE_DUST_BROWN, 50);
		if (direction == DirectionConstants.RIGHT) {
			emitter.setParticleSpeedXMin(0);
			emitter.setParticleSpeedXMax(2);
		} else {
			emitter.setParticleSpeedXMin(-2);
			emitter.setParticleSpeedXMax(0);
		}
		emitter.setParticleSpeedYMin(-1);
		emitter.setParticleSpeedYMax(-1);
		emitter.setSpeedChange(0, 75, 0, 50);
		emitter.setParticleFadeOut(true);
		emitter.setParticleTimeOut(1000);
		emitter.setGenerate(false);
		emitter.setNumberOfParticlePerGeneration(1);
		emitter.setParticleAlpha(150);
		SharedVars.getSingleton().getParticleManager().addEmitter(emitter);
	}

	@Override
	public void update(long gameDuration) {
		if (dying) {
			deathAnim.update(gameDuration, DirectionConstants.RIGHT);
			if (deathAnim.isAnimationFinished()) {
				dead = true;
				emitter.setStopping(true);
			}
			return;
		}

		if (sprite.getX() + sprite.getWidth() < MoveUtil.BACKGROUND_LEFT || sprite.getX() > MoveUtil.BACKGROUND_RIGHT) {
			dead = true;
			emitter.setStopping(true);
		}

		if (inWarning) {
			if (warningExpiration < gameDuration) {
				inWarning = false;
				active = true;
				initSpeed();
				emitter.setGenerate(true);
			} else {
				if (warningExpiration - WARNING_DURATION / 4 < gameDuration) {
					warning.setBlinkPeriod(2, 2);
				}
				warning.update(gameDuration, direction);

				if (direction == DirectionConstants.LEFT) {
					SharedVars.getSingleton().getBullWarningRightList().add(warning);
				} else {
					SharedVars.getSingleton().getBullWarningLeftList().add(warning);
				}
			}
		}

		super.update(gameDuration);

		if (direction == DirectionConstants.LEFT) {
			emitter.setParticleDirection(DirectionConstants.RIGHT);
		} else {
			emitter.setParticleDirection(DirectionConstants.LEFT);
		}
		emitter.setX(sprite.getX() + sprite.getWidth()/2);
		emitter.setY(sprite.getY() + sprite.getHeight() - emitter.getParticleSprite().getHeight()/2);
	}

	@Override
	public void applyCollisionCharacter(Personage _personage) {
		if (_personage.takeDamage(dammage, EnumLife.TAKE_DAMAGE, RECOVERY_TIME)) {
			int newSpeedX = (direction == DirectionConstants.LEFT) ? -SPEED_COLLISION_X : SPEED_COLLISION_X;
			_personage.setDirection((direction == DirectionConstants.LEFT) ? DirectionConstants.RIGHT
					: DirectionConstants.LEFT);
			_personage.setSpeedX(newSpeedX);
			_personage.setSpeedY(SPEED_COLLISION_Y);
			_personage.addState(StateEnum.STATE_KNOCK_BACK, 500);
		}
	}

	@Override
	public void die() {
		dying = true;

		deathAnim.setX((sprite.getX() + sprite.getWidth() / 2) - deathAnim.getWidth() / 2);
		deathAnim.setY((sprite.getY() + sprite.getHeight() / 2) - deathAnim.getHeight() / 2);
		deathAnim.play("die", false, true);
	}

	@Override
	public void draw(Canvas canvas) {
		if (dying) {
			deathAnim.draw(canvas, DirectionConstants.RIGHT);
		} else {
			if (inWarning) {
				warning.draw(canvas, direction);
			} else {
				super.draw(canvas);
			}
		}
	}

	/**
	 * Initialize the position, speed and misc. attributes of the enemy.
	 */
	@Override
	public void initRandomPositionAndSpeed(Point playerPosition) {
		// random spawn position
		int random = (int) (Math.random() * 100);
		if (random < 50) {
			sprite.setX(MoveUtil.BACKGROUND_LEFT - sprite.getWidth());
			sprite.setY(MoveUtil.GROUND - sprite.getHeight());
			direction = DirectionConstants.RIGHT;
		} else {
			sprite.setX(MoveUtil.BACKGROUND_RIGHT);
			sprite.setY(MoveUtil.GROUND - sprite.getHeight());
			direction = DirectionConstants.LEFT;
		}

		init();
	}

	private void initSpeed() {
		// init speed
		setSpeedY(0);
		if (direction == DirectionConstants.LEFT) {
			setSpeedX(-VITESSE_BULL);
		} else {
			setSpeedX(VITESSE_BULL);
		}
	}

}
