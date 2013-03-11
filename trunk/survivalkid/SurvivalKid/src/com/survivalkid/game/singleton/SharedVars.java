package com.survivalkid.game.singleton;

import java.util.ArrayList;
import java.util.List;

import com.survivalkid.game.core.AnimatedSprite;

/**
 * Dirty singleton that contains shared variables.
 * @author Callil
 *
 */
public class SharedVars {

	/** The instance. */
	private static SharedVars instance;
	
	private List<AnimatedSprite> bullWarningLeftList;
	private List<AnimatedSprite> bullWarningRightList;
	
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
		setBullWarningLeftList(new ArrayList<AnimatedSprite>());
		setBullWarningRightList(new ArrayList<AnimatedSprite>());
	}

	/**
	 * @return the bullWarningList
	 */
	public List<AnimatedSprite> getBullWarningLeftList() {
		return bullWarningLeftList;
	}

	/**
	 * @param bullWarningList the bullWarningList to set
	 */
	public void setBullWarningLeftList(List<AnimatedSprite> bullWarningList) {
		this.bullWarningLeftList = bullWarningList;
	}

	/**
	 * @return the bullWarningRightList
	 */
	public List<AnimatedSprite> getBullWarningRightList() {
		return bullWarningRightList;
	}

	/**
	 * @param bullWarningRightList the bullWarningRightList to set
	 */
	public void setBullWarningRightList(List<AnimatedSprite> bullWarningRightList) {
		this.bullWarningRightList = bullWarningRightList;
	}
	
}
