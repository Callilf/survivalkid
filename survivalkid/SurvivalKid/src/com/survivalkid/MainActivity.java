package com.survivalkid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.survivalkid.game.GameManager;
import com.survivalkid.game.manager.CharacterManager;
import com.survivalkid.game.util.CollisionUtil;

public class MainActivity extends Activity {

	/** TAG for the logs. */
	private static final String TAG = MainActivity.class.getSimpleName();

	private GameManager gamePanel;

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
//			if (!gamePanel.restart()) {
//				if (CollisionUtil.hideShowDisplayHitBoxes()) {
//					CharacterManager.OWN_PERSO = (CharacterManager.OWN_PERSO+1)%2;
//				}
//			}
		default:
			break;
		}
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		gamePanel.getThread().setPause(true);
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.menu, menu);
		return true;
	}
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		gamePanel.getThread().setPause(true);
		return true;
	}
	
	@Override
	public void onOptionsMenuClosed(Menu menu) {
		gamePanel.getThread().setPause(false);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.m_option:
			return true;
		case R.id.m_left:
			gamePanel.stop();
			break;
		case R.id.m_hitbox:
			CollisionUtil.hideShowDisplayHitBoxes();
			break;
		case R.id.m_switchchar:
			CharacterManager.OWN_PERSO = (CharacterManager.OWN_PERSO+1)%2;
			break;
		case R.id.m_restart:
			Log.d(TAG, "Test");
			gamePanel.restart();
			break;
		default:
			//return false;
		}

		gamePanel.getThread().setPause(false);
		return true;
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
		gamePanel.stop();
	}
	
	@Override
	protected void onResume() {
		Log.d(TAG, "Resuming...");
		super.onResume();
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
