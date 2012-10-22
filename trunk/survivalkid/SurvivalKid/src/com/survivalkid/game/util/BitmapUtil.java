package com.survivalkid.game.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public final class BitmapUtil {	
	private static Resources resource;

	private BitmapUtil(){}
	
	public static void initialize(Resources r) {
		resource = r;
	}

	
	/**
	 * Create a bitmap based on the id.
	 * @param id the id from R.java
	 * @return the bitmap created
	 */
	public static Bitmap createBitmap(int id) {
		return BitmapFactory.decodeResource(resource, id);
	}
}
