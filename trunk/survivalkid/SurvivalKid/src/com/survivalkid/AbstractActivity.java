package com.survivalkid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public abstract class AbstractActivity extends Activity {
	
	private static final String TAG = AbstractActivity.class.getSimpleName();
	
	private String tagParent;

	/**
	 * Kill the application
	 */
	protected void exitApplication() {
		finish();
		System.gc();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	@Override
	protected void onDestroy() {
		Log.d(TAG, tagParent + " - Destroying...");
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, tagParent + " - Stopping...");
		super.onStop();
	}

	@Override
	protected void onPause() {
		Log.d(TAG, tagParent + " - Pausing...");
		super.onPause();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, tagParent + " - Resuming...");
		super.onResume();
	}

	@Override
	protected void onRestart() {
		Log.d(TAG, tagParent + " - Restarting...");
		super.onRestart();
	}

	@Override
	protected void onUserLeaveHint() {
		Log.d(TAG, tagParent + " - User leaving hint...");
		super.onUserLeaveHint();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, tagParent + " - SavingInstanceState...");
		super.onSaveInstanceState(outState);
	}
	
	public void setTagParent(String tagParent) {
		this.tagParent = tagParent;
	}

}
