package com.survivalkid.game.manager;

import static com.survivalkid.game.util.MoveUtil.SCREEN_VIRTUAL_HEIGHT;
import static com.survivalkid.game.util.MoveUtil.SCREEN_VIRTUAL_WIDTH;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.survivalkid.R;
import com.survivalkid.game.core.Constants.PersonageConstants;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.util.BitmapUtil;
import com.survivalkid.game.util.DesignUtil;

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
			canvas.drawRect(life, DesignUtil.PAINT_LIFE);
			
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
		
		lifeDisplayerPart2 = BitmapUtil.createBitmap(R.drawable.yugo_life_bar_part2);
		lifeDisplayerPart3 = BitmapUtil.createBitmap(R.drawable.yugo_life_bar_part3);
		
		initLifebarRect();
		//************************************************************************
	}
	
	public void initLifebarRect() {
		int leftDisp = (int) (SCREEN_VIRTUAL_WIDTH * 0.33f);
		int bottom = (int) (SCREEN_VIRTUAL_HEIGHT * 0.92f);
		lifeDisplayerRect1 = new Rect(leftDisp, bottom, leftDisp + lifeDisplayerPart1.getWidth(), bottom + lifeDisplayerPart1.getHeight());
		lifeDisplayerRect2 = new Rect(lifeDisplayerRect1.right, bottom, lifeDisplayerRect1.right + lifeDisplayerPart2.getWidth(),  bottom + lifeDisplayerPart2.getHeight());
		lifeDisplayerRect3 = new Rect(lifeDisplayerRect2.right, bottom, lifeDisplayerRect2.right + lifeDisplayerPart3.getWidth(),  bottom + lifeDisplayerPart3.getHeight());
		int left = lifeDisplayerRect2.left;
		int right = lifeDisplayerRect3.right - 5;
		sizeLifeBar = right - left;
		life = new Rect(left, (int) (lifeDisplayerRect2.top + lifeDisplayerPart2.getHeight() * 0.1f) , right, (int) (lifeDisplayerRect2.bottom - lifeDisplayerPart2.getHeight() * 0.1f));

	}
}
