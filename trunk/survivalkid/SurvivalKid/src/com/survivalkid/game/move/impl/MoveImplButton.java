package com.survivalkid.game.move.impl;

import com.survivalkid.game.move.AbstractMove;
import com.survivalkid.game.util.MoveUtil;

public class MoveImplButton extends AbstractMove {

	
	
	@Override
	public boolean isOnLeft(int x, int y) {
		return MoveUtil.btn_left.isOnButton(x, y);
	}
	@Override	
	public boolean isOnRight(int x, int y) {
		return MoveUtil.btn_right.isOnButton(x, y);
	}
	@Override	
	public boolean isOnTop(int x, int y) {
		return MoveUtil.btn_up.isOnButton(x, y);
	}

}
