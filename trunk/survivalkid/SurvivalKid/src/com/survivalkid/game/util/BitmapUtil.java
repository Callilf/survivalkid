package com.survivalkid.game.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.survivalkid.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public final class BitmapUtil {
	private final static int IMAGE_MAX_SIZE = 200;
	
	private static Resources resource;
	
	public static Bitmap CIRCULAR_SAW;
	public static Bitmap CATERPILLAR;
	public static Bitmap METEORE;
	public static Bitmap FIRE_TRAIL;
	public static Bitmap FIRE_GROUND;
	public static Bitmap SMOKE_WHITE_LARGE;
	public static Bitmap SMOKE_WHITE_SMALL;
	public static Bitmap SMOKE_BROWN_LARGE;

	private BitmapUtil(){}
	
	public static void initialize(Resources r) {
		resource = r;
		CIRCULAR_SAW = createBitmap(R.drawable.enemy_circular_saw);
		CATERPILLAR = createBitmap(R.drawable.caterpillar);
		METEORE = createBitmap(R.drawable.meteor);
		FIRE_TRAIL = createBitmap(R.drawable.fire);
		FIRE_GROUND = createBitmap(R.drawable.fire_ground);
		SMOKE_WHITE_LARGE = createBitmap(R.drawable.dead_anim);
		SMOKE_WHITE_SMALL = createBitmap(R.drawable.dead_anim_small);
		SMOKE_BROWN_LARGE = createBitmap(R.drawable.dead_anim_black);
	}
	
	public static Bitmap decodeFile(File f){
	    Bitmap b = null;
	    try {
	        //Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;

	        FileInputStream fis = new FileInputStream(f);
	        BitmapFactory.decodeStream(fis, null, o);
	        fis.close();

	        int scale = 1;
	        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
	            scale = (int)Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / 
	               (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        }

	        //Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = scale;
	        fis = new FileInputStream(f);
	        b = BitmapFactory.decodeStream(fis, null, o2);
	        fis.close();
	    } catch (IOException e) {
	    }
	    return b;
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
