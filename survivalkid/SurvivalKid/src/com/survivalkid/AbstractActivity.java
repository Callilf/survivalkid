package com.survivalkid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.survivalkid.game.core.Constants;

public abstract class AbstractActivity extends Activity {
	
	private static final String TAG = AbstractActivity.class.getSimpleName();
	
	protected static final String RESULT = "result";
	protected static final String RETURN_NOTHING = "nothing";
	
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
		if (Constants.DEBUG) {
			Log.d(TAG, tagParent + " - Destroying...");
		}
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		if (Constants.DEBUG) {
			Log.d(TAG, tagParent + " - Stopping...");
		}
		super.onStop();
	}

	@Override
	protected void onPause() {
		if (Constants.DEBUG) {
			Log.d(TAG, tagParent + " - Pausing...");
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (Constants.DEBUG) {
			Log.d(TAG, tagParent + " - Resuming...");
		}
		super.onResume();
	}

	@Override
	protected void onRestart() {
		if (Constants.DEBUG) {
			Log.d(TAG, tagParent + " - Restarting...");
		}
		super.onRestart();
	}

	@Override
	protected void onUserLeaveHint() {
		if (Constants.DEBUG) {
			Log.d(TAG, tagParent + " - User leaving hint...");
		}
		super.onUserLeaveHint();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (Constants.DEBUG) {
			Log.d(TAG, tagParent + " - SavingInstanceState...");
		}
		super.onSaveInstanceState(outState);
	}
	
	public void setTagParent(String tagParent) {
		this.tagParent = tagParent;
	}

	/**
	 * Return a result and finish the activity
	 * 
	 * @param result the result
	 */
	public void returnResult(String result) {
		 Intent returnIntent = new Intent();
		 if(result != null) {
			 returnIntent.putExtra(RESULT, result);
			 setResult(RESULT_OK,returnIntent);
		 } else {
			 setResult(RESULT_CANCELED,returnIntent);
		 }
		 finish();
	}

}
