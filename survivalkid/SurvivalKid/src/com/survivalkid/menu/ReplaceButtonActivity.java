package com.survivalkid.menu;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.survivalkid.AbstractActivity;
import com.survivalkid.R;
import com.survivalkid.game.ReplaceMenu;
import com.survivalkid.game.core.Constants;
import com.survivalkid.game.util.MoveUtil;
import com.survivalkid.game.util.PrefsUtil;


public class ReplaceButtonActivity extends AbstractActivity {

	private static final String TAG = ReplaceButtonActivity.class.getSimpleName();
	
	ReplaceMenu gamePanel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTagParent("ReplaceButton");
		super.onCreate(savedInstanceState);
		
		// Set fullscreen and remove the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// initialize multitouch
		MoveUtil.HAS_MULTITOUCH = getPackageManager().hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH);
		
		PrefsUtil.initPreferences(this);
		MoveUtil.initializeButton();
		gamePanel = new ReplaceMenu(this);
		setContentView(gamePanel);
		
		if (Constants.DEBUG) {
			Log.d(TAG, "Replace menu activity created");
		}
	}
	
	@Override
	public void onBackPressed() {
		gamePanel.terminate();
		finish();				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.activity_replacebutton_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.m_resetbutton:
			MoveUtil.buttonPosition.resetPosition();
			gamePanel.drawScreen();
			break;
		case R.id.m_cancel:
			MoveUtil.buttonPosition.init();
			finish();
			break;
		case R.id.m_valid:
			gamePanel.terminate();
			finish();
			break;
		default:
			// return false;
		}

		return true;
	}
}
