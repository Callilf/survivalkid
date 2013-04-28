package com.survivalkid.game.singleton;

import java.util.HashSet;
import java.util.Set;

import android.graphics.Point;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Difficulty;
import com.survivalkid.game.manager.DecorManager;
import com.survivalkid.game.manager.EnemyManager;
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
	private DecorManager decorManager;
	private EnemyManager enemyManager;
	private Difficulty difficulty;

	private boolean persoDrawnInBackground;
	
	//When true, characters items and enemies are frozen
	private boolean frozen;
	private Point corridaPosition;
	
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
		persoDrawnInBackground = false;
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

	/**
	 * @return the persoDrawnInBackground
	 */
	public boolean isPersoDrawnInBackground() {
		return persoDrawnInBackground;
	}

	/**
	 * @param persoDrawnInBackground the persoDrawnInBackground to set
	 */
	public void setPersoDrawnInBackground(boolean persoDrawnInBackground) {
		this.persoDrawnInBackground = persoDrawnInBackground;
	}

	/**
	 * @return the enemyManager
	 */
	public EnemyManager getEnemyManager() {
		return enemyManager;
	}

	/**
	 * @param enemyManager the enemyManager to set
	 */
	public void setEnemyManager(EnemyManager enemyManager) {
		this.enemyManager = enemyManager;
	}
	
	/**
	 * @return the difficulty
	 */
	public Difficulty getDifficulty() {
		return difficulty;
	}

	/**
	 * @param difficulty the difficulty to set
	 */
	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	/**
	 * @return the frozen
	 */
	public boolean isFrozen() {
		return frozen;
	}

	/**
	 * @param frozen the frozen to set
	 */
	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	/**
	 * @return the decorManager
	 */
	public DecorManager getDecorManager() {
		return decorManager;
	}

	/**
	 * @param decorManager the decorManager to set
	 */
	public void setDecorManager(DecorManager decorManager) {
		this.decorManager = decorManager;
	}

	/**
	 * @return the corridaPosition
	 */
	public Point getCorridaPosition() {
		return corridaPosition;
	}

	/**
	 * @param corridaPosition the corridaPosition to set
	 */
	public void setCorridaPosition(Point corridaPosition) {
		this.corridaPosition = corridaPosition;
	}
}
