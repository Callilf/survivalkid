package com.survivalkid.game.algo.enemy;

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
	private int firstStep(int _difficulty, int _maxDifficulty) {
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

}