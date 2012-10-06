package com.survivalkid.game.util;

import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.move.AbstractMove;
import com.survivalkid.game.move.MoveImplSideScreen;
import com.survivalkid.game.singleton.GameContext;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;

@SuppressLint("NewApi")
public final class MoveUtil {

	private static final Display display = GameContext.getSingleton().getDisplay();
	
	/** Define the implementation of move */
	private static AbstractMove moveImpl = new MoveImplSideScreen();
	

	
	public static final int GRAVITY = moveImpl.GRAVITY;
	public static int MAX_X;
	public static int MAX_Y;
	
	private static int isLeftEnabled = 0;
	private static int isRightEnabled = 0;
	private static int isTopEnabled = 0;
	
	/** To check the last direction pressed */
	private static boolean lastEnabledLeft = false;	
	
	static {
		Point size = new Point();
		display.getSize(size);
		MAX_X = size.x;
		MAX_Y = size.y;
	}

	private MoveUtil() {
	};
	
	/**
	 * Calculate what has changed
	 * 
	 * @param touchX new position X
	 * @param touchY new position Y
	 * @param event event of motion
	 * @param perso personage
	 */
	public static void calculMove(MotionEvent event, Personage perso) {
		int touchX = (int) event.getX();
		int touchY = (int) event.getY();
		
		
		int action =  event.getAction();
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			actionOnDown(touchX, touchY);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			actionOnUp(touchX, touchY);
			break;
		case MotionEvent.ACTION_MOVE:
			actionOnMove(touchX, touchY);
			//int nbEvent = event.getHistorySize();
			//actionOnUp((int) event.getHistoricalX(nbEvent-1), (int) event.getHistoricalY(nbEvent-1));
			break;
		default:
			break;
		}

	}
	
	public static void calculNewSpeed(Personage perso) {
		if (isLeftEnabled > 0 && (isRightEnabled == 0 || lastEnabledLeft)) {
			moveImpl.moveToLeft(perso);
			lastEnabledLeft = true;
		}
		else if (isRightEnabled > 0) {
			moveImpl.moveToRight(perso);
			lastEnabledLeft = false;
		}
		if (isTopEnabled > 0) {
			moveImpl.jump(perso);
		}		
	}
	
	public static void actionOnDown(int touchX, int touchY) {
		boolean verif1 = false, verif2 = false, verif3 = false;
		if (moveImpl.isOnLeft(touchX, touchY)) { isLeftEnabled=1; isRightEnabled=0; lastEnabledLeft = true; }
		if (moveImpl.isOnRight(touchX, touchY)) { isRightEnabled=1; isLeftEnabled=0; lastEnabledLeft = false; }
		if (moveImpl.isOnTop(touchX, touchY)) isTopEnabled=1;
		else isTopEnabled=0;
	}
	
	public static void actionOnMove(int touchX, int touchY) {
		boolean verif1 = false, verif2 = false, verif3 = false;
		if (moveImpl.isOnLeft(touchX, touchY)) isLeftEnabled=1;
		if (moveImpl.isOnRight(touchX, touchY)) isRightEnabled=1;
		if (moveImpl.isOnTop(touchX, touchY)) isTopEnabled=1;
		else isTopEnabled=0;
	}
	
	public static void actionOnUp(int touchX, int touchY) {
		if (moveImpl.isOnLeft(touchX, touchY)) isLeftEnabled=0;
		if (moveImpl.isOnRight(touchX, touchY)) isRightEnabled=0;
		if (moveImpl.isOnTop(touchX, touchY)) isTopEnabled=0;
	}
	
	public static boolean isHorizontalMoving() {
		return isLeftEnabled != 0 || isRightEnabled != 0;
	}

	
}
