package com.survivalkid.game.singleton;

import java.util.HashSet;
import java.util.Set;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.manager.ParticleManager;

/**
 * Dirty singleton that contains shared variables.
 * @author Callil
 *
 */
public class SharedVars {

	/** The instance. */
	private static SharedVars instance;
	
	private Set<AnimatedSprite> bullWarningLeftList;
	private Set<AnimatedSprite> bullWarningRightList;
	
	private ParticleManager particleManager;
	
	/** Get the singleton. */
	public static SharedVars getSingleton() {
		if (instance == null) {
			instance = new SharedVars();
		}
		return instance;
	}
	
	/**
	 * Initialize the singleton
	 */
	public void initSingleton() {
		setBullWarningLeftList(new HashSet<AnimatedSprite>());
		setBullWarningRightList(new HashSet<AnimatedSprite>());
	}

	/**
	 * @return the bullWarningList
	 */
	public Set<AnimatedSprite> getBullWarningLeftList() {
		return bullWarningLeftList;
	}

	/**
	 * @param bullWarningList the bullWarningList to set
	 */
	public void setBullWarningLeftList(Set<AnimatedSprite> bullWarningList) {
		this.bullWarningLeftList = bullWarningList;
	}

	/**
	 * @return the bullWarningRightList
	 */
	public Set<AnimatedSprite> getBullWarningRightList() {
		return bullWarningRightList;
	}

	/**
	 * @param bullWarningRightList the bullWarningRightList to set
	 */
	public void setBullWarningRightList(Set<AnimatedSprite> bullWarningRightList) {
		this.bullWarningRightList = bullWarningRightList;
	}

	/**
	 * @return the particleManager
	 */
	public ParticleManager getParticleManager() {
		return particleManager;
	}

	/**
	 * @param particleManager the particleManager to set
	 */
	public void setParticleManager(ParticleManager particleManager) {
		this.particleManager = particleManager;
	}
	
}