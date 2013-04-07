package com.survivalkid.game.thread;

import android.util.Log;

import com.survivalkid.R;
import com.survivalkid.game.core.Constants;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.util.BitmapUtil;

public class ActiveLoadingThread extends Thread {
	/** Tag for logs. */
	private static final String TAG = ActiveLoadingThread.class.getSimpleName();

	public ActiveLoadingThread() {
		super();
	}



	@Override
	public void run() {
		if (Constants.DEBUG) {
			Log.d(TAG, "Start init enum & other image");
		}
		int nbEnum = SpriteEnum.values().length; // to init the enum
		BitmapUtil.createBitmap(R.drawable.ground, true);
		BitmapUtil.createBitmap(R.drawable.arrow_left, true);
		BitmapUtil.createBitmap(R.drawable.arrow_left_pressed, true);
		BitmapUtil.createBitmap(R.drawable.arrow_right, true);
		BitmapUtil.createBitmap(R.drawable.arrow_right_pressed, true);
		BitmapUtil.createBitmap(R.drawable.arrow_up, true);
		BitmapUtil.createBitmap(R.drawable.arrow_up_pressed, true);
		if (Constants.DEBUG) {
			Log.d(TAG, "End init enum & other image - " + nbEnum);	
		}
	}

}
