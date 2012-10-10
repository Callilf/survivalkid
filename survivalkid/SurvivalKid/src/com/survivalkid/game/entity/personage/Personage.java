package com.survivalkid.game.entity.personage;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.Constants.PersonageConstants;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.move.MovePersoManager;
import com.survivalkid.game.util.MoveUtil;

public class Personage extends GameEntity {

	private int playerNumber = -1;
	private int persoType;
	
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
	public Personage(int perso, Bitmap bitmap, int x, int y, int nbColum,
			int nbRows) {
		super("CharacterA", bitmap, x, y, nbColum, nbRows);
		gravity = 2;
		persoType = perso;
		movePersoManager = new MovePersoManager();

		switch (perso) {
		case PersonageConstants.PERSO_YUGO:
			addAnimation(PersonageConstants.ANIM_STAND, new int[]{0}, 20);
			addAnimation(PersonageConstants.ANIM_RUN, new int[]{1,2,3,5,6,7,9,10,11,12,13,14}, 20);
			addAnimation(PersonageConstants.ANIM_JUMPUP, new int[]{19,18,17}, 13);
			addAnimation(PersonageConstants.ANIM_JUMPDOWN, new int[]{19}, 13);
			redefineHitBox( (sprite.getWidth()*45) / 100, (sprite.getHeight()*10) / 100, (sprite.getWidth()*40) / 100, (sprite.getHeight()*84) / 100);
			break;
		case PersonageConstants.PERSO_YUNA:
			addAnimation(PersonageConstants.ANIM_STAND, new int[]{0}, 20);
			addAnimation(PersonageConstants.ANIM_RUN, new int[]{1,2,4,6,7,8,9,10,11,12,13,14}, 20);
			addAnimation(PersonageConstants.ANIM_JUMPUP, new int[]{39,40,41}, 13);
			addAnimation(PersonageConstants.ANIM_JUMPDOWN, new int[]{17,18,19}, 13);
			redefineHitBox( (sprite.getWidth()*35) / 100, (sprite.getHeight()*10) / 100, (sprite.getWidth()*40) / 100, (sprite.getHeight()*84) / 100);
			break;
		}
		
		

	}

	@Override
	public void collide(GameEntity _gameEntity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void die() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(long gameTime) {
		movePersoManager.calculNewSpeed(this);
		
		//Set the direction based on the button pushed
		if(movePersoManager.isLeftEnabled) {
			direction = DirectionConstants.LEFT;
		} else if (movePersoManager.isRightEnabled) {
			direction = DirectionConstants.RIGHT;
		}
		
		super.update(gameTime);

		//Play the correct animation
		if(isJumpingUp) {
			play(PersonageConstants.ANIM_JUMPUP, false, true);
		} else if (isJumpingDown) {
			play(PersonageConstants.ANIM_JUMPDOWN, false, true);
		} else if (isMovingHorizontally || movePersoManager.isLeftEnabled || movePersoManager.isRightEnabled) {
			play(PersonageConstants.ANIM_RUN, true, true);
		} else {
			play(PersonageConstants.ANIM_STAND, false, true);
		}
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

}
