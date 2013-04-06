package com.survivalkid.game.move;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;

import com.survivalkid.game.core.TouchHandler;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.move.impl.MoveImplButton;
import com.survivalkid.game.util.MoveUtil;

public class MovePersoManager {

	/** Define the implementation of move */
	private AbstractMove moveImpl = new MoveImplButton();
	
	/** true if the priority if moving to left, false to right (in case there is both left and right moving enabled) */
	public boolean lastEnabledLeft;
	
	public boolean isLeftEnabled;
	public boolean isRightEnabled;
	public boolean isTopEnabled;
	
	/** list of the last pointer enabled */
	private static List<Integer> lastPidPressed;
	
	public MovePersoManager() {
		lastEnabledLeft = true;
		isLeftEnabled = false;
		isRightEnabled = false;
		isTopEnabled = false;
		lastPidPressed = new ArrayList<Integer>();
	}
	
	/**
	 * Calculate the new speed of the perso
	 * 
	 * @param perso the perso to apply the new speed
	 */
	public void calculNewSpeed(Personage perso) {
		if (isLeftEnabled && (!isRightEnabled || lastEnabledLeft)) {
			moveImpl.moveToLeft(perso);
		}
		else if (isRightEnabled) {
			moveImpl.moveToRight(perso);
		}
		else {
			moveImpl.slowDown(perso);
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
	public void calculMove(MotionEvent event, TouchHandler touchHandler) {
		// get the action and the pid of the pointer
		Integer pidObject = touchHandler.getPid();
		int pidUP = -1;
		
		switch (touchHandler.getActionCode()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			lastPidPressed.remove(pidObject); // security in case of the point_up don't catch by the previous pressed 
			lastPidPressed.add(pidObject);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			lastPidPressed.remove(pidObject);
			pidUP = touchHandler.getPid();
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
	 * Calculate the modification to apply to the moving
	 * 
	 * @param event the event which contains all the data
	 * @param ignorePid a pid to ignore (pid corresponding to a UP event)
	 */
	public void calculChange(MotionEvent event, int ignorePid) {
		List<Integer> onLeft = new ArrayList<Integer>();
		List<Integer> onRight = new ArrayList<Integer>();
		for (int i = 0; i < event.getPointerCount(); i++) {
			// the pid up is ignored if exist
			if (i == ignorePid) continue;
			
			int x = MoveUtil.normTouchX(event.getX(i));
			int y = MoveUtil.normTouchY(event.getY(i));
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
	public boolean isHorizontalMoving() {
		return isLeftEnabled || isRightEnabled;
	}
	
	/**
	 * change the speed of the player
	 * @param factor
	 */
	public void changeSpeed(float factor) {
		moveImpl.modifySpeedPlayer(factor);
	}
	
	/**
	 * Reset the default speed
	 */
	public void reinitializeSpeed() {
		moveImpl.init();
	}
}

