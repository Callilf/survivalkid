package com.survivalkid.game.entity.personage;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.util.Log;

import com.survivalkid.R;
import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.Constants.PersonageConstants;
import com.survivalkid.game.core.enums.StateEnum;
import com.survivalkid.game.entity.Life.EnumLife;
import com.survivalkid.game.entity.LifeChangeDisplayer;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.Life;
import com.survivalkid.game.move.MovePersoManager;
import com.survivalkid.game.util.BitmapUtil;

public class Personage extends GameEntity {
	private static final String TAG = Personage.class.getSimpleName();

	private int playerNumber = -1;
	private int persoType;
	private Life life;
	private AnimatedSprite deathAnim;

	private boolean dying;
	
	//State recovery
	private int recoveryMaxTime = 700;
	private long recoveryBeginTime;
	
	//Damages when the player has been hit
	private List<LifeChangeDisplayer> damages;
	List<LifeChangeDisplayer> damagesOver;

	/** Manager of the move of the perso */
	private MovePersoManager movePersoManager;

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
	public Personage(int perso, int spriteSheetId, int x, int y, int nbColum, int nbRows) {
		super("CharacterA", BitmapUtil.createBitmap(spriteSheetId), x, y, nbColum, nbRows);
		gravity = 2;
		persoType = perso;
		movePersoManager = new MovePersoManager();
		life = new Life(100);
		deathAnim = new AnimatedSprite(BitmapUtil.createBitmap(R.drawable.dead_anim), 0, 0, 7, 1);
		dying = false;
		
		//state attributes
		recoveryBeginTime = -1;
		
		//damages
		damages = new ArrayList<LifeChangeDisplayer>();
		damagesOver = new ArrayList<LifeChangeDisplayer>();
		
		switch (perso) {
		case PersonageConstants.PERSO_YUGO:
			addAnimation(PersonageConstants.ANIM_STAND, new int[] { 0 }, 20);
			addAnimation(PersonageConstants.ANIM_RUN, new int[] { 1, 2, 3, 5, 6, 7, 9, 10, 11, 12, 13, 14 }, 20);
			addAnimation(PersonageConstants.ANIM_JUMPUP, new int[] { 19, 18, 17 }, 13);
			addAnimation(PersonageConstants.ANIM_JUMPDOWN, new int[] { 19 }, 13);
			redefineHitBox((sprite.getWidth() * 30) / 100, (sprite.getHeight() * 10) / 100,
					(sprite.getWidth() * 40) / 100, (sprite.getHeight() * 84) / 100);
			break;
		case PersonageConstants.PERSO_YUNA:
			addAnimation(PersonageConstants.ANIM_STAND, new int[] { 0 }, 20);
			addAnimation(PersonageConstants.ANIM_RUN, new int[] { 1, 2, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14 }, 20);
			addAnimation(PersonageConstants.ANIM_JUMPUP, new int[] { 20, 21, 22 }, 13);
			addAnimation(PersonageConstants.ANIM_JUMPDOWN, new int[] { 17, 18, 19 }, 13);
			redefineHitBox((sprite.getWidth() * 35) / 100, (sprite.getHeight() * 10) / 100,
					(sprite.getWidth() * 40) / 100, (sprite.getHeight() * 84) / 100);
			break;
		}

		deathAnim.addAnimation("die", new int[] { 0, 1, 2, 3, 4, 5, 6 }, 17);

	}

	@Override
	public void collide(GameEntity _gameEntity) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * When the character is hit
	 * @param _damage the amount of damages
	 */
	public void takeDamage(int _damage, EnumLife typeChange) {
		if (StateEnum.STATE_RECOVERY.equals(state)) {
			return;
		}

		life.modifyLife(_damage, typeChange);
		setState(StateEnum.STATE_RECOVERY);
		//Display the damages above the head of the character
		damages.add(new LifeChangeDisplayer(_damage, typeChange,
				System.currentTimeMillis(), sprite.getX() + sprite.getWidth()/2 - 20, sprite.getY()));
	}
	
	/**
	 * When the character take a bonus
	 * 
	 * @param _bonusLife the amount of hp win
	 */
	public void revoceryLife(int _bonusLife, EnumLife typeChange) {
		life.modifyLife(_bonusLife, typeChange);
		damages.add(new LifeChangeDisplayer(_bonusLife, typeChange,
				System.currentTimeMillis(), sprite.getX() + sprite.getWidth()/2 - 20, sprite.getY()));
	}

	@Override
	public void die() {
		Log.d(TAG, "Perso DEAD !");
		dying = true;

		deathAnim.setX((sprite.getX() + sprite.getWidth() / 2) - deathAnim.getWidth() / 2);
		deathAnim.setY((sprite.getY() + sprite.getHeight() / 2) - deathAnim.getHeight() / 2);
		deathAnim.play("die", false, true);
	}

	@Override
	public void update(long gameTime) {
		if (dying) {
			deathAnim.update(gameTime, direction);
			if (deathAnim.isAnimationFinished()) {
				dead = true;
			}
			return;
		} else if (life.getCurrentLife() == 0) {
			die();
			return;
		}

		movePersoManager.calculNewSpeed(this);

		// Set the direction based on the button pushed
		if (movePersoManager.isLeftEnabled && 
				(!movePersoManager.isRightEnabled || movePersoManager.lastEnabledLeft)) {
			direction = DirectionConstants.LEFT;
		} else if (movePersoManager.isRightEnabled) {
			direction = DirectionConstants.RIGHT;
		}
		
		//Handle the update of different states
		if (StateEnum.STATE_RECOVERY.equals(state)) {
			if(recoveryBeginTime == -1) {
				recoveryBeginTime = gameTime;
			}
			
			if(gameTime - recoveryBeginTime >= recoveryMaxTime) {
				setState(StateEnum.STATE_NORMAL);
				recoveryBeginTime = -1;
			}
		}
		
		
		

		super.update(gameTime);

		// Play the correct animation
		if (isJumpingUp) {
			play(PersonageConstants.ANIM_JUMPUP, false, true);
		} else if (isJumpingDown) {
			play(PersonageConstants.ANIM_JUMPDOWN, false, true);
		} else if (movePersoManager.isLeftEnabled || movePersoManager.isRightEnabled) {
			play(PersonageConstants.ANIM_RUN, true, true);
		} else {
			play(PersonageConstants.ANIM_STAND, false, true);
		}
		
		//Handle the damage displayers
		for(LifeChangeDisplayer dd : damages) {
			dd.update(gameTime);
			if(dd.isOver()) {
				damagesOver.add(dd);
			}
		}
		for(LifeChangeDisplayer dd : damagesOver) {
			damages.remove(dd);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (dying) {
			deathAnim.draw(canvas, direction);
		} else {
			super.draw(canvas);
			//Handle the damage displayers
			for(LifeChangeDisplayer dd : damages) {
				dd.draw(canvas);
			}
		}
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

}
