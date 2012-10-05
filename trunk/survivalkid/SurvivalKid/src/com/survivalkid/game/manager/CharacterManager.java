package com.survivalkid.game.manager;

import java.util.List;

import com.survivalkid.game.entity.personage.Personage;

public class CharacterManager implements IManager {

	private List<Personage> characterList;

	public void create() {
		// TODO Auto-generated method stub
		
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}

	public void draw() {
		// TODO Auto-generated method stub
		
	}
	
	public List<Personage> getCharacterList() {
		return characterList;
	}
	
	public Personage getCharacterList(int num) {
		return characterList.get(num);
	}
}
