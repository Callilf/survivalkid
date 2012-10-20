package com.survivalkid.game.entity;

import static java.lang.Math.max;

/**
 * Class which contain the life
 * 
 * @author Thomas
 */
public class Life {

	private int maxLife;
	private int currentLife;
	
	public enum EnumLife { 
			
		/** loose life */
		TAKE_DAMAGE(-1, ""), 
		/** loose a percentage of life */
		TAKE_DAMAGE_PC(-1, "%"), 
		/** gain life */
		RECOVERY_LIFE(1, ""),
		/** gain a percentage of life */
		RECOVERY_LIFE_PC(1, "%");
		
		private int facteur;
		private String unite;
		private EnumLife(int _facteur, String _unite) {
			facteur = _facteur;
			unite = _unite;
		}
		public int getFacteur() {
			return facteur;
		}
		public String getUnite() {
			return unite;
		}
	}
	
	/**
	 * Constructor which initialize the max life and the current Life at hp
	 * 
	 * @param hp initial hp
	 */
	public Life(int hp) {
		this(hp, hp);
	}
	
	/**
	 * Constructor which initialize the max life at hpMax and the current Life at hp
	 * 
	 * @param hp initial hp
	 * @param hpMax initial hp max
	 */
	public Life(int hp, int hpMax) {
		maxLife = hp;
		currentLife = hpMax;			
	}	
	
	public boolean modifyLife(int hp, EnumLife typeChange) {
		switch (typeChange) {
		case TAKE_DAMAGE:
			return looseLife(hp);
		case RECOVERY_LIFE:
			return gainLife(hp);
		case TAKE_DAMAGE_PC:
			return loosePcLife(hp);
		case RECOVERY_LIFE_PC:
			return gainPcLife(hp);
		default:
			throw new IllegalStateException("Typechange " + typeChange + "unknown");
		}
	}
	
	/**
	 * Decrease the hp of the perso
	 * @param hp the augmentation of pv
	 * @return true if the entity has no remained life
	 */
	private boolean looseLife(int hp)
	{
		currentLife = max(0, currentLife - hp);
		return currentLife == 0;
	}	
	
	/**
	 * Increase the hp of the perso
	 * @param hp the augmentation of pv
	 * @return true if the entity has the max if his life
	 */
	private boolean gainLife(int hp)
	{
		currentLife = Math.min(max(currentLife, maxLife), currentLife + hp);
		return maxLife == currentLife;
	}
	
	private boolean loosePcLife(int pourcent) {
		return looseLife(maxLife * pourcent / 100);
	}
	
	private boolean gainPcLife(int pourcent) {
		return gainLife(maxLife * pourcent / 100);
	}

	/// GETTER AND SETTER
	
	public float getCurrentPcLife() {
		return currentLife / (float) maxLife;
	}

	public int getMaxLife() {
		return maxLife;
	}

	public void setMaxLife(int maxLife) {
		this.maxLife = maxLife;
	}

	public int getCurrentLife() {
		return currentLife;
	}

	public void setCurrentLife(int currentLife) {
		this.currentLife = currentLife;
	}

}
