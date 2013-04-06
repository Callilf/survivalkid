package com.survivalkid.game.core;

import static com.survivalkid.game.util.MoveUtil.RESCALING_ACTIVE;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

import com.survivalkid.R;
import com.survivalkid.game.GameManager;
import com.survivalkid.game.util.BitmapUtil;
import com.survivalkid.game.util.MoveUtil;

/**
 * Manage the draw on the surface
 * 
 * @author Thomas
 *
 */
public class SurfaceHandler {

	private SurfaceHolder surfaceHolder;
	
	private GameManager gameManager;
	
	private ChronoDisplayer chronoRestore;
	private Bitmap ground;
	
	public SurfaceHandler(GameManager gameManager) {
		this.gameManager = gameManager;
		this.surfaceHolder = gameManager.getHolder();
		
		this.chronoRestore = new ChronoDisplayer(MoveUtil.BACKGROUND_WIDTH*1/3 + MoveUtil.BACKGROUND_LEFT, 
				MoveUtil.BACKGROUND_HEIGHT/2 + MoveUtil.BACKGROUND_TOP);
		chronoRestore.setSize(60f, true);
		
		ground = BitmapUtil.createBitmap(R.drawable.ground);
		
	}
	
	/**
	 * Do a draw of the game. If withChrono is true, display a count down in foreground.
	 */
	public void completeDraw(boolean withChrono) {
		Canvas canvas = null;
		try {
			canvas = this.surfaceHolder.lockCanvas();
			if (canvas != null) {
				synchronized (surfaceHolder) {
					applyScaleRatio(canvas);
					this.gameManager.onDraw(canvas);
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
					applyScaleRatio(canvas);
					drawBackground(canvas);
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
	
	public void applyScaleRatio(Canvas canvas) {
		if (RESCALING_ACTIVE) {
			canvas.scale(MoveUtil.RATIO_RESCALING_WIDTH, MoveUtil.RATIO_RESCALING_HEIGHT);
		}
	}
	
	public void resetScale(Canvas canvas) {
		if (RESCALING_ACTIVE) {
			canvas.scale(1/MoveUtil.RATIO_RESCALING_WIDTH, 1/MoveUtil.RATIO_RESCALING_HEIGHT);
		}		
	}
	
	public void drawBackground(Canvas canvas) {
		canvas.drawColor(Color.BLUE);
		int left = MoveUtil.BACKGROUND_LEFT;
		int top = MoveUtil.BACKGROUND_TOP;
		
		// put the bottom at the bottom and hide the top
		if (MoveUtil.BACKGROUND_IMAGE_HEIGHT > MoveUtil.BACKGROUND_HEIGHT) {
			top = MoveUtil.BACKGROUND_HEIGHT - MoveUtil.BACKGROUND_IMAGE_HEIGHT;
		}
		
		// center the image in horizontal
		if (MoveUtil.BACKGROUND_IMAGE_WIDTH > MoveUtil.BACKGROUND_WIDTH) {
			left = (MoveUtil.BACKGROUND_WIDTH - MoveUtil.BACKGROUND_IMAGE_WIDTH)/2;
		}
		
		canvas.drawBitmap(ground, left, top, null);		
	}
	
	public void drawButton(Canvas canvas) {
		MoveUtil.btn_left.draw(canvas);
		MoveUtil.btn_right.draw(canvas);
		MoveUtil.btn_up.draw(canvas);		
	}

	public ChronoDisplayer getChronoRestore() {
		return chronoRestore;
	}
}
