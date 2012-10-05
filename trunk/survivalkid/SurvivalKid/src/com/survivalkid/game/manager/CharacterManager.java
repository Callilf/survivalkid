package com.survivalkid.game.manager;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

import com.survivalkid.game.entity.personage.Personage;

public class CharacterManager implements IManager {

	private List<Personage> characterList;
	
	public CharacterManager() {
		characterList = new ArrayList<Personage>();
	}

	public void create() {
		// TODO Auto-generated method stub
		characterList = new ArrayList<Personage>();
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}

	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		for(Personage perso : characterList) {
			perso.draw(canvas);
		}
	}
	
	public List<Personage> getCharacterList() {
		return characterList;
	}
	
	public Personage getCharacterList(int num) {
		return characterList.get(num);
	}
	
	public void addCharacter(Personage perso) {
		characterList.add(perso);
    }
}
