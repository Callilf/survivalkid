package com.survivalkid.game.manager;

import static com.survivalkid.game.util.MoveUtil.SCREEN_HEIGHT;
import static com.survivalkid.game.util.MoveUtil.SCREEN_WIDTH;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.survivalkid.R;
import com.survivalkid.game.core.Constants.PersonageConstants;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.util.BitmapUtil;

public class CharacterManager implements IManager {

	public static int OWN_PERSO = 0;

	private int numberOfPlayers;
	private List<Personage> characterList;
	private List<Personage> deadCharacters;
	
	private Bitmap lifeDisplayer;
	private Bitmap lifeDisplayerPart1;
	Rect lifeDisplayerRect1;
	private Bitmap lifeDisplayerPart2;
	Rect lifeDisplayerRect2;
	private Bitmap lifeDisplayerPart3;
	Rect lifeDisplayerRect3;
	Rect lifeDisplayerRect;
	Rect life;
	int sizeLifeBar;

	public CharacterManager() {
		characterList = new ArrayList<Personage>();
		deadCharacters = new ArrayList<Personage>();
		numberOfPlayers = 0;
	}

	public void create() {
		characterList = new ArrayList<Personage>();
	}

	public void update(long gameDuration) {
		for (Personage perso : characterList) {
			if (perso.isDead()) {
				deadCharacters.add(perso);
			} else {
				perso.updateTimed(gameDuration);
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
		for (Personage perso : characterList) {
			perso.drawTimed(canvas);
		}

		if (characterList.size() > OWN_PERSO) {
			// compute the size of the life of the perso
			float pcLife = characterList.get(OWN_PERSO).getLife().getCurrentPcLife();
			int newSizeBarLife = (int) (pcLife * sizeLifeBar);
			life.right = life.left + newSizeBarLife;
			final Paint paint = new Paint();
			paint.setARGB(255, 200, 20, 20);
			canvas.drawRect(life, paint);
			
			//Display the life decoration (above the life bar...)
			if(lifeDisplayer != null) {
				//TEMP for yuna
				canvas.drawBitmap(lifeDisplayer, lifeDisplayerRect.left, lifeDisplayerRect.top, null);
			} else {
				canvas.drawBitmap(lifeDisplayerPart1, lifeDisplayerRect1.left, lifeDisplayerRect1.top, null);
				canvas.drawBitmap(lifeDisplayerPart2, lifeDisplayerRect2.left, lifeDisplayerRect2.top, null);
				canvas.drawBitmap(lifeDisplayerPart3, lifeDisplayerRect3.left, lifeDisplayerRect3.top, null);
			}
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
		
		//Placement of the life bar and the decoration around it *****************
		if(characterList.get(OWN_PERSO).getPersoType() == PersonageConstants.PERSO_YUGO) {
			lifeDisplayerPart1 = BitmapUtil.createBitmap(R.drawable.yugo_life_bar_part1);
		} else if(characterList.get(OWN_PERSO).getPersoType() == PersonageConstants.PERSO_YUNA) {
			lifeDisplayerPart1 = BitmapUtil.createBitmap(R.drawable.yuna_life_bar_part1);
		}
		
		int leftDisp = (int) (SCREEN_WIDTH * 0.33f);
		lifeDisplayerRect1 = new Rect(leftDisp, SCREEN_HEIGHT - 40, leftDisp + lifeDisplayerPart1.getWidth(), SCREEN_HEIGHT - 40 + lifeDisplayerPart1.getHeight());
		lifeDisplayerPart2 = BitmapUtil.createBitmap(R.drawable.yugo_life_bar_part2);
		lifeDisplayerRect2 = new Rect(lifeDisplayerRect1.right, SCREEN_HEIGHT - 40, lifeDisplayerRect1.right + lifeDisplayerPart2.getWidth(),  SCREEN_HEIGHT - 40 + lifeDisplayerPart2.getHeight());
		lifeDisplayerPart3 = BitmapUtil.createBitmap(R.drawable.yugo_life_bar_part3);
		lifeDisplayerRect3 = new Rect(lifeDisplayerRect2.right, SCREEN_HEIGHT - 40, lifeDisplayerRect2.right + lifeDisplayerPart3.getWidth(),  SCREEN_HEIGHT - 40 + lifeDisplayerPart3.getHeight());
		
		int left = lifeDisplayerRect2.left;
		int right = lifeDisplayerRect3.right - 5;
		sizeLifeBar = right - left;
		life = new Rect(left, (int) (lifeDisplayerRect2.top + lifeDisplayerPart2.getHeight() * 0.1f) , right, (int) (lifeDisplayerRect2.bottom - lifeDisplayerPart2.getHeight() * 0.1f));
		//************************************************************************
	}
}
