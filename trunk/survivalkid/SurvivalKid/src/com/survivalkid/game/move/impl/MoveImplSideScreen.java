package com.survivalkid.game.move.impl;

import com.survivalkid.game.move.AbstractMove;
import com.survivalkid.game.util.MoveUtil;

public class MoveImplSideScreen extends AbstractMove {

	
	
	@Override
	public boolean isOnLeft(int x, int y) {
		// for test if the 20% left of the screen is touch
		return x<MoveUtil.SCREEN_WIDTH/5;
	}
	@Override	
	public boolean isOnRight(int x, int y) {
		// for test if the 20% right of the screen is touch
		return x>MoveUtil.SCREEN_WIDTH/5*4;
	}
	@Override	
	public boolean isOnTop(int x, int y) {
		return y<MoveUtil.GROUND/2;
	}

}
