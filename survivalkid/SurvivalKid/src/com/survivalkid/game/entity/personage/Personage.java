package com.survivalkid.game.entity.personage;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.survivalkid.R;
import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.Constants.PersonageConstants;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.Life;
import com.survivalkid.game.move.MovePersoManager;

public class Personage extends GameEntity {
	private static final String TAG = Personage.class.getSimpleName();

	private int playerNumber = -1;
	private int persoType;
	private Life life;
	private AnimatedSprite deathAnim;

	private boolean dying;

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
	public Personage(int perso, Resources resource, int spriteSheetId, int x, int y, int nbColum, int nbRows) {
		super("CharacterA", BitmapFactory.decodeResource(resource, spriteSheetId), x, y, nbColum, nbRows);
		gravity = 2;
		persoType = perso;
		movePersoManager = new MovePersoManager();
		life = new Life(100);
		deathAnim = new AnimatedSprite(BitmapFactory.decodeResource(resource, R.drawable.dead_anim), 0, 0, 7, 1);
		dying = false;

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
			addAnimation(PersonageConstants.ANIM_JUMPUP, new int[] { 39, 40, 41 }, 13);
			addAnimation(PersonageConstants.ANIM_JUMPDOWN, new int[] { 17, 18, 19 }, 13);
			redefineHitBox((sprite.getWidth() * 35) / 100, (sprite.getHeight() * 10) / 100,
					(sprite.getWidth() * 40) / 100, (sprite.getHeight() * 84) / 100);
			break;
		}
		
		deathAnim.addAnimation("die", new int[] {0,1,2,3,4,5,6 }, 17);

	}

	@Override
	public void collide(GameEntity _gameEntity) {
		// TODO Auto-generated method stub

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
			if(deathAnim.isAnimationFinished()) {
				dead=true;
			}
		} else if (!dying && life.getCurrentLife() == 0) {
			die();
			return;
		}

		movePersoManager.calculNewSpeed(this);

		// Set the direction based on the button pushed
		if (movePersoManager.isLeftEnabled) {
			direction = DirectionConstants.LEFT;
		} else if (movePersoManager.isRightEnabled) {
			direction = DirectionConstants.RIGHT;
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
	}

	@Override
	public void draw(Canvas canvas) {
		if (dying) {
			deathAnim.draw(canvas, direction);
		} else {
			super.draw(canvas);
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
