package com.survivalkid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.survivalkid.game.EndMenu;
import com.survivalkid.game.singleton.GameContext;

public class EndActivity extends AbstractActivity {

	/** TAG for the logs. */
	private static final String TAG = EndActivity.class.getSimpleName();

	private GameActivity parent;
	private EndMenu menu;
	
	/** Whether this menu is open because the game is over (true) or just paused (false). */
	private boolean endMode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle b = getIntent().getExtras();
		endMode = b.getBoolean("endMode");
		int selectedCharacter = b.getInt("selectedCharacter");
		
		parent = (GameActivity) GameContext.getSingleton().getContext();

		setTagParent("End");
		super.onCreate(savedInstanceState);
		
		// Set fullscreen and remove the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		menu = new EndMenu(this, endMode, selectedCharacter);
		setContentView(menu);
		
		WindowManager.LayoutParams params = getWindow().getAttributes();  
		
		params.height = menu.getPanel().getHeight();  
		params.width = menu.getPanel().getWidth();
//		params.x = MoveUtil.SCREEN_WIDTH/2 - params.width/2;
//		params.y = MoveUtil.SCREEN_HEIGHT/2 - params.height/2;

		this.getWindow().setAttributes(params); 
		
		Log.d(TAG, "EndActivity created");
	}
	
	public void returnResult(String result) {
		 Intent returnIntent = new Intent();
		 if(result != null) {
			 returnIntent.putExtra("result", result);
			 setResult(RESULT_OK,returnIntent);
		 } else {
			 setResult(RESULT_CANCELED,returnIntent);
		 }
		 finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(!endMode) {
				//If the game is just pause, unpause if by touching "BACK"
				Intent returnIntent = new Intent();
				setResult(RESULT_CANCELED, returnIntent);
				finish();
			}
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
	
	
	@Override
	protected void onStart() {
		super.onStart();
		parent.getGamePanel().drawBackgroundAndButton();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

}
