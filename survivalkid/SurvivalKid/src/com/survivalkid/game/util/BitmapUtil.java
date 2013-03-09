package com.survivalkid.game.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

public final class BitmapUtil {	
	private static Resources resource;

	private static SparseArray<Bitmap> cache = new SparseArray<Bitmap>();
	
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
		return createBitmap(id, true);
	}
	
	/**
	 * Create a bitmap based on the id.
	 * @param id the id from R.java
	 * @param putInCache if true, put the result in cache
	 * @return the bitmap created
	 */
	public static Bitmap createBitmap(int id, boolean putInCache) {
		Bitmap b = cache.get(id);
		if (b != null) {
			return b;
		}
		if (putInCache) {
			b = BitmapFactory.decodeResource(resource, id);
			cache.put(id, b);
			return b;
		} else {
			return BitmapFactory.decodeResource(resource, id);
		}
	}
	
}
