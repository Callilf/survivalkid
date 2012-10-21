package com.survivalkid.game.algo.enemy;

import java.util.List;
import java.util.Map;

import android.graphics.Point;

import com.survivalkid.game.entity.enemy.EnemyEntity;

public class ThreeStepEnemyGenerator {

	/**
	 * Première étape de l'algo. En fonction de la difficulté courante, on
	 * génère une difficulté d'ennemi, c'est-à-dire la difficulté qui correspond
	 * à la liste d'ennemi dans laquelle on va piocher dans la suite.
	 * 
	 * @param _difficulty
	 *            la difficulté courante.
	 * @param _maxDifficulty
	 *            la difficulté maximale, celle qui correspond à la catégorie
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

	private static Class<? extends EnemyEntity> secondStep(int difficulty,
			Map<Integer, List<Class<? extends EnemyEntity>>> enemydifficultymap) {
		List<Class<? extends EnemyEntity>> enemies = enemydifficultymap
				.get(Integer.valueOf(difficulty));
		return enemies.get((int) (Math.random() * (enemies.size())));
	}

	private static EnemyEntity thirdStep(
			Class<? extends EnemyEntity> _enemyClassToGenerate, Point _playerPosition) {
		EnemyEntity newEnemy = null;
		try {
			newEnemy = _enemyClassToGenerate.newInstance();
			newEnemy.initRandomPositionAndSpeed(_playerPosition);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return newEnemy;
	}

	/**
	 * @param _currentDifficulty
	 *            Current difficulty of the game.
	 * @param _maxDifficulty
	 *            Number of different difficulties available.
	 * @param enemydifficultymap
	 *            The map.
	 * @param _playerPosition
	 * @return
	 */
	public static EnemyEntity generateRandomEnemy(
			int _currentDifficulty,
			int _maxDifficulty,
			Map<Integer, List<Class<? extends EnemyEntity>>> enemydifficultymap,
			Point _playerPosition) {
		int difficulty = firstStep(_currentDifficulty, _maxDifficulty);
		Class<? extends EnemyEntity> enemyClassToGenerate = secondStep(difficulty,
				enemydifficultymap);
		return thirdStep(enemyClassToGenerate, _playerPosition);
	}

}