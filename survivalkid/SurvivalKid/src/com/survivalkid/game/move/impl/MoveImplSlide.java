package com.survivalkid.game.move.impl;

import android.util.SparseArray;

import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.move.AbstractMove;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.util.MoveUtil;

/**
 * Allow to move by slide a pointer on the screen
 * 
 * @author ThomAs
 */
public class MoveImplSlide extends AbstractMove {

	private class SlideCheck {
		long initial = 0;
		long current = 0;
	}
	private SparseArray<SlideCheck> checks;

	private Personage perso;
	
	private int thresholdDown = 30;
	private int thresholdUp = 100;
	
	public MoveImplSlide(Personage p) {
		super();
		perso = p;	
		checks = new SparseArray<SlideCheck>();
	}
	
	@Override
	public boolean isOnLeft(int x, int y) {
		return x < perso.getHitBox().left;
	}

	@Override
	public boolean isOnRight(int x, int y) {
		return x > perso.getHitBox().right;
	}

	@Override
	public boolean isOnTop(int x, int y) {
		return y < MoveUtil.GROUND - MoveUtil.SCREEN_VIRTUAL_HEIGHT/4 
				&& y < perso.getHitBox().top + MoveUtil.SCREEN_VIRTUAL_HEIGHT/4;
	}
	
	@Override
	public boolean keepPid(int pid) {
		long duration = GameContext.getSingleton().gameDuration;
		SlideCheck check = checks.get(pid);
		if (check == null) {
			check = new SlideCheck();
			check.initial = duration;
			check.current = duration;
			checks.put(pid, check);
			return false;
		}
		if (duration - check.current > thresholdUp) {
			// init
			check.initial = duration;
		}
		check.current = duration;
		return (check.current - check.initial) > thresholdDown;
	}

}
