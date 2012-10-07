package com.survivalkid.game.util;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;

import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.move.AbstractMove;
import com.survivalkid.game.move.MoveImplSideScreen;
import com.survivalkid.game.singleton.GameContext;

@SuppressLint("NewApi")
public final class MoveUtil {

	/** the display of the activity */
	private static final Display display = GameContext.getSingleton().getDisplay();
	
	/** Define the implementation of move */
	private static AbstractMove moveImpl = new MoveImplSideScreen();
	
	public static final int GRAVITY = moveImpl.GRAVITY;
	public static int MAX_X;
	public static int MAX_Y;
	
	/** list of the last pointer enabled */
	private static List<Integer> lastPidPressed;
	
	/** true if the priority if moving to left, false to right (in case there is both left and right moving enabled) */
	public static boolean lastEnabledLeft = true;
	
	private static boolean isLeftEnabled = false;
	private static boolean isRightEnabled = false;
	private static boolean isTopEnabled = false;
	
	static {
		Point size = new Point();
		display.getSize(size);
		MAX_X = size.x;
		MAX_Y = size.y - 30;
		lastPidPressed = new ArrayList<Integer>();
	}

	private MoveUtil() {
		// static class, the constructor can't be called
	};
	
	/**
	 * Calculate the new speed of the perso
	 * 
	 * @param perso the perso to apply the new speed
	 */
	public static void calculNewSpeed(Personage perso) {
		if (isLeftEnabled && (!isRightEnabled || lastEnabledLeft)) {
			moveImpl.moveToLeft(perso);
		}
		else if (isRightEnabled) {
			moveImpl.moveToRight(perso);
		}
		if (isTopEnabled) {
			moveImpl.jump(perso);
		}
		else {
			moveImpl.noJump(perso);
		}
	}
	
	/**
	 * Calculate the move to apply which the touch event
	 * 
	 * @param event event of motion
	 */
	public synchronized static void calculMove(MotionEvent event) {
		// get the action and the pid of the pointer
		int action =  event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		int pid = action >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		Integer pidObject = pid;
		int pidUP = -1;
		
		switch (actionCode) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			lastPidPressed.remove(pidObject); // security in case of the point_up don't catch by the previous pressed 
			lastPidPressed.add(pidObject);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			lastPidPressed.remove(pidObject);
			pidUP = pid;
			break;
		default:
			break;
		}

		// recalculate the move to do.
		isLeftEnabled = false;
		isRightEnabled = false;
		isTopEnabled = false;
		calculChange(event, pidUP);

	}	

	/**
	 * Calculate the changement to aply to the moving
	 * 
	 * @param event the event which contains all the data
	 * @param ignorePid a pid to ignore (pid corresponding to a UP event)
	 */
	public static void calculChange(MotionEvent event, int ignorePid) {
		List<Integer> onLeft = new ArrayList<Integer>();
		List<Integer> onRight = new ArrayList<Integer>();
		for (int i = 0; i < event.getPointerCount(); i++) {
			// the pid up is ignored if exist
			if (i == ignorePid) continue;
			
			int x = (int) event.getX(i);
			int y = (int) event.getY(i);
			// if the pointer has moved or not have previous position
			if (moveImpl.isOnLeft(x, y)) {
				onLeft.add(i);
				isLeftEnabled = true;
			}
			else if (moveImpl.isOnRight(x, y)) {
				onRight.add(i);
				isRightEnabled = true;
			}
			if (moveImpl.isOnTop(x, y)) {
				isTopEnabled = true;
			}
		}
		// Calculate if left or right has priority : the last key enabled
		try {
			if (isLeftEnabled && isRightEnabled) {
				for(int i=lastPidPressed.size()-1 ; i>=0 ; i++) {
					int lastPid = lastPidPressed.get(i);
					if (onLeft.contains(lastPid)) {
						// left has priority
						lastEnabledLeft = true;
						break;
					}
					if (onRight.contains(lastPid)) {
						// right has priority
						lastEnabledLeft = false;
						break;
					}				
				}
			}
		}
		catch(IndexOutOfBoundsException e) {
			// lastPidPressed has change during the calculation, the process continue we ignore the last priority
		}
	}

	
	/**
	 * Test if there is the perso is moving horizontally 
	 * 
	 * @return true if the perso is moving to the left or to the right
	 */
	public static boolean isHorizontalMoving() {
		return isLeftEnabled || isRightEnabled;
	}


	
}
