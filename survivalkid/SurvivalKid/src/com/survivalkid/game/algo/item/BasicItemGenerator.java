package com.survivalkid.game.algo.item;

import java.util.List;

import android.graphics.Point;

import com.survivalkid.game.entity.item.ItemEntity;

public class BasicItemGenerator {

	/**
	 * @param _currentDifficulty
	 *            Current difficulty of the game.
	 * @param _maxDifficulty
	 *            Number of different difficulties available.
	 * @param itemdifficultymap
	 *            The map.
	 * @param _playerPosition
	 * @return
	 */
	public static ItemEntity generateRandomItem(List<Class<? extends ItemEntity>> itemList) {
		
		//1 chance out of 10 to generate an item
		int randomValue = (int) (Math.random()*100);
		if(randomValue < 89) {
			return null;
		}
		
		Class<? extends ItemEntity> itemClassToGenerate = itemList.get((int) (Math.random() * itemList.size()));
		
		ItemEntity newItem = null;
		try {
			newItem = itemClassToGenerate.newInstance();
			
			//If in balloon mode, the ballon will initialize it's own spanw point
			//and the item will spawn at the drop point of the crate
			if(!newItem.isInBalloon()) {
				newItem.initRandomPositionAndSpeed(new Point(0,0));
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return newItem;
	}

}