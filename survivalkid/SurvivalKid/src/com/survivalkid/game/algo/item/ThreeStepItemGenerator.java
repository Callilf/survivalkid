package com.survivalkid.game.algo.item;

import java.util.List;
import java.util.Map;

import android.graphics.Point;

import com.survivalkid.game.entity.item.ItemEntity;

public class ThreeStepItemGenerator {

	/**
	 * Première étape de l'algo. En fonction de la difficulté courante, on
	 * génère une difficulté d'ennemi, c'est-à-dire la difficulté qui correspond
	 * à la liste d'ennemi dans laquelle on va piocher dans la suite.
	 * 
	 * @param _difficulty
	 *            la difficult� courante.
	 * @param _maxDifficulty
	 *            la difficult� maximale, celle qui correspond � la cat�gorie
	 *            d'ennmis la plus difficile.
	 * @return
	 */
	private static int firstStep(int _difficulty, int _maxDifficulty) {
		// Create list of probabilities
		int[] probas = new int[_maxDifficulty];
		probas[0] = (int) (1.0 / (Math.pow(2, _difficulty)) * 100);
		for (int i = 1; i < _maxDifficulty; i++) {
			probas[i] = (int) (1.0 / (Math.pow(2, Math.abs(i - _difficulty))) * 100)
					+ probas[i - 1];
		}

		// Now generate a number between 0 and probas[max].
		int gen = (int) (Math.random() * probas[_maxDifficulty - 1]);
		// return the category
		for (int i = 0; i < _maxDifficulty; i++) {
			if (gen < probas[i]) {
				return i;
			}
		}
		// should not happen
		return -1;

	}

	private static Class<? extends ItemEntity> secondStep(int difficulty,
			Map<Integer, List<Class<? extends ItemEntity>>> itemdifficultymap) {
		List<Class<? extends ItemEntity>> items = itemdifficultymap
				.get(Integer.valueOf(difficulty));
		return items.get((int) (Math.random() * (items.size())));
	}

	private static ItemEntity thirdStep(
			Class<? extends ItemEntity> _itemClassToGenerate, Point _playerPosition) {
		ItemEntity newItem = null;
		try {
			newItem = _itemClassToGenerate.newInstance();
			//newItem.initRandomPositionAndSpeed(_playerPosition);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return newItem;
	}

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
	public static ItemEntity generateRandomItem(
			int _currentDifficulty,
			int _maxDifficulty,
			Map<Integer, List<Class<? extends ItemEntity>>> itemdifficultymap,
			Point _playerPosition) {
		int difficulty = firstStep(_currentDifficulty, _maxDifficulty);
		Class<? extends ItemEntity> itemClassToGenerate = secondStep(difficulty,
				itemdifficultymap);
		return thirdStep(itemClassToGenerate, _playerPosition);
	}

}