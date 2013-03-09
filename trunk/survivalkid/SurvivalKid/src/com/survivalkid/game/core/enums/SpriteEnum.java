package com.survivalkid.game.core.enums;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.survivalkid.R;
import com.survivalkid.game.core.Animation;
import com.survivalkid.game.core.Constants.PersonageConstants;
import com.survivalkid.game.util.BitmapUtil;
import com.survivalkid.game.util.MoveUtil;

public enum SpriteEnum {
	BALLOON_CRATE(R.drawable.balloon_crate, 6, 2, new Animation("swing", new int[] { 0, 1, 2, 1, 0, 3, 4, 3}, 8),
			new Animation("explode", new int[] { 5, 6, 7, 8, 9, 10}, 40),
			new Animation("exploded", new int[] { 10}, 20)),
	
	YUGO(R.drawable.yugo, 6, 4, new Animation(PersonageConstants.ANIM_STAND, new int[] { 0 }, 20), 
			new Animation(PersonageConstants.ANIM_RUN, new int[] { 1, 2, 3, 5, 6, 7, 9, 10, 11, 12, 13, 14 }, 20), 
			new Animation(PersonageConstants.ANIM_JUMPUP, new int[] { 19, 18, 17 }, 13), 
			new Animation(PersonageConstants.ANIM_JUMPDOWN, new int[] { 19 }, 13),
			new Animation(PersonageConstants.ANIM_KNOCK_BACK, new int[] { 20 }, 13)),

	YUNA(R.drawable.yuna, 6, 4, new Animation(PersonageConstants.ANIM_STAND, new int[] { 0 }, 20), 
			new Animation(PersonageConstants.ANIM_RUN, new int[] { 1, 2, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14 }, 20), 
			new Animation(PersonageConstants.ANIM_JUMPUP, new int[] { 20, 21, 22 }, 13), 
			new Animation(PersonageConstants.ANIM_JUMPDOWN, new int[] { 17, 18, 19 }, 13),
			new Animation(PersonageConstants.ANIM_KNOCK_BACK, new int[] {23}, 13)),
	
	CIRCULAR_SAW(R.drawable.enemy_circular_saw, 10, 1, new Animation("rotate", new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, 20)),
	
	CATERPILLAR(R.drawable.caterpillar, 4, 1, new Animation("crawl", new int[] { 0, 1, 2, 3, 2, 1 }, 13)),
	
	CATERPILLAR_PURPLE(R.drawable.caterpillar_purple, 4, 1, new Animation("crawl", new int[] { 0, 1, 2, 3, 2, 1 }, 13)),
	
	BULL(R.drawable.bull, 4, 2, new Animation("run", new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, 30)),
	
	BULL_WARNING(R.drawable.bull_warning, 4, 2, new Animation("run", new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, 30)),
	
	CRATE_EXPLOSION(R.drawable.crate_explosion, 6, 1, new Animation("explode", new int[] { 0, 1, 2, 3, 4, 5 }, 20)),
	
	METEORE(R.drawable.meteor, 1, 1),
	
	METEORE_EXPLOSION(R.drawable.meteor_explosion, 6, 2,  new Animation("explode", new int[] { 0, 1, 2, 3, 4, 5}, 20)),
	
	FIRE_METEORE(R.drawable.meteor_fire, 1, 1),
	
	FIRE_METEORE_EXPLOSION(R.drawable.meteor_fire_explosion, 6, 2,  new Animation("explode", new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8}, 20)),
	
	MEDKIT(R.drawable.medkit, 1, 1),
	
	RED_CLOCK(R.drawable.item_red_clock, 1, 1),
	
	FIRE_TRAIL(R.drawable.fire, 6, 1, new Animation("fire", new int[] { 0, 1, 2, 3, 4, 5 }, 15)),
	
	FIRE_GROUND(R.drawable.fire_ground, 7, 2,  new Animation("fire", new int[] { 0, 1, 2, 3, 4, 5, 6 }, 15),
			new Animation("faint", new int[] { 7, 8, 9, 10, 11 }, 10)),
	
	SMOKE_WHITE_LARGE(R.drawable.dead_anim, 7, 1,  new Animation("die", new int[] { 0, 1, 2, 3, 4, 5, 6 }, 25)),
	
	SMOKE_WHITE_SMALL(R.drawable.dead_anim_small, 7, 1,  new Animation("die", new int[] { 0, 1, 2, 3, 4, 5, 6 }, 25)),
	
	SMOKE_BROWN_LARGE(R.drawable.dead_anim_black, 7, 1, new Animation("die", new int[] { 0, 1, 2, 3, 4, 5, 6 }, 25));

	private Bitmap bitmap;
	private Bitmap flippedBitmap;
	private int nbColumn;
	private int nbRows;
	private Map<String, Animation> animations;

	/**
	 * Constructor.
	 * 
	 * @param _id
	 *            the resource id
	 * @param _nbColumn
	 *            the number of columns in the spritesheet
	 * @param _nbRows
	 *            the number of rows in the spritesheet
	 * @param _anims
	 *            the animations
	 */
	SpriteEnum(int _id, int _nbColumn, int _nbRows, Animation... _anims) {
		bitmap = BitmapUtil.createBitmap(_id);
		
		if (MoveUtil.RATIO_WIDTH != 1 || MoveUtil.RATIO_HEIGHT != 1) {
			Matrix matrixRatio = new Matrix();
			matrixRatio.preScale(MoveUtil.RATIO_WIDTH, MoveUtil.RATIO_HEIGHT);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrixRatio, true);
		}
		
		// Create the flipped image
		Matrix matrix = new Matrix();
		matrix.preScale(-1, 1);
		flippedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		
		nbColumn = _nbColumn;
		nbRows = _nbRows;

		animations = new HashMap<String, Animation>();
		for (Animation anim : _anims) {
			animations.put(anim.getName(), anim);
		}
	}

	/**
	 * @return the bitmap
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}

	/**
	 * @param bitmap the bitmap to set
	 */
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	/**
	 * @return the flippedBitmap
	 */
	public Bitmap getFlippedBitmap() {
		return flippedBitmap;
	}

	/**
	 * @param flippedBitmap the flippedBitmap to set
	 */
	public void setFlippedBitmap(Bitmap flippedBitmap) {
		this.flippedBitmap = flippedBitmap;
	}

	/**
	 * @return the nbColumn
	 */
	public int getNbColumn() {
		return nbColumn;
	}

	/**
	 * @param nbColumn the nbColumn to set
	 */
	public void setNbColumn(int nbColumn) {
		this.nbColumn = nbColumn;
	}

	/**
	 * @return the nbRows
	 */
	public int getNbRows() {
		return nbRows;
	}

	/**
	 * @param nbRows the nbRows to set
	 */
	public void setNbRows(int nbRows) {
		this.nbRows = nbRows;
	}

	/**
	 * @return the animations
	 */
	public Map<String, Animation> getAnimations() {
		return animations;
	}

	/**
	 * @param animations the animations to set
	 */
	public void setAnimations(Map<String, Animation> animations) {
		this.animations = animations;
	}
	
	
	
}
