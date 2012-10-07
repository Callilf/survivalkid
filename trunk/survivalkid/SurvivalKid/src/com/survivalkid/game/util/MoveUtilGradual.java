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
public final class MoveUtilGradual {

	private static final Display display = GameContext.getSingleton().getDisplay();
	
	/** Define the implementation of move */
	private static AbstractMove moveImpl = new MoveImplSideScreen();
	
	private static final int MAP_LEFT = 0;
	private static final int MAP_RIGHT = 1;
	private static final int MAP_TOP = 2;

	
	public static final int GRAVITY = moveImpl.GRAVITY;
	public static int MAX_X;
	public static int MAX_Y;
	
	private static SparseArray<List<Integer>> mapKeyPressed;
	private static SparseArray<Point> mapPointerPosition;
	
	private static int isLeftEnabled = 0;
	private static int isRightEnabled = 0;
	private static int isTopEnabled = 0;
	
	/** To check the last direction pressed */
	public static boolean lastEnabledLeft = false;	
	
	static {
		Point size = new Point();
		display.getSize(size);
		MAX_X = size.x;
		MAX_Y = size.y;
		
		mapPointerPosition = new SparseArray<Point>();
		mapKeyPressed = new SparseArray<List<Integer>>();
		mapKeyPressed.put(MAP_LEFT, new ArrayList<Integer>());
		mapKeyPressed.put(MAP_RIGHT, new ArrayList<Integer>());
		mapKeyPressed.put(MAP_TOP, new ArrayList<Integer>());
	}

	private MoveUtilGradual() {
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
		// get the action and the pid of the pointer
		int action =  event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		int pid = action >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		
		int touchX = (int) event.getX(pid);
		int touchY = (int) event.getY(pid);
		
		switch (actionCode) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			actionOnDown(touchX, touchY, pid, true);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			//actionOnUp(touchX, touchY, pid);
			actionOnUp(pid);
			break;
		case MotionEvent.ACTION_MOVE:
			actionOnMove(event, pid);
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
		}
		else if (isRightEnabled > 0) {
			moveImpl.moveToRight(perso);
		}
		if (isTopEnabled > 0) {
			moveImpl.jump(perso);
		}		
	}
	
	/**
	 * Do the action when a new pointer is enabled
	 * 
	 * @param touchX position X of the pointer
	 * @param touchY position Y of the poinrer
	 * @param pid pointer pid
	 * @param isPriority define if the action is priority if another pointer is enabled
	 */
	public static void actionOnDown(int touchX, int touchY, int pid, boolean isPriority) {
		boolean somethingChange = false;
		mapPointerPosition.put(pid, new Point(touchX, touchY));
		if (moveImpl.isOnLeft(touchX, touchY)) { 
			isLeftEnabled=1; 
			if (isPriority) lastEnabledLeft = true; 
			addMapKeyPressed(MAP_LEFT, pid);
			somethingChange=true;
		}
		if (moveImpl.isOnRight(touchX, touchY)) { 
			isRightEnabled=1; 
			if (isPriority) lastEnabledLeft = false; 
			addMapKeyPressed(MAP_RIGHT, pid);
			somethingChange=true;
		}
		if (moveImpl.isOnTop(touchX, touchY)) {
			isTopEnabled=1;
			addMapKeyPressed(MAP_TOP, pid);
			somethingChange=true;
		}
		// BECAUSE OF BUG, HACK TO REINITIALIZE THE MOVE
		if (isPriority && !somethingChange) {
			mapPointerPosition.clear();
			mapKeyPressed.get(MAP_LEFT).clear();
			mapKeyPressed.get(MAP_TOP).clear();
			mapKeyPressed.get(MAP_RIGHT).clear();
			isLeftEnabled = 0;
			isRightEnabled = 0;
			isTopEnabled = 0;
		}
	}
	
	public static void actionOnMove(MotionEvent event, int pid) {
		//boolean test = lastEnabledLeft;
		for (int i = 0; i < event.getPointerCount(); i++) {
			Point oldPosition = mapPointerPosition.get(pid);
			int newX = (int) event.getX(i);
			int newY = (int) event.getY(i);
			// if the pointer has moved or not have previous position
			if (oldPosition == null  || !oldPosition.equals(newX, newY)) {
				// we withdraw potentially the pid from all keyPressed
				actionOnUp(pid);
				// we simulated that the new touch is pressed but without priority
				actionOnDown(newX, newY, pid, false);
			}
		}
		//Log.d("TESTMOVE", "Avant : "+test+" - Après :"+lastEnabledLeft);
	}
	
	public static void actionOnUp(int pid) {
		mapPointerPosition.remove(pid);
		if (removeMapKeyPressed(MAP_LEFT, pid)) isLeftEnabled=0;
		if (removeMapKeyPressed(MAP_RIGHT, pid)) isRightEnabled=0; 
		if (removeMapKeyPressed(MAP_TOP,pid)) isTopEnabled=0; 
	}
	public static void actionOnUp(int touchX, int touchY, int pid) {
		mapPointerPosition.remove(pid);
		if (moveImpl.isOnLeft(touchX, touchY)) { 
			if (removeMapKeyPressed(MAP_LEFT, pid)) isLeftEnabled=0;
		}
		if (moveImpl.isOnRight(touchX, touchY)) { 
			if (removeMapKeyPressed(MAP_RIGHT,pid)) isRightEnabled=0; 
		}
		if (moveImpl.isOnTop(touchX, touchY)) {
			if (removeMapKeyPressed(MAP_TOP,pid)) isTopEnabled=0; 
		}
	}
	
	public static boolean isHorizontalMoving() {
		return isLeftEnabled != 0 || isRightEnabled != 0;
	}
	
	/**
	 * Set that the direction is enabled by the pointer pid
	 * 
	 * @param direction MAP_LEFT, MAP_RIGHT or MAP_TOP
	 * @param pid the pointer pid
	 */
	private static void addMapKeyPressed(int direction, int pid) {
		mapKeyPressed.get(direction).add(pid);
	}
	
	/**
	 * Remove from mapKeyPressed the pointer pid with its direction and return if the direction is not pressed anymore
	 * 
	 * @param direction MAP_LEFT, MAP_RIGHT or MAP_TOP
	 * @param pid the pointer pid
	 * @return true if the list in direction is empty
	 */
	private static boolean removeMapKeyPressed(int direction, Integer pid) {
		List<Integer> list = mapKeyPressed.get(direction);
		list.remove(pid);
		return list.isEmpty();
	}

	
}
