package com.survivalkid.game.util;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;

public final class TimerUtil {

	public static final boolean TIMER_ACTIVE = true;
	
	private static final String TAG = TimerUtil.class.getSimpleName();
	
	private static class TimeCoef {
		
		private long lastInit;
		private long duration;
		private int maxDuration;
		private int minDuration;
		private int nbTime;
		
		public TimeCoef() {
			duration = 0;
			nbTime = 0;
			maxDuration = 0;
			minDuration = Integer.MAX_VALUE;
		}
		
		public void update() {
			long timePassed = System.currentTimeMillis() - lastInit;
			duration += timePassed;
			int durInt = (int) timePassed;
			maxDuration = Math.max(maxDuration, durInt);
			minDuration = Math.min(minDuration, durInt);
			nbTime++;
		}
		
		public void logDuration() {
			StringBuilder sb = new StringBuilder();
			sb.append("For ").append(nbTime).append(" time use : ").append("total=").append(duration)
				.append("ms, moyenne=").append(duration/(float)nbTime).append("ms, min=")
				.append(minDuration).append("ms, max=").append(maxDuration).append("ms");
			Log.i(TAG, sb.toString());
		}
	}
	
	private TimerUtil() {}
	
	private static Map<String, TimeCoef> mapTime = new HashMap<String, TimeCoef>();
	
	/**
	 * Call to initialize the time
	 */
	public static void init(String object) {
		 TimeCoef calc = mapTime.get(object);
		 if (calc == null) {
			 calc = new TimeCoef();
			 mapTime.put(object, calc);
		 }
		 calc.lastInit = System.currentTimeMillis();
	}
	
	/**
	 * Call to add a duration to a type of update
	 * 
	 * @param object string that define the type of update (e.g. : caterpillar)
	 */
	public static void end(String object) {
		 mapTime.get(object).update();
	}
	
	/**
	 * Display log duration for the object
	 * 
	 * @param object the object
	 */
	public static void log(String object) {
		Log.i(TAG, "Duration for "+object);
		mapTime.get(object).logDuration();
	}
	
	public static void logAll() {
		Log.i(TAG, "LOG ALL DURATION");
		for (Entry<String, TimeCoef> entry : mapTime.entrySet()) {
			Log.i(TAG, "Duration for " + entry.getKey());
			entry.getValue().logDuration();
		}
		Log.i(TAG, "END LOG ALL DURATION");
	}
	
}
