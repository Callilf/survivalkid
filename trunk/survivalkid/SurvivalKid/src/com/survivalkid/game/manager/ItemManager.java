package com.survivalkid.game.manager;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.survivalkid.game.algo.item.BasicItemGenerator;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.item.ItemEntity;
import com.survivalkid.game.entity.item.impl.Medkit;
import com.survivalkid.game.util.TimerUtil;

public class ItemManager extends ObjectManager {

	private List<ItemEntity> itemList;
	private List<ItemEntity> deadItems;

	/**
	 * Counter used to see when to generate.
	 */
	private long generationCounter;
	/**
	 * Generation frequency in ms.
	 */
	private long generationFrequency = 2000;

	private static final List<Class<? extends ItemEntity>> itemMap;

	static {
		itemMap = new ArrayList<Class<? extends ItemEntity>>();
		itemMap.add(Medkit.class);
	}

	public ItemManager() {
		itemList = new ArrayList<ItemEntity>();
		deadItems = new ArrayList<ItemEntity>();
	}

	public void create() {
		// TODO Auto-generated method stub

	}

	
	//*******************
	// UPDATE
	//*******************	
	public void update(long gameTime) {
		// Do we have to generate?
		if (gameTime - generationCounter >= generationFrequency) {
			generationCounter = gameTime;
			generateTimed();
		}

		for (ItemEntity item : itemList) {
			if (item.isDead()) {
				// Clean the list by removing all the dead items
				deadItems.add(item);
			} else {
				item.updateTimed(gameTime);
			}
		}

		// remove the dead items from the list so that they are removed from the
		// game
		if (deadItems.size() > 0) {
			for (ItemEntity item : deadItems) {
				itemList.remove(item);
			}
			deadItems.clear();
		}
	}

	
	
	//*******************
	// DRAW
	//*******************	
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		for (ItemEntity item : itemList) {
			item.drawTimed(canvas);
		}
	}

	
	
	
	//*******************
	// GENERATION FUNCTIONS
	//*******************	
	private void generateTimed() {
		if (!TimerUtil.TIMER_ACTIVE) {
			generate();
			return;
		}
		TimerUtil.start("item generation");
		generate();
		TimerUtil.end("item generation");
	}

	@Override
	public void generate() {
		ItemEntity newItemEntity = BasicItemGenerator.generateRandomItem(itemMap);
		if (newItemEntity != null) {
			itemList.add(newItemEntity);
		}
	}

	
	
	//*******************
	// CHECK BALLOON TOUCHBOX
	//*******************	
	public void checkBalloonTouchBox(MotionEvent event) {
		for(ItemEntity item : itemList) {
			if (item.isInBalloon()) {
				for (int i = 0; i < event.getPointerCount(); i++) {
					int x = (int) event.getX(i);
					int y = (int) event.getY(i);
					
					//If the balloon is touched
					if(item.getBalloon().getBalloonTouchBox().contains(x, y)) {
						item.balloonTouched();
					}
					
				}
			}
		}
	}
	
	
	
	@Override
	public void addEntity(GameEntity ge) {
		itemList.add((ItemEntity) ge);
	}

	/**
	 * @return the itemList
	 */
	public List<ItemEntity> getItemList() {
		return itemList;
	}

	/**
	 * @param itemList
	 *            the itemList to set
	 */
	public void setItemList(List<ItemEntity> itemList) {
		this.itemList = itemList;
	}

}
