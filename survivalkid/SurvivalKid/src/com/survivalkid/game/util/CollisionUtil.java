package com.survivalkid.game.util;

import com.survivalkid.game.entity.GameEntity;

public final class CollisionUtil {

	private CollisionUtil(){};
	
	// USE FOR DEBUGGING
	public static boolean displayHitBoxes = false;
	
	public static boolean Overlaps(GameEntity ge1, GameEntity ge2) {
		return ge1.getHitBox().intersect(ge2.getHitBox());
	}
	

	/** Hide or show the hit boxes */
	public static boolean hideShowDisplayHitBoxes() {
		return (displayHitBoxes = !displayHitBoxes);
	}
	
}
