package com.survivalkid.game.move;

import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.thread.MainThread;

public abstract class AbstractMove {
	
	
	// GRAVITY
	// public int GRAVITY = 3;
	protected int NB_FRAME_JUMP = (int) (20/MainThread.FPS_RATIO);
	protected int NB_FRAME_JUMP_SLOW = (int) (17/MainThread.FPS_RATIO);
	
	protected float VITESSE_MAX_X = 7;
	protected float ACCELERATION_X = 0.7f;
	protected float DECELERATE_X = 1;
	protected float VITESSE_Y = -12;
	
	private int durationJump = 0;
	private boolean isDescending = false;
	private boolean hasReleasedJump = true;
	
	public abstract boolean isOnLeft(int x, int y);
	
	public abstract boolean isOnRight(int x, int y);
	
	public abstract boolean isOnTop(int x, int y);
	
	public void moveToLeft(Personage perso) {
		perso.setSpeedX(Math.max(-VITESSE_MAX_X,perso.getSpeedX()-ACCELERATION_X));
	}
	
	public void moveToRight(Personage perso) {
		perso.setSpeedX(Math.min(VITESSE_MAX_X,perso.getSpeedX()+ACCELERATION_X));
	}
	
	public void jump(Personage perso) {
		if (perso.onFloor() && hasReleasedJump) {
			hasReleasedJump = false;
			isDescending = false;
			durationJump = 0;
			perso.setSpeedY(VITESSE_Y);
		}
		else if (!isDescending) {
			// if the perso maintain jump
			durationJump++;
			if (durationJump < NB_FRAME_JUMP) {
				perso.setSpeedY(VITESSE_Y);
			}
			else if (durationJump < NB_FRAME_JUMP_SLOW) {
				perso.setSpeedY(VITESSE_Y+perso.getGravity()/2);
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
				vitesseX = Math.max(0, vitesseX-DECELERATE_X);
			}
			else
			{
				vitesseX = Math.min(0, vitesseX+DECELERATE_X);
			}
			perso.setSpeedX(vitesseX);
		}
	}
	
}
