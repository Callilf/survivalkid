package com.survivalkid.game.manager;

import static com.survivalkid.game.util.MoveUtil.SCREEN_HEIGHT;
import static com.survivalkid.game.util.MoveUtil.SCREEN_WIDTH;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.survivalkid.game.entity.personage.Personage;

public class CharacterManager implements IManager {

	public static int OWN_PERSO = 0;

	private int numberOfPlayers;
	private List<Personage> characterList;
	private List<Personage> deadCharacters;

	Rect life;
	int sizeLifeBar;

	public CharacterManager() {
		characterList = new ArrayList<Personage>();
		deadCharacters = new ArrayList<Personage>();
		numberOfPlayers = 0;

		int left = (int) (SCREEN_WIDTH * 0.4f);
		int right = (int) (SCREEN_WIDTH * 0.7f);
		sizeLifeBar = right - left;
		life = new Rect(left, SCREEN_HEIGHT - 20, right, SCREEN_HEIGHT - 10);
	}

	public void create() {
		// TODO Auto-generated method stub
		characterList = new ArrayList<Personage>();
	}

	public void update(long gameTime) {
		// TODO Auto-generated method stub

		for (Personage perso : characterList) {
			if (perso.isDead()) {
				deadCharacters.add(perso);
			} else {
				perso.update(gameTime);
			}
		}

		// Remove the dead characters from the list
		if (deadCharacters.size() > 0) {
			for (Personage perso : deadCharacters) {
				characterList.remove(perso);
			}
			deadCharacters.clear();
		}
	}

	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		for (Personage perso : characterList) {
			perso.draw(canvas);
		}

		if (characterList.size() > OWN_PERSO) {
			// compute the size of the life of the perso
			float pcLife = characterList.get(OWN_PERSO).getLife().getCurrentPcLife();
			int newSizeBarLife = (int) (pcLife * sizeLifeBar);
			life.right = life.left + newSizeBarLife;
			final Paint paint = new Paint();
			paint.setARGB(200, 0, 0, 200);
			canvas.drawRect(life, paint);
		}
	}

	public List<Personage> getCharacterList() {
		return characterList;
	}

	public Personage getCharacterList(int num) {
		return characterList.get(num);
	}

	public void addCharacter(Personage perso) {
		perso.setPlayerNumber(numberOfPlayers);
		characterList.add(perso);
		numberOfPlayers++;
	}
}
