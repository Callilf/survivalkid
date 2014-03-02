package com.survivalkid.menu;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ScrollView;

import com.survivalkid.AbstractActivity;
import com.survivalkid.R;
import com.survivalkid.game.core.Constants;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.enemy.impl.Bull;
import com.survivalkid.game.entity.enemy.impl.Caterpillar;
import com.survivalkid.game.entity.enemy.impl.CircularSaw;
import com.survivalkid.game.entity.enemy.impl.FireMeteor;
import com.survivalkid.game.entity.enemy.impl.Meteore;
import com.survivalkid.game.entity.item.impl.BalloonCrate;
import com.survivalkid.game.entity.item.impl.Corrida;
import com.survivalkid.game.entity.item.impl.EnemySpeedIncrease;
import com.survivalkid.game.entity.item.impl.EnemySpeedReducer;
import com.survivalkid.game.entity.item.impl.Medkit;
import com.survivalkid.game.entity.item.impl.PlayerSpeedReducer;
import com.survivalkid.game.entity.item.impl.SuperMedkit;
import com.survivalkid.game.util.DesignUtil;

public class HowToPlayActivity extends AbstractActivity {
	
	/** TAG for the logs. */
	private static final String TAG = HowToPlayActivity.class.getSimpleName();

	private ListView enemyList;
	private ListView itemList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTagParent("HowToPlay");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_how_to_play);
        
        enemyList = (ListView) findViewById(R.id.enemies);
        itemList = (ListView) findViewById(R.id.itemsList);
        
        //Fill lists
        List<GameEntity> enemies = new ArrayList<GameEntity>();
        List<GameEntity> items = new ArrayList<GameEntity>();
        
        //Enemies
        enemies.add(new Caterpillar());
        enemies.add(new CircularSaw());
        enemies.add(new Meteore());
        enemies.add(new FireMeteor());
        enemies.add(new Bull());
        
        //Items
        items.add(new BalloonCrate());
        items.add(new Medkit());
        items.add(new SuperMedkit());
        items.add(new PlayerSpeedReducer());
        items.add(new EnemySpeedReducer());
        items.add(new EnemySpeedIncrease());
        items.add(new Corrida());
        
        enemyList.setAdapter(new Item_Adapter(this, enemies));
        itemList.setAdapter(new Item_Adapter(this, items));
        
        DesignUtil.setListViewHeightBasedOnChildren(enemyList);
        DesignUtil.setListViewHeightBasedOnChildren(itemList);
        
        ScrollView sv = (ScrollView)findViewById(R.id.scrollView);
        sv.smoothScrollTo(0,0);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_how_to_play, menu);
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
			this.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
			break;
		case KeyEvent.KEYCODE_MENU:

		default:
			break;
		}
		return false;
	}


}
