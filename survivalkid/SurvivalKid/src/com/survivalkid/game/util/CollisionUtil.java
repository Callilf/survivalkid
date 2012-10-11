package com.survivalkid.game.util;

import com.survivalkid.game.entity.GameEntity;

public final class CollisionUtil {

	private CollisionUtil(){};
	
	public static boolean Overlaps(GameEntity ge1, GameEntity ge2) {
		return ge1.getHitBox().intersect(ge2.getHitBox());
	}
	
}
