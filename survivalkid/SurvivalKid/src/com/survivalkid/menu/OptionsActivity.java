package com.survivalkid.menu;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.survivalkid.AbstractActivity;
import com.survivalkid.R;
import com.survivalkid.game.core.Constants;
import com.survivalkid.game.core.Constants.PreferencesConstants;
import com.survivalkid.game.util.MoveUtil;
import com.survivalkid.game.util.PrefsUtil;

public class OptionsActivity extends AbstractActivity {
	
	/** TAG for the logs. */
	private static final String TAG = OptionsActivity.class.getSimpleName();

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTagParent("Options");
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		
        setContentView(R.layout.activity_options);
        
        //ACTIVATE SOUND
        RadioGroup soundRadioGroup = (RadioGroup) findViewById(R.id.radioSound);
        boolean isCurrentSoundOn = PrefsUtil.getPrefs().getBoolean(PreferencesConstants.SOUND_ENABLED, true);
        setChecked(soundRadioGroup, isCurrentSoundOn? 0:1);
        soundRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            	boolean isSoundOn = checkedId == R.id.radioSoundOn;
            	PrefsUtil.setPrefs(boolean.class, PreferencesConstants.SOUND_ENABLED, isSoundOn);
            }
        });
        
        //CENTERED OR SCALE UP THE SCREEN
        RadioGroup imageRadioGroup = (RadioGroup) findViewById(R.id.radioImage);    
        if (MoveUtil.isScalingPossible()) {
	        boolean isCurrentImageCentered = PrefsUtil.getPrefs().getBoolean(PreferencesConstants.RESCALING_ENABLED, true);
	        setChecked(imageRadioGroup, isCurrentImageCentered? 1:0);
	        imageRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	        {
	            public void onCheckedChanged(RadioGroup group, int checkedId) {
	            	boolean isImageCentered = checkedId == R.id.radioImageCentered;
	                PrefsUtil.setPrefs(boolean.class, PreferencesConstants.RESCALING_ENABLED, !isImageCentered);
	            }
	        });
        }
        else {
        	// could be change by  imageRadioGroup.setVisibility(View.GONE) but the label image stay
        	imageRadioGroup.getChildAt(1).setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_options, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (Constants.DEBUG) {
			Log.d(TAG, "Touch pressed : " + keyCode);
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			this.overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
			break;
		case KeyEvent.KEYCODE_MENU:

		default:
			break;
		}
		return false;
	}
	
	private void setChecked(RadioGroup radioGroup, int position) {
		RadioButton radioButton = (RadioButton) radioGroup.getChildAt(position);
		radioButton.setChecked(true);
	}

}
