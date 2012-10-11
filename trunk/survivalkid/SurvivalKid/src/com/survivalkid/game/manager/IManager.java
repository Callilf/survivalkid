package com.survivalkid.game.manager;

import android.graphics.Canvas;

public interface IManager {

	public void create();
	public void update(long gameTime);
	public void draw(Canvas canvas);
}
