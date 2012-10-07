package com.survivalkid.game.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.conn.MultihomePlainSocketFactory;

import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.move.AbstractMove;
import com.survivalkid.game.move.MoveImplSideScreen;
import com.survivalkid.game.singleton.GameContext;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.location.Address;
import android.util.Log;
import android.util.SparseArray;
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
	
	private static List<Integer> lastPidPressed;
	public static boolean lastEnabledLeft = true;
	
	private static boolean isLeftEnabled = false;
	private static boolean isRightEnabled = false;
	private static boolean isTopEnabled = false;
	
	static {
		Point size = new Point();
		display.getSize(size);
		MAX_X = size.x;
		MAX_Y = size.y;
		lastPidPressed = new ArrayList<Integer>();
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
	public synchronized static void calculMove(MotionEvent event) {
		// get the action and the pid of the pointer
		int action =  event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		int pid = action >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		int pidUP = -1;
		
		switch (actionCode) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			lastPidPressed.add(pid);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			lastPidPressed.remove((Integer)pid);
			pidUP = pid;
			break;
		default:
			break;
		}

		isLeftEnabled = false;
		isRightEnabled = false;
		isTopEnabled = false;
		calculChange(event, pidUP);

	}
	
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
	}
	

	public static void calculChange(MotionEvent event, int pid) {
		List<Integer> onLeft = new ArrayList<Integer>();
		List<Integer> onRight = new ArrayList<Integer>();
		for (int i = 0; i < event.getPointerCount(); i++) {
			// the pid up is ignored if exist
			if (i == pid) continue;
			
			int x = (int) event.getX(i);
			int y = (int) event.getY(i);
			// if the pointer has moved or not have previous position
			if (moveImpl.isOnLeft(x, y)) {
				onLeft.add(i);
				isLeftEnabled = true;
			}
			if (moveImpl.isOnRight(x, y)) {
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

	
	public static boolean isHorizontalMoving() {
		return isLeftEnabled || isRightEnabled;
	}


	
}
