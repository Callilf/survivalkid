package com.survivalkid.game.util;

import android.graphics.Rect;

import com.survivalkid.game.entity.GameEntity;

public final class CollisionUtil {

	private CollisionUtil(){};
	
	// USE FOR DEBUGGING
	public static boolean displayHitBoxes = false;
	
	public static boolean Overlaps(GameEntity ge1, GameEntity ge2) {
		ge1.getHitBox();
		return Rect.intersects(ge2.getHitBox(), ge1.getHitBox());
	}
	

	/** Hide or show the hit boxes */
	public static boolean hideShowDisplayHitBoxes() {
		return (displayHitBoxes = !displayHitBoxes);
	}
	
}
