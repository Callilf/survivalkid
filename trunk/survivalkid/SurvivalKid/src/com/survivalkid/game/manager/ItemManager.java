package com.survivalkid.game.manager;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

import com.survivalkid.game.entity.GameEntity;
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
		//Clean the list by removing all the dead items
		for(ItemEntity item : itemList) {
			if(item.isDead()) {
				itemList.remove(item);
			}
		}
		
		for(ItemEntity item : itemList) {
			item.update(gameTime);
		}
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
	
	@Override
	public void addEntity(GameEntity ge) {
		// TODO Auto-generated method stub
		itemList.add((ItemEntity)ge);
	}

}
