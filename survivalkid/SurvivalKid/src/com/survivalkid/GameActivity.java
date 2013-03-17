package com.survivalkid;

import android.content.Intent;
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
import com.survivalkid.game.manager.CharacterManager;
import com.survivalkid.game.singleton.GameContext;
import com.survivalkid.game.util.CollisionUtil;
import com.survivalkid.game.util.MoveUtil;
import com.survivalkid.game.util.PrefsUtil;
import com.survivalkid.game.util.TimerUtil;

public class GameActivity extends AbstractActivity {

	/** TAG for the logs. */
	private static final String TAG = GameActivity.class.getSimpleName();
	
	private GameManager gamePanel;
	private MediaPlayer backgroundMusic;
	private int selectedCharacter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		Bundle b = getIntent().getExtras();
		selectedCharacter = b.getInt("selectedCharacter");
		setTagParent("Game");

		super.onCreate(savedInstanceState);
		// Set fullscreen and remove the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		PrefsUtil.initPreferences(this);
		gamePanel = new GameManager(this);
		backgroundMusic = MediaPlayer.create(this, R.raw.for_the_children);

		launchGame(selectedCharacter);

		Log.d(TAG, "View added");
	}

	public void launchGame(int persoSelected) {

		gamePanel.setPersoSelected(persoSelected);
		setContentView(gamePanel);
		gamePanel.create();

		gamePanel.getThread().setRunning(true);
		gamePanel.getThread().start();

		backgroundMusic.start();
	}

	public void displayEndMenu() {
		Intent intent = new Intent(GameActivity.this.getBaseContext(), EndActivity.class);

		Bundle b = new Bundle();
		b.putBoolean("endMode", true);
		intent.putExtras(b);
		startActivityForResult(intent, 1);
	}
	
	public void displayPauseMenu() {
		Intent intent = new Intent(GameActivity.this.getBaseContext(), EndActivity.class);
		Bundle b = new Bundle();
		b.putBoolean("endMode", false);
		intent.putExtras(b);
		startActivityForResult(intent, 1);
	}
	
	public void backToMainMenu() {
		Intent intent = new Intent(GameActivity.this.getBaseContext(), MenuActivity.class);
		startActivity(intent);
		finish();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				String result = data.getStringExtra("result");
				
				if ("leave".equals(result)) {
					//Leave game
					exitApplication();
				} else if ("menu".equals(result)) {
					//back to main menu
					backToMainMenu();
				} else if ("restart".equals(result)) {
					//restart
					gamePanel.restart();
				} else {
					Log.e(TAG, "Wrong return code.");
				}
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code on no result return
				// Not used
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "Touch pressed : " + keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			displayPauseMenu();
			//exitApplication();
			break;
		case KeyEvent.KEYCODE_MENU:
			//Menu button
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
		if (!gamePanel.isModeEditLocationButton()) {
			gamePanel.getThread().setPause(false);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.m_option:
			return true;
		case R.id.m_left:
			exitApplication();
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
		case R.id.m_movebutton:
			gamePanel.changeLocationButton();
			break;
		case R.id.m_resetbutton:
			MoveUtil.buttonPosition.resetPosition();
			gamePanel.afterChangeLocationButton();
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

		if (!gamePanel.isModeEditLocationButton()) {
			gamePanel.getThread().setPause(false);
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		gamePanel.leaveGame();
		backgroundMusic.release();
	}

	@Override
	protected void onPause() {
		super.onPause();
		gamePanel.stop();
		backgroundMusic.pause();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		//Afficher le fond ???
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		displayPauseMenu();
	}

	@Override
	protected void onResume() {
		super.onResume();
		backgroundMusic.start();
		gamePanel.unpause();
	}

}
