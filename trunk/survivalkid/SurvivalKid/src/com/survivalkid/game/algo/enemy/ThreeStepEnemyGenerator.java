package com.survivalkid.game.algo.enemy;

public class ThreeStepEnemyGenerator {

	/**
	 * Premi�re �tape de l'algo. En fonction de la difficult� courante, on
	 * g�n�re une difficult� d'ennemi, c'est-�-dire la difficult� qui correspond
	 * � la liste d'ennemi dans laquelle on va piocher dans la suite.
	 * 
	 * @param _difficulty
	 *            la difficult� courante.
	 * @param _maxDifficulty
	 *            la difficult� maximale, celle qui correspond � la cat�gorie
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