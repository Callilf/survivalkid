package com.survivalkid.game.core;

import android.view.MotionEvent;

import com.survivalkid.game.util.MoveUtil;

/**
 * Simplify the processing of the MotionEvent. Initialize the coordinate only when used
 * 
 * @author Thomas
 */
public class TouchHandler {

	private int pid;
	private int actionCode;
	private int x;
	private int y;	
	
	private MotionEvent event;
	private boolean coordNotSet;
	
	/**
	 * Create a new TouchHandler with lazy init of the coordinate
	 * 
	 * @param event the MotionEvent
	 */
	public TouchHandler(MotionEvent event) {
		this(event, false);
	}

	/**
	 * Create a new TouchHandler with possibilities to force init of the coordinate
	 * 
	 * @param event the MotionEvent
	 */
	public TouchHandler(MotionEvent event, boolean initCoordinate) {
		int action =  event.getAction();
		actionCode = action & MotionEvent.ACTION_MASK;
		pid = action >> MoveUtil.ACTION_POINTER_INDEX_SHIFT;
		if (initCoordinate) {
			x = MoveUtil.normTouchX(event.getX(pid));
			y = MoveUtil.normTouchY(event.getY(pid));	
			coordNotSet = false;
		}
		else {
			this.event = event;
			coordNotSet = true;
		}
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getActionCode() {
		return actionCode;
	}

	public void setActionCode(int actionCode) {
		this.actionCode = actionCode;
	}

	public int getX() {
		if (coordNotSet) {
			x = MoveUtil.normTouchX(event.getX(pid));
			y = MoveUtil.normTouchY(event.getY(pid));	
			coordNotSet = false;
		}
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		if (coordNotSet) {
			x = MoveUtil.normTouchX(event.getX(pid));
			y = MoveUtil.normTouchY(event.getY(pid));	
			coordNotSet = false;
		}
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
