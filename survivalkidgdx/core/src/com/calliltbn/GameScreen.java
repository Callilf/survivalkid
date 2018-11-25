/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.calliltbn;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.calliltbn.components.PlayerComponent;
import com.calliltbn.components.SpriteComponent;
import com.calliltbn.factory.EntityFactory;
import com.calliltbn.systems.MoveSystem;
import com.calliltbn.systems.PlayerSpeedSystem;
import com.calliltbn.systems.RenderingSystem;
import com.calliltbn.util.Mappers;

public class GameScreen extends ScreenAdapter {

	public enum State {
		GAME_RUNNING, GAME_PAUSE, GAME_OVER;
	}

	// dimensions
	public static final int SCREEN_H = 480;
	public static final int SCREEN_W = 854;

	SurvivalKidGame game;

	public OrthographicCamera guiCam;
	public EntityFactory entityFactory;

	public PooledEngine engine;	
	private State state;
		
	private Entity timeDisplayer;

	public Entity player;

	public GameScreen (SurvivalKidGame game) {
		this.game = game;
		// init singleton
		InputSingleton.getInstance();
		// already running
		state = State.GAME_RUNNING;
		guiCam = new OrthographicCamera(SCREEN_W, SCREEN_H);
		guiCam.position.set(SCREEN_W / 2, SCREEN_H / 2, 0);

		engine = new PooledEngine();
		this.entityFactory = new EntityFactory(this.engine);

		player = entityFactory.createPlayer(new Vector2(100, 40), PlayerComponent.Perso.YUGO);
		entityFactory.createPlayer(new Vector2(300, 250), PlayerComponent.Perso.YUNA);
		entityFactory.createBouncePlayer();

		SpriteComponent spriteComponent = Mappers.getComponent(SpriteComponent.class, player);
		InputSingleton.getInstance().initMainPlayerPosition(spriteComponent);

		engine.addSystem(new PlayerSpeedSystem());
		engine.addSystem(new MoveSystem());
		engine.addSystem(new RenderingSystem(game.batch));
	}

	public void update (float deltaTime) {
		// force deltaTime equivalent to 60 fps
		deltaTime = 1/60f;
		engine.update(deltaTime);

		switch (state) {
		case GAME_RUNNING:
			updateRunning(deltaTime);
			break;
		case GAME_PAUSE:
			updatePause();
			break;
		case GAME_OVER:
			updateGameOver();
			break;
		}
	}

	private void updateRunning (float deltaTime) {
		if (Gdx.input.justTouched()) {
			//
		}
	}


	private void updatePause() {
		// should check if pause is end
		if (Gdx.input.justTouched()) {

		}
	}

	private void updateGameOver () {
		if (Gdx.input.justTouched()) {
			// game.setScreen(new MainMenuScreen(game));
		}
	}

	public void drawUI () {
		switch (state) {
		case GAME_RUNNING:
			presentRunning();
			break;
		case GAME_PAUSE:
			presentPause();
			break;
		case GAME_OVER:
			presentGameOver();
			break;
		}
	}

	private void presentRunning () {
	}
	
	/** Create the entity that displays the current game time.
	private void createTimeDisplayer() {
		//Display time
		timeDisplayer = entityFactory.createText(new Vector3(0,0,100), "Time: ", null);
		TextComponent text = Mappers.textComponent.get(timeDisplayer);
		text.setText("Time: " + GameTimeSingleton.getInstance().getElapsedTime());
		TransformComponent transfo =  Mappers.transfoComponent.get(timeDisplayer);
		transfo.pos.set(300, 100, 100);
	} */

	private void presentPause() {
		System.out.print("Should be already pause");
		//pause();
	}

	private void presentGameOver () {
	}

	@Override
	public void render(float delta) {
		update(delta);
		drawUI();
	}

	@Override
	public void pause () {
		// TODO nothing yet
	}
}