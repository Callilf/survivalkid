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
	
	/**
	 * Decrease the hp of the perso
	 * @param hp the augmentation of pv
	 * @return true if the entity has no remained life
	 */
	public boolean looseLife(int hp)
	{
		currentLife = max(0, currentLife - hp);
		return currentLife == 0;
	}	
	
	/**
	 * Increase the hp of the perso
	 * @param hp the augmentation of pv
	 * @return true if the entity has the max if his life
	 */
	public boolean gainLife(int hp)
	{
		currentLife = Math.min(max(currentLife, maxLife), currentLife + hp);
		return maxLife == currentLife;
	}
	
	public boolean loosePcLife(int pourcent) {
		return looseLife(maxLife * pourcent / 100);
	}
	
	public boolean gainPcLife(int pourcent) {
		return gainLife(maxLife * pourcent / 100);
	}

	/// GETTER AND SETTER
	
	public float getCurrentPcLife() {
		return currentLife / maxLife;
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
