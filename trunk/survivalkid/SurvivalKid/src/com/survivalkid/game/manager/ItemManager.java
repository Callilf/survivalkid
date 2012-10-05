package com.survivalkid.game.manager;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

import com.survivalkid.game.entity.item.ItemEntity;

public class ItemManager extends ObjectManager {

	private List<ItemEntity> itemList;
	
	public ItemManager() {
		itemList = new ArrayList<ItemEntity>();
	}
	
	public void create() {
		// TODO Auto-generated method stub

	}

	public void update(long gameTime) {
		// TODO Auto-generated method stub

	}

	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		for(ItemEntity item : itemList) {
			item.draw(canvas);
		}
	}

	@Override
	public void generate() {
		// TODO Auto-generated method stub

	}

}
