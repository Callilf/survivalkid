package com.survivalkid.game.move;

import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.thread.MainThread;

public abstract class AbstractMove {
	
	
	protected static final int DEFAULT_NB_FRAME_JUMP = (int) (20/MainThread.FPS_RATIO);
	protected static final int DEFAULT_NB_FRAME_JUMP_SLOW = (int) (17/MainThread.FPS_RATIO);

	protected static final float DEFAULT_VITESSE_MAX_X = 7;
	protected static final float DEFAULT_ACCELERATION_X = 0.7f;
	protected static final float DEFAULT_DECELERATE_X = 1;
	protected static final float DEFAULT_VITESSE_Y = -12;

	protected int nbFrameJump = (int) (20/MainThread.FPS_RATIO);
	protected int nbFrameJumpSlow = (int) (17/MainThread.FPS_RATIO);
	
	protected float vitesseMaxX = 7;
	protected float accelerationX = 0.7f;
	protected float decelerateX = 1;
	protected float vitesseY = -12;
	
	private int durationJump = 0;
	private boolean isDescending = false;
	private boolean hasReleasedJump = true;
	
	public AbstractMove() {
		init();
	}
	
	public void init() {
		nbFrameJump = DEFAULT_NB_FRAME_JUMP;
		nbFrameJumpSlow = DEFAULT_NB_FRAME_JUMP_SLOW;
		vitesseMaxX = DEFAULT_VITESSE_MAX_X;
		accelerationX = DEFAULT_ACCELERATION_X;
		decelerateX = DEFAULT_DECELERATE_X;
		vitesseY = DEFAULT_VITESSE_Y;
	}

	public abstract boolean isOnLeft(int x, int y);
	
	public abstract boolean isOnRight(int x, int y);
	
	public abstract boolean isOnTop(int x, int y);
	
	public boolean keepPid(int pid) {
		return true;
	}
	
	public void startNewFrame() {
	}
	
	public void moveToLeft(Personage perso) {
		perso.setSpeedX(Math.max(-vitesseMaxX,perso.getSpeedX()-accelerationX));
	}
	
	public void moveToRight(Personage perso) {
		perso.setSpeedX(Math.min(vitesseMaxX,perso.getSpeedX()+accelerationX));
	}
	
	public void jump(Personage perso) {
		if (perso.onFloor() && hasReleasedJump) {
			hasReleasedJump = false;
			isDescending = false;
			durationJump = 0;
			perso.setSpeedY(vitesseY);
		}
		else if (!isDescending) {
			// if the perso maintain jump
			durationJump++;
			if (durationJump < nbFrameJump) {
				perso.setSpeedY(vitesseY);
			}
			else if (durationJump < nbFrameJumpSlow) {
				perso.setSpeedY(vitesseY+perso.getGravity()/2);
			}
		}
	}	
	
	/**
	 * Indicate that the player don't click on jump
	 * 
	 * @param perso the personage
	 */
	public void noJump(Personage perso) {
		isDescending = !perso.onFloor();
		hasReleasedJump = true;
	}

	/**
	 * Indicate that the player don't click on left or right
	 * 
	 * @param perso the personage
	 */
	public void slowDown(Personage perso) {
		float vitesseX = perso.getSpeedX();
		if (vitesseX != 0) {
			if (vitesseX > 0)
			{
				vitesseX = Math.max(0, vitesseX-decelerateX);
			}
			else
			{
				vitesseX = Math.min(0, vitesseX+decelerateX);
			}
			perso.setSpeedX(vitesseX);
		}
	}
	
	public void modifySpeedPlayer(float factor) {
		nbFrameJump = (int) (DEFAULT_NB_FRAME_JUMP * 1/factor);
		nbFrameJumpSlow = (int) (DEFAULT_NB_FRAME_JUMP_SLOW * 1/factor);
		vitesseMaxX = DEFAULT_VITESSE_MAX_X * factor;
		accelerationX = DEFAULT_ACCELERATION_X * factor;
		decelerateX = DEFAULT_DECELERATE_X * factor;
		vitesseY = DEFAULT_VITESSE_Y * factor;
	}
	
}
