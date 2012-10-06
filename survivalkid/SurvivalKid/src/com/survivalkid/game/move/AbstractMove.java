package com.survivalkid.game.move;

import com.survivalkid.game.entity.personage.Personage;

public abstract class AbstractMove {
	
	public int GRAVITY = 5;
	public int VITESSE_X = 30;
	public int VITESSE_Y = -25;
	
	private int durationJump = 0;
	
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
			durationJump = 0;
			perso.setSpeedY(VITESSE_Y);
		}
		else if (durationJump++ < 40) {
			perso.setSpeedY(VITESSE_Y);
		}
		else if (durationJump++ < 65) {
			perso.setSpeedY(VITESSE_Y+durationJump);
		}
		
	}	
	
}
