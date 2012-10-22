package com.survivalkid.game.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.util.Log;

/**
 * Use : 
 * - call TimerUtil.start(tag);
 * - do your process you want to check
 * - call TimerUtil.end(tag);
 * 
 * To show the result of the timer, call log(tag); or logAll();
 * 
 * @author Thomas
 *
 */
public final class TimerUtil {

	public static final boolean TIMER_ACTIVE = true;
	
	private static final String TAG = TimerUtil.class.getSimpleName();
	
	private static class LocalTimer {
		
		/** Last initialization of the timer */
		private long lastInit;
		/** Total duration of the timer */
		private long duration;
		/** Max duration of one call of the timer */
		private int maxDuration;
		/** Min duration of one call of the timer */
		private int minDuration;
		/** Number of times the timer has been called */
		private int nbTime;
		
		public LocalTimer() {
			duration = 0;
			nbTime = 0;
			maxDuration = 0;
			minDuration = Integer.MAX_VALUE;
		}
		
		/**
		 * After an new initialization of lastInit, terminate the current call of this timer
		 */
		public void update() {
			long timePassed = System.currentTimeMillis() - lastInit;
			duration += timePassed;
			int durInt = (int) timePassed;
			maxDuration = Math.max(maxDuration, durInt);
			minDuration = Math.min(minDuration, durInt);
			nbTime++;
		}
		
		@Override
		public String toString() {
			float moyenne = ((duration*10000)/(long)nbTime)/10000f;
			StringBuilder sb = new StringBuilder();
			sb.append("avg=").append(moyenne).append("ms, tot=").append(duration).append("ms (for ").append(nbTime)
				.append(" calls), min=").append(minDuration).append("ms, max=").append(maxDuration).append("ms");
			return sb.toString();
		}
	}
	
	private TimerUtil() {}
	
	private static Map<String, LocalTimer> mapTime = new TreeMap<String, LocalTimer>();
	
	/**
	 * Call to initialize the time
	 */
	public static void start(String object) {
		 LocalTimer calc = mapTime.get(object);
		 if (calc == null) {
			 calc = new LocalTimer();
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
		Log.i(TAG, "Timer - " + object + " > " + mapTime.get(object));
	}
	
	public static void logAll() {
		Log.i(TAG, "LOG ALL DURATION");
		for (Entry<String, LocalTimer> entry : mapTime.entrySet()) {
			Log.i(TAG, "Timer - " + entry.getKey() + " > " + entry.getValue());
		}
		Log.i(TAG, "END LOG ALL DURATION");
	}
	
	public static void reset() {
		mapTime.clear();
	}
	
}
