package com.survivalkid.menu;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.survivalkid.AbstractActivity;
import com.survivalkid.R;

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
        soundRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioSoundOn) {
                	//SOUND ON
                	//TODO -> Set to preferences
                } else {
                	//SOUND OFF
                	//TODO -> Set to preferences
                }
            }
        });
        
        //CENTERED OR SCALE UP THE SCREEN
        RadioGroup imageRadioGroup = (RadioGroup) findViewById(R.id.radioImage);        
        imageRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioImageCentered) {
                	//CENTERED
                	//TODO -> Set to preferences
                } else {
                	//SCALE UP
                	//TODO -> Set to preferences
                }
            }
        });
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
		Log.d(TAG, "Touch pressed : " + keyCode);
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

}
