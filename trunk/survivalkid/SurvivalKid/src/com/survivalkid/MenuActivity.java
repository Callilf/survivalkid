package com.survivalkid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.survivalkid.game.StartMenu;
import com.survivalkid.game.util.PrefsUtil;

public class MenuActivity extends Activity {

	/** TAG for the logs. */
	private static final String TAG = MenuActivity.class.getSimpleName();

	private StartMenu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// Set fullscreen and remove the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		PrefsUtil.initPreferences(this);
		menu = new StartMenu(this);
		setContentView(menu);
		
		Log.d(TAG, "View added");
	}
	
	public void launchGame(int persoSelected) {
		Intent intent = new Intent(MenuActivity.this
				.getBaseContext(), GameActivity.class);
		
		Bundle b = new Bundle();
		b.putInt("selectedCharacter", persoSelected);
		intent.putExtras(b);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "Touch pressed : " + keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;
		case KeyEvent.KEYCODE_MENU:

		default:
			break;
		}
		return false;
	}


	@Override
	protected void onDestroy() {
		Log.d(TAG, "Destroying...");
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "Stopping...");
		super.onStop();
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "Pausing...");
		super.onPause();

		menu.stop();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "Resuming...");
		super.onResume();

		menu.unpause();
	}

	@Override
	protected void onRestart() {
		Log.d(TAG, "Restarting...");
		super.onResume();
	}

	@Override
	protected void onUserLeaveHint() {
		Log.d(TAG, "User leaving hint...");
		super.onUserLeaveHint();
	}

}
