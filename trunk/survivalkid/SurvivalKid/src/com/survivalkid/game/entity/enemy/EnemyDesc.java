package com.survivalkid.game.entity.enemy;

import java.util.ArrayList;
import java.util.List;

import com.survivalkid.R;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.singleton.GameContext;

/**
 * Describe each enemy
 * 
 * @author Thomas
  */
public enum EnemyDesc {
	
	
	BULL(R.string.bull, R.string.bullDesc, SpriteEnum.BULL, 1, 25, 30, 40, 50),
	CATERPILLAR(R.string.caterpillar, R.string.caterpillarDesc, SpriteEnum.CATERPILLAR, 0, 5, 5, 10, 15),
	CATERPILLAR_PURPLE(R.string.caterpillar, R.string.caterpillarDesc, SpriteEnum.CATERPILLAR_PURPLE, 0, 10, 10, 15, 20),
	CIRCULAR_SAW(R.string.circularSaw, R.string.circularSawDesc, SpriteEnum.CIRCULAR_SAW, 1, 15, 20, 25, 30),
	FIRE_GROUND(R.string.fireGround, -1, SpriteEnum.FIRE_GROUND, 1, 1, 2, 3),
	FIRE_METEOR(R.string.fireMeteor, R.string.fireMeteorDesc, SpriteEnum.FIRE_METEORE, 1, 10, 10, 15, 20),
	METEORE(R.string.meteor, R.string.meteorDesc, SpriteEnum.METEORE, 0, 10, 10, 15, 20);
	
	private String name;
	private String txtDesc;
	private SpriteEnum spriteEnum;
	private int difficulty; // the difficulty of the enemy, not the difficulty of the game
	private List<Integer> damage;
	
	private EnemyDesc(int idName, int idTxtDesc, SpriteEnum spriteEnum, int difficulty, int... damages) {
		this.name = GameContext.getSingleton().getContext().getString(idName);
		if (idTxtDesc != -1) {
			this.txtDesc = GameContext.getSingleton().getContext().getString(idTxtDesc);
		}
		this.spriteEnum = spriteEnum;
		this.difficulty = difficulty;
		this.damage = new ArrayList<Integer>();
		for (int damage : damages) {
			this.damage.add(damage);
		}
	}
	
	public String getName() {
		return name;
	}

	public String getTxtDesc() {
		return txtDesc;
	}

	public SpriteEnum getSpriteEnum() {
		return spriteEnum;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public int getDamage(int difficulty) {
		// difficulty 1 = easy => return the element at index 0
		// we can set any level of difficulty, it take the last element if not exist
		return damage.get(Math.min(difficulty, damage.size())-1);
	}
}
