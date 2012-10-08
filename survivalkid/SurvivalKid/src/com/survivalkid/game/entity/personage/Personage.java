package com.survivalkid.game.entity.personage;

import android.graphics.Bitmap;

import com.survivalkid.game.core.Constants.PersonageConstants;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.util.MoveUtil;

public class Personage extends GameEntity {

	private int playerNumber = -1;
	private int persoType;

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
		setSubjectToGravity(true);
		persoType = perso;

		switch (perso) {
		case PersonageConstants.PERSO_YUGO:
			addAnimation(PersonageConstants.ANIM_STAND, new int[]{0}, 20);
			addAnimation(PersonageConstants.ANIM_RUN, new int[]{1,2,3,5,6,7,9,10,11,12,13,14}, 20);
			addAnimation(PersonageConstants.ANIM_JUMPUP, new int[]{17,18,19}, 13);
			addAnimation(PersonageConstants.ANIM_JUMPDOWN, new int[]{17,18,19}, 13);
			redefineHitBox( (sprite.getX()*23) / 100, (sprite.getY()*10) / 100, (sprite.getWidth()*50) / 100, (sprite.getHeight()*84) / 100);
			break;
		case PersonageConstants.PERSO_YUNA:
			addAnimation(PersonageConstants.ANIM_STAND, new int[]{0}, 20);
			addAnimation(PersonageConstants.ANIM_RUN, new int[]{1,2,4,6,7,8,9,10,11,12,13,14}, 20);
			addAnimation(PersonageConstants.ANIM_JUMPUP, new int[]{39,40,41}, 13);
			addAnimation(PersonageConstants.ANIM_JUMPDOWN, new int[]{17,18,19}, 13);
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
		if (playerNumber == 0) {
			MoveUtil.calculNewSpeed(this);
		}
		if (!MoveUtil.isHorizontalMoving()) {
			setSpeedX(getSpeedX() / 3);
		}
		super.update(gameTime);
		
		if(isJumpingUp) {
			play(PersonageConstants.ANIM_JUMPUP, false, true);
		} else if (isJumpingDown) {
			play(PersonageConstants.ANIM_JUMPDOWN, false, true);
		} else if (isMovingHorizontally) {
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

}
