package com.survivalkid;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.survivalkid.game.GameManager;
import com.survivalkid.game.StartMenu;
import com.survivalkid.game.manager.CharacterManager;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.util.CollisionUtil;
import com.survivalkid.game.util.TimerUtil;

public class MainActivity extends Activity {

	/** TAG for the logs. */
	private static final String TAG = MainActivity.class.getSimpleName();

	private StartMenu menu;
	private GameManager gamePanel;
	private MediaPlayer backgroundMusic;

	private boolean inMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// Set fullscreen and remove the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		menu = new StartMenu(this);
		gamePanel = new GameManager(this);
		backgroundMusic = MediaPlayer.create(this, R.raw.for_the_children);

		setContentView(menu);
		inMenu = true;
        
		Log.d(TAG, "View added");
	}

	public void launchGame(int persoSelected) {
		inMenu = false;

		gamePanel.setPersoSelected(persoSelected);
		setContentView(gamePanel);
		gamePanel.create();

		gamePanel.thread.setRunning(true);
		gamePanel.thread.start();

		backgroundMusic.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "Touch pressed : " + keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;
		case KeyEvent.KEYCODE_MENU:
			// if (!gamePanel.restart()) {
			// if (CollisionUtil.hideShowDisplayHitBoxes()) {
			// CharacterManager.OWN_PERSO = (CharacterManager.OWN_PERSO+1)%2;
			// }
			// }
		default:
			break;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!inMenu) {
			gamePanel.getThread().setPause(true);
		}

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.menu, menu);
		return true;
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (!inMenu) {
			gamePanel.getThread().setPause(true);
		}
		return true;
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		if (!inMenu) {
			gamePanel.getThread().setPause(false);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.m_option:
			return true;
		case R.id.m_left:
			finish();
			break;
		case R.id.m_hitbox:
			CollisionUtil.hideShowDisplayHitBoxes();
			break;
		case R.id.m_switchchar:
			int nbPlayer = gamePanel.getNbPlayer();
			if (nbPlayer > 0) {
				CharacterManager.OWN_PERSO = (CharacterManager.OWN_PERSO + 1) % nbPlayer;
			}
			break;
		case R.id.m_restart:
			Log.d(TAG, "Test");
			gamePanel.restart();
			break;
		case R.id.m_timer:
			TimerUtil.logAll();
			break;
		case R.id.m_test1:
			Log.d(TAG, "HighScore : " + GameContext.getSingleton().getDataSave().toString());
			break;
		case R.id.m_test2:
			// TO CODE, FOR TESTING
			break;
		default:
			// return false;
		}

		gamePanel.getThread().setPause(false);
		return true;
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "Destroying...");
		gamePanel.leaveGame();
		backgroundMusic.release();
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

		if (inMenu) {
			menu.stop();
		} else {
			gamePanel.stop();
			backgroundMusic.pause();
		}
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "Resuming...");
		super.onResume();

		// menu.unpause();
		if (!inMenu) {
			backgroundMusic.start();
			gamePanel.unpause();
		} else {
			menu.unpause();
		}
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
	
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	Log.d(TAG, "SavingInstanceState...");
    	super.onSaveInstanceState(outState);
    }

}
