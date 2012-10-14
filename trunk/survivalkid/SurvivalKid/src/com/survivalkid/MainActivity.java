package com.survivalkid;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.survivalkid.game.GameManager;
import com.survivalkid.game.manager.CharacterManager;
import com.survivalkid.game.util.CollisionUtil;

public class MainActivity extends Activity {

	/** TAG for the logs. */
	private static final String TAG = MainActivity.class.getSimpleName();

	GameManager gamePanel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		// Set fullscreen and remove the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		gamePanel = new GameManager(this);
		setContentView(gamePanel);
		Log.d(TAG, "View added");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Touch pressed : "+keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			gamePanel.stop();
			break;
		case KeyEvent.KEYCODE_MENU:
			if (!gamePanel.restart()) {
				if (CollisionUtil.hideShowDisplayHitBoxes()) {
					CharacterManager.OWN_PERSO = (CharacterManager.OWN_PERSO+1)%2;
				}
			}
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
	}
}
