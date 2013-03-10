package com.survivalkid.game.util;

import android.graphics.Rect;

import com.survivalkid.game.core.Constants.PreferencesConstants;
import com.survivalkid.game.entity.GameEntity;

public final class CollisionUtil {

	private CollisionUtil(){};
	
	// USE FOR DEBUGGING
	public static boolean displayHitBoxes = PrefsUtil.getPrefs().getBoolean(PreferencesConstants.DISPLAY_HITBOX, false);
	
	public static boolean Overlaps(GameEntity ge1, GameEntity ge2) {
		ge1.getHitBox();
		return Rect.intersects(ge2.getHitBox(), ge1.getHitBox());
	}
	

	/** Hide or show the hit boxes */
	public static boolean hideShowDisplayHitBoxes() {
		displayHitBoxes = !displayHitBoxes;
		PrefsUtil.setPrefs(boolean.class, PreferencesConstants.DISPLAY_HITBOX, displayHitBoxes);
		return displayHitBoxes;
	}
	
}
