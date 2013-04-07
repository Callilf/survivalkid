package com.survivalkid.game.entity.personage;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.util.Log;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.Constants.PersonageConstants;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.core.enums.StateEnum;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.Life;
import com.survivalkid.game.entity.Life.EnumLife;
import com.survivalkid.game.entity.LifeChangeDisplayer;
import com.survivalkid.game.entity.StateObject;
import com.survivalkid.game.move.MovePersoManager;

public class Personage extends GameEntity {
	private static final String TAG = Personage.class.getSimpleName();

	private static final int DEFAULT_RECOVERYTIME = 700;

	private int playerNumber = -1;
	private int persoType;
	private Life life;
	private AnimatedSprite deathAnim;
	
	//Attributes for the corrida item
	private AnimatedSprite corridaAnim;

	// The bag that allows to store items
	private Bag bag;

	private boolean dying;

	// Damages when the player has been hit
	private List<LifeChangeDisplayer> damages;
	List<LifeChangeDisplayer> damagesOver;

	/** Manager of the move of the perso */
	private MovePersoManager movePersoManager;

	// Display the state alterations on the top right of the screen
	private StateDisplayer stateDisplayer;

	/**
	 * Create a personage
	 * 
	 * @param perso
	 *            the personage (use PersonageConstant.PERSO_YUGO or PERSO_YUNA)
	 * @param bitmap
	 *            the bitmap
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param nbColum
	 *            the number of columns in the spritesheet
	 * @param nbRows
	 *            the number of rows in the spritesheet
	 */
	public Personage(int perso, SpriteEnum spriteEnum, int x, int y) {
		super("CharacterA", spriteEnum, x, y);
		gravity = 2;
		persoType = perso;
		movePersoManager = new MovePersoManager();
		life = new Life(100);
		deathAnim = new AnimatedSprite(SpriteEnum.SMOKE_WHITE_LARGE, -100, -100);
		corridaAnim = new AnimatedSprite(SpriteEnum.YUGO_CORRIDA, -100, -100);
		dying = false;

		// damages
		damages = new ArrayList<LifeChangeDisplayer>();
		damagesOver = new ArrayList<LifeChangeDisplayer>();

		stateDisplayer = new StateDisplayer(this);

		setBag(new Bag(this));

		switch (perso) {
		case PersonageConstants.PERSO_YUGO:
			redefineHitBox((sprite.getWidth() * 30) / 100, (sprite.getHeight() * 10) / 100,
					(sprite.getWidth() * 40) / 100, (sprite.getHeight() * 84) / 100);
			break;
		case PersonageConstants.PERSO_YUNA:
			redefineHitBox((sprite.getWidth() * 35) / 100, (sprite.getHeight() * 10) / 100,
					(sprite.getWidth() * 40) / 100, (sprite.getHeight() * 84) / 100);
			break;
		}

		if (Constants.DEBUG) {
			Log.d(TAG, "Personage " + persoType + " created");
		}
	}

	/**
	 * @return the persoType
	 */
	public int getPersoType() {
		return persoType;
	}

	/**
	 * @param persoType
	 *            the persoType to set
	 */
	public void setPersoType(int persoType) {
		this.persoType = persoType;
	}

	@Override
	public void collide(GameEntity _gameEntity) {
		// TODO not used at the moment
	}

	/**
	 * 
	 * @param _damage
	 *            the amount of damages
	 * @param typeChange
	 *            TAKE_DAMAGE or TAKE_DAMAGE_PC (percent or not)
	 * @param recoveryMaxTime
	 *            duration of the recovery
	 * @return true if the perso has taken the damages, false otherwise
	 */
	public boolean takeDamage(int _damage, EnumLife _typeChange, int _recoveryMaxTime) {
		if (hasState(StateEnum.STATE_RECOVERY) || hasState(StateEnum.STATE_CORRIDA) 
				|| hasState(StateEnum.STATE_UNTOUCHABLE)) {
			return false;
		}

		life.modifyLife(_damage, _typeChange);
		addState(StateEnum.STATE_RECOVERY, _recoveryMaxTime);

		// Display the damages above the head of the character
		damages.add(new LifeChangeDisplayer(_damage, _typeChange, sprite.getX() + sprite.getWidth() / 2 - 20, sprite
				.getY()));
		return true;
	}

	/**
	 * When the character is hit
	 * 
	 * @param _damage
	 *            the amount of damages
	 * @param typeChange
	 *            TAKE_DAMAGE or TAKE_DAMAGE_PC (percent or not)
	 * @return true if the perso has taken the damages, false otherwise
	 */
	public boolean takeDamage(int _damage, EnumLife _typeChange) {
		return takeDamage(_damage, _typeChange, DEFAULT_RECOVERYTIME);
	}

	/**
	 * When the character take a bonus
	 * 
	 * @param _bonusLife
	 *            the amount of hp win
	 */
	public void recoveryLife(int _bonusLife, EnumLife typeChange) {
		life.modifyLife(_bonusLife, typeChange);
		damages.add(new LifeChangeDisplayer(_bonusLife, typeChange, sprite.getX() + sprite.getWidth() / 2 - 20, sprite
				.getY()));
	}

	@Override
	public void die() {
		if (Constants.DEBUG) {
			Log.d(TAG, "Perso DEAD !");
		}
		dying = true;

		// Suppress status of the player
		for (StateEnum state : states.keySet()) {
			processEndState(state);
		}
		states.clear();

		deathAnim.setX((sprite.getX() + sprite.getWidth() / 2) - deathAnim.getWidth() / 2);
		deathAnim.setY((sprite.getY() + sprite.getHeight() / 2) - deathAnim.getHeight() / 2);
		deathAnim.play("die", false, true);
	}

	@Override
	public void update(long gameDuration) {
		if (dying) {
			deathAnim.update(gameDuration, direction);
			if (deathAnim.isAnimationFinished()) {
				dead = true;
			}
			return;
		} else if (life.getCurrentLife() == 0) {
			die();
			return;
		}

		// If state corrida is active, the player is unvisible and immobile and
		// is replaced by the corrida animation.
		if (!hasState(StateEnum.STATE_KNOCK_BACK) && !hasState(StateEnum.STATE_CORRIDA)) {
			movePersoManager.calculNewSpeed(this);

			// Set the direction based on the button pushed
			if (movePersoManager.isLeftEnabled
					&& (!movePersoManager.isRightEnabled || movePersoManager.lastEnabledLeft)) {
				direction = DirectionConstants.LEFT;
			} else if (movePersoManager.isRightEnabled) {
				direction = DirectionConstants.RIGHT;
			}
		} else if (hasState(StateEnum.STATE_CORRIDA)) {
			setSpeedX(0);
			corridaAnim.setX(sprite.getX() + sprite.getWidth() / 2 - corridaAnim.getWidth() / 2);
			corridaAnim.setY(sprite.getY() + sprite.getHeight() - corridaAnim.getHeight());
			if (corridaAnim.isAnimationFinished()) {
				states.remove(StateEnum.STATE_CORRIDA);
				processEndState(StateEnum.STATE_CORRIDA);
			}
			corridaAnim.update(gameDuration, DirectionConstants.RIGHT);
		}

		super.update(gameDuration);

		if (!hasState(StateEnum.STATE_CORRIDA)) {
			// Play the correct animation
			selectAnimation();
		}

		// Handle the damage displayers
		for (LifeChangeDisplayer dd : damages) {
			dd.update(gameDuration);
			if (dd.isOver()) {
				damagesOver.add(dd);
			}
		}
		for (LifeChangeDisplayer dd : damagesOver) {
			damages.remove(dd);
		}

		stateDisplayer.update(gameDuration);
		bag.update(gameDuration);
	}

	/**
	 * Handle the animations of the character.
	 */
	private void selectAnimation() {
		if (hasState(StateEnum.STATE_KNOCK_BACK) && !isOnFloor) {
			play(PersonageConstants.ANIM_KNOCK_BACK, false, true);
			return;
		}

		if (isJumpingUp) {
			play(PersonageConstants.ANIM_JUMPUP, false, true);
		} else if (isJumpingDown) {
			play(PersonageConstants.ANIM_JUMPDOWN, false, true);
		} else if (movePersoManager.isLeftEnabled || movePersoManager.isRightEnabled) {
			play(PersonageConstants.ANIM_RUN, true, true);
		} else {
			play(PersonageConstants.ANIM_STAND, false, true);
		}
	}

	@Override
	protected void processStartState(StateObject stateObject) {
		switch (stateObject.getState()) {
		case STATE_LOW_SPEED:
			movePersoManager.changeSpeed(0.5f);
			break;
		default:
			super.processStartState(stateObject);
		}

	}

	@Override
	protected void processEndState(StateEnum stateEnum) {
		switch (stateEnum) {
		case STATE_LOW_SPEED:
			movePersoManager.reinitializeSpeed();
			break;
		default:
			super.processEndState(stateEnum);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (dying) {
			deathAnim.draw(canvas, direction);
		} else if (hasState(StateEnum.STATE_CORRIDA)) {
			corridaAnim.draw(canvas, DirectionConstants.RIGHT);
		} else {
			super.draw(canvas);
			// Handle the damage displayers
			for (LifeChangeDisplayer dd : damages) {
				dd.draw(canvas);
			}

			stateDisplayer.draw(canvas);
		}

		bag.draw(canvas);
	}

	public void setOverlaping(boolean b) {
		overlaping = b;
	}

	/**
	 * @return the playerNumber
	 */
	public int getPlayerNumber() {
		return playerNumber;
	}

	/**
	 * @param playerNumber
	 *            the playerNumber to set
	 */
	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	public MovePersoManager getMoveManager() {
		return movePersoManager;
	}

	public Life getLife() {
		return life;
	}

	/**
	 * @return the bag
	 */
	public Bag getBag() {
		return bag;
	}

	/**
	 * @param bag
	 *            the bag to set
	 */
	public void setBag(Bag bag) {
		this.bag = bag;
	}

	public void playCorridaWaitingAnim() {
		if (corridaAnim != null && hasState(StateEnum.STATE_CORRIDA)) {
			corridaAnim.play("prepare", true, false);
		}
	}

	public void playCorridaDodgingAnim() {
		if (corridaAnim != null && hasState(StateEnum.STATE_CORRIDA)) {
			corridaAnim.play("ole", false, true);
		}
	}
}
