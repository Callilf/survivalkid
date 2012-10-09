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

	public static int MAX_X;
	public static int MAX_Y;
	
	static {
		Point size = new Point();
		display.getSize(size);
		MAX_X = size.x;
		MAX_Y = size.y - 40;
	}

	private MoveUtil() {
		// static class, the constructor can't be called
	};
	
}
