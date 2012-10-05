package com.survivalkid.game.util;

import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.singleton.GameContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;

@SuppressLint("NewApi")
public final class MoveUtil {

	private static final Display display = GameContext.getSingleton().getDisplay();
	
	/** To check the last direction pressed */
	private static boolean goOnLeft = false;
	
	public static final int GRAVITY = 5;
	public static int VITESSE_X = 5;
	public static int VITESSE_Y = -25;
	public static int MAX_X;
	public static int MAX_Y;
	
	static {
		Point size = new Point();
		display.getSize(size);
		MAX_X = size.x;
		MAX_Y = size.y;
	}

	private MoveUtil() {
	};
	
	public static void calculMove(int touchX, int touchY, int action, Personage perso) {
		boolean isOnLeft = isOnLeft(touchX, touchY);
		boolean isOnRight = isOnRight(touchX, touchY);
		
		if (action == MotionEvent.ACTION_UP) {
			// if you release the left touch, then the left touch will be prioritary
			goOnLeft  = isOnLeft;
		}
		else {
			// if the left of the screen is toggle
			if (isOnLeft && !isOnRight || goOnLeft) {
				perso.setSpeedX(-VITESSE_X);
			}
			else if (isOnRight) {
				perso.setSpeedX(VITESSE_X);
			}
			if (isOnTop(touchX, touchY) && !perso.onFloor()) {
				perso.setSpeedY(VITESSE_Y);
			}
		}

	}
	
	public static boolean isOnLeft(int x, int y) {
		return x<MAX_X/2;
	}
	
	public static boolean isOnRight(int x, int y) {
		return x>MAX_X/2;
	}
	
	public static boolean isOnTop(int x, int y) {
		return y>MAX_Y/2;
	}
	
}
