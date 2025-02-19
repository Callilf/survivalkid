package com.survivalkid.game.core;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.survivalkid.AbstractSurfaceView;
import com.survivalkid.R;
import com.survivalkid.game.core.Difficulty.DifficultyEnum;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.singleton.SharedVars;
import com.survivalkid.game.util.BitmapUtil;
import com.survivalkid.game.util.DesignUtil;
import com.survivalkid.game.util.MoveUtil;

/**
 * Manage the draw on the surface
 * 
 * @author Thomas
 *
 */
public class SurfaceHandler {

	private SurfaceHolder surfaceHolder;
	
	private AbstractSurfaceView surfaceView;
	
	private ChronoDisplayer chronoRestore;
	private Bitmap ground;
	
	public SurfaceHandler(AbstractSurfaceView surfaceView) {
		this.surfaceView = surfaceView;
		this.surfaceHolder = surfaceView.getHolder();
		
		this.chronoRestore = new ChronoDisplayer(MoveUtil.BACKGROUND_WIDTH*1/3 + MoveUtil.BACKGROUND_LEFT, 
				MoveUtil.BACKGROUND_HEIGHT/2 + MoveUtil.BACKGROUND_TOP);
		chronoRestore.setSize(60f, true);
		
	}
	
	/**
	 * Set the background image of the level depending of the difficulty.
	 * @param diff the difficulty
	 */
	public void setBackground(Difficulty diff) {
		//Display the correct ground depending on the difficulty
		if ( SharedVars.getSingleton().getDifficulty() != null) {
			if (diff.getDifficulty() == DifficultyEnum.HARD 
					|| diff.getDifficulty() == DifficultyEnum.HARDER
					|| diff.getDifficulty() == DifficultyEnum.HARDEST) {
				ground = BitmapUtil.createBitmap(R.drawable.ground_hell);
			} else {
				ground = BitmapUtil.createBitmap(R.drawable.ground);
			}
		} else {
			ground = BitmapUtil.createBitmap(R.drawable.ground);
		}
	}
	
	/**
	 * Do a draw of the game. If withChrono is true, display a count down in foreground.
	 */
	@SuppressLint("WrongCall")
	public void completeDraw(boolean withChrono) {
		Canvas canvas = null;
		try {
			canvas = this.surfaceHolder.lockCanvas();
			if (canvas != null) {
				synchronized (surfaceHolder) {
					DesignUtil.applyScaleRatio(canvas);
					this.surfaceView.onDraw(canvas);
					if (withChrono) {
						chronoRestore.draw(canvas);
					}
				}
			}
		}
		finally {
			// in case of an exception the surface is not left in
			// an inconsistent state
			if (canvas != null) {
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}	// end finally		
	}
	
	
	public void drawBackgroundAndButton() {
		Canvas canvas = null;
		try {
			canvas = surfaceHolder.lockCanvas();
			if (canvas != null) {
				synchronized (surfaceHolder) {
					DesignUtil.applyScaleRatio(canvas);
					drawBackgroundGame(canvas);
					MoveUtil.virtualBag.draw(canvas);
					drawButton(canvas);
				}
			}
		}
		finally {
			// in case of an exception the surface is not left in
			// an inconsistent state
			if (canvas != null) {
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}	// end finally			
	}
	
	public void drawBackgroundGame(Canvas canvas) {
		DesignUtil.drawBackgroundImage(canvas, ground, false);
	}
	
	public void drawButton(Canvas canvas) {
		if (!GameContext.getSingleton().slideMoveEnabled) {
			MoveUtil.btn_left.draw(canvas);
			MoveUtil.btn_right.draw(canvas);
			MoveUtil.btn_up.draw(canvas);
		}
	}

	public ChronoDisplayer getChronoRestore() {
		return chronoRestore;
	}
}
