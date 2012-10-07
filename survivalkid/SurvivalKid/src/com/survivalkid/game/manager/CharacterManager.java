package com.survivalkid.game.manager;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

import com.survivalkid.game.entity.personage.Personage;

public class CharacterManager implements IManager {

	private int numberOfPlayers;
	private List<Personage> characterList;
	
	public CharacterManager() {
		characterList = new ArrayList<Personage>();
		numberOfPlayers = 0;
	}

	public void create() {
		// TODO Auto-generated method stub
		characterList = new ArrayList<Personage>();
	}

	public void update(long gameTime) {
		// TODO Auto-generated method stub
		for(Personage perso : characterList) {
			perso.update(gameTime);
		}
	}

	public void draw(Canvas canvas, boolean displayHitBox) {
		// TODO Auto-generated method stub
		for(Personage perso : characterList) {
			perso.draw(canvas, displayHitBox);
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
