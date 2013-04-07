package com.survivalkid.menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.survivalkid.AbstractActivity;
import com.survivalkid.R;
import com.survivalkid.game.MainMenu;
import com.survivalkid.game.core.Constants;
import com.survivalkid.game.util.PrefsUtil;

public class MainMenuActivity extends AbstractActivity {

	/** TAG for the logs. */
	private static final String TAG = MainMenuActivity.class.getSimpleName();

	private MainMenu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		setTagParent("Menu");
		super.onCreate(savedInstanceState);
		// Set fullscreen and remove the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		PrefsUtil.initPreferences(this);
		menu = new MainMenu(this);
		setContentView(menu);

		if (Constants.DEBUG) {
			Log.d(TAG, "View added");
		}
	}

	public void goToCharacterSelect() {
		Intent intent = new Intent(MainMenuActivity.this.getBaseContext(), CharacterSelectMenuActivity.class);
		startActivity(intent);
		this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
	
	public void goToOptions() {
		Intent intent = new Intent(MainMenuActivity.this.getBaseContext(), OptionsActivity.class);
		startActivity(intent);
		this.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
	}
	
	public void goToHowToPlay() {
		Intent intent = new Intent(MainMenuActivity.this.getBaseContext(), HowToPlayActivity.class);
		startActivity(intent);
		this.overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (Constants.DEBUG) {
			Log.d(TAG, "Touch pressed : " + keyCode);
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			exitApplication();
			break;
		case KeyEvent.KEYCODE_MENU:

		default:
			break;
		}
		return false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		menu.stop();
	}

	@Override
	protected void onResume() {
		super.onResume();
		menu.unpause();
	}

}
