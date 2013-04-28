package com.survivalkid.game.manager;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

import com.survivalkid.R;
import com.survivalkid.game.core.DynamicDrawableObject;
import com.survivalkid.game.core.InformText;
import com.survivalkid.game.util.DesignUtil;

/**
 * Manage the decor, as the text in front screen
 * 
 * @author Thomas
 */
public class DecorManager implements IManager {

	private List<DynamicDrawableObject> foregroundObject;
	private List<DynamicDrawableObject> backgroundObject;
	private List<DynamicDrawableObject> objectForegroundToAdd;
	private List<DynamicDrawableObject> objectBackgroundToAdd;
	
	private long lastAddInformText;
	private static final int FREQUENCY_ALERT_TIME = 60000;
	
	public DecorManager() {
		lastAddInformText = 0;
		create();
	}
	
	public void create() {
		foregroundObject = new ArrayList<DynamicDrawableObject>();
		backgroundObject = new ArrayList<DynamicDrawableObject>();
		objectForegroundToAdd = new ArrayList<DynamicDrawableObject>();
		objectBackgroundToAdd = new ArrayList<DynamicDrawableObject>();
	}

	public void update(long gameDuration) {
		// add the object
		if (!objectForegroundToAdd.isEmpty()) {
			for (DynamicDrawableObject o : objectForegroundToAdd) {
				foregroundObject.add(o);
			}
			objectForegroundToAdd.clear();
		}
		if (!objectBackgroundToAdd.isEmpty()) {
			for (DynamicDrawableObject o : objectBackgroundToAdd) {
				backgroundObject.add(o);
			}
			objectBackgroundToAdd.clear();
		}
		
		// manage the creation of object
		if (gameDuration - FREQUENCY_ALERT_TIME > lastAddInformText) {
			lastAddInformText += FREQUENCY_ALERT_TIME;
			int quantity = (int) (lastAddInformText/FREQUENCY_ALERT_TIME);
			InformText alertTime = new InformText(quantity + " " + DesignUtil.getQuantityString(R.plurals.minute, quantity), 0.4f);
			alertTime.initInform(10, 500);
			foregroundObject.add(alertTime);
		}
		
		updateObject(gameDuration);
	}
	
	private void updateObject(long gameDuration) {
		List<DynamicDrawableObject> deadObject = new ArrayList<DynamicDrawableObject>();
		for (DynamicDrawableObject object : foregroundObject) {
			object.update(gameDuration);
			if(object.isDead()) {
				deadObject.add(object);
			}
		}
		if (!deadObject.isEmpty()) {
			foregroundObject.removeAll(deadObject);
			deadObject.clear();
		}
		for (DynamicDrawableObject object : backgroundObject) {
			object.update(gameDuration);
			if (object.isDead()) {
				deadObject.add(object);
			}
		}
		if (!deadObject.isEmpty()) {
			backgroundObject.removeAll(deadObject);
			deadObject.clear();
		}		
	}

	public void draw(Canvas canvas) {
		draw(canvas, true);
	}

	public void draw(Canvas canvas, boolean isForeground) {
		List<DynamicDrawableObject> objectToDraw = isForeground? foregroundObject:backgroundObject;
		for (DynamicDrawableObject object : objectToDraw) {
			object.draw(canvas);
		}	
	}
	
	public void addToForeground(DynamicDrawableObject o) {
		objectForegroundToAdd.add(o);
	}
	
	public void addToBackground(DynamicDrawableObject o) {
		objectBackgroundToAdd.add(o);
	}

}
