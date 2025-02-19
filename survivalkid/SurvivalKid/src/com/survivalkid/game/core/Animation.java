package com.survivalkid.game.core;


public class Animation {

	private String name;
	private int[] frameList;
	private int fps;
	
	public Animation(String _name, int[] _frameList, int _fps) {
		name = _name;
		frameList = _frameList;
		fps = _fps;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the frameList
	 */
	public int[] getFrameList() {
		return frameList;
	}

	/**
	 * @param frameList the frameList to set
	 */
	public void setFrameList(int[] frameList) {
		this.frameList = frameList;
	}

	/**
	 * @return the fps
	 */
	public int getFps() {
		return fps;
	}

	/**
	 * @param fps the fps to set
	 */
	public void setFps(int fps) {
		this.fps = fps;
	}
	
	
}
