package com.survivalkid.game.core;

import android.graphics.Canvas;

public interface DynamicDrawableObject {

	void update(long gameDuration);
	
	void draw(Canvas canvas);
	
	boolean isDead();
}
