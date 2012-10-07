package com.survivalkid.game.move;

import com.survivalkid.game.entity.personage.Personage;

public abstract class AbstractMove {
	
	
	// GRAVITY
	public int GRAVITY = 3;
	public int NB_FRAME_JUMP = 9;
	public int NB_FRAME_JUMP_SLOW = 17;
	
	
	public int VITESSE_X = 8;
	public int VITESSE_Y = -21;
	
	private int durationJump = 0;
	private boolean isDescending = false;
	
	public abstract boolean isOnLeft(int x, int y);
	
	public abstract boolean isOnRight(int x, int y);
	
	public abstract boolean isOnTop(int x, int y);
	
	public void moveToLeft(Personage perso) {
		perso.setSpeedX(-VITESSE_X);
	}
	
	public void moveToRight(Personage perso) {
		perso.setSpeedX(VITESSE_X);
	}
	
	public void jump(Personage perso) {
		if (perso.onFloor()) {
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
				perso.setSpeedY(VITESSE_Y+durationJump);
			}
		}
	}	
	
	public void noJump(Personage perso) {
		isDescending = !perso.onFloor();
	}
	
}
