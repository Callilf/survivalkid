/**
 * 
 */
package com.calliltbn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

/**
 * Singleton used to manager player's inputs.
 * @author Callil
 *
 */
public class InputSingleton {
	
	/** The instance. */
	private static InputSingleton instance;
	
	/** Whether the left key has been pressed during this frame. */
    public boolean leftPressed;
    
	/** Whether the right key has been pressed during this frame. */
    public boolean rightPressed;
    
    /** Whether the up key has been pressed this frame. */
    public boolean jumpPressed;

	/** Whether the down key has been pressed this frame. */
	public boolean bagPressed;


	public boolean leftLastPressed = false;

	/**
	 *  Forbidden constructor since it's a singleton.
	 */
	private InputSingleton() {}
	
	/**
	 * Get the instance.
	 * @return the {@link InputSingleton} instance.
	 */
	public static InputSingleton getInstance() {
		if (instance == null) {
			instance = new InputSingleton();
			instance.initInputProcessor();
		}
		
		return instance;
	}
	
	
	/**
	 * Reset all booleans.
	 */
	public void resetEvents() {
		this.leftPressed = false;
		this.rightPressed = false;
		this.jumpPressed = false;
	}
	
	public int getClickX() {
		return Gdx.input.getX();
	}
	public int getClickY() {
		return Gdx.graphics.getHeight() - Gdx.input.getY();
	}

	private boolean setKeyUpOrDown(int keycode, boolean kewDown) {
		switch(keycode) {
			case Keys.UP :
				jumpPressed = kewDown;
				break;
			case Keys.LEFT:
				leftPressed = kewDown;
				if (kewDown) {
					leftLastPressed = true;
				}
				break;
			case Keys.RIGHT:
				rightPressed = kewDown;
				if (kewDown) {
					leftLastPressed = false;
				}
				break;
			case Keys.DOWN:
				bagPressed = kewDown;
				break;
			default:
				return false;
		}
		return true;
	}

	private boolean setTouchUpOrDown(int button, boolean touchDown) {
		switch(button) {
			case Input.Buttons.LEFT: return setKeyUpOrDown(Keys.LEFT, touchDown);
			case Input.Buttons.RIGHT: return setKeyUpOrDown(Keys.RIGHT, touchDown);
			case Input.Buttons.FORWARD: return setKeyUpOrDown(Keys.UP, touchDown);
			case Input.Buttons.BACK: return setKeyUpOrDown(Keys.DOWN, touchDown);
			default: return false;
		}
	}
	
	/**
	 * Initialize the inputProcessor.
	 */
	private void initInputProcessor() {
		Gdx.input.setInputProcessor(new InputProcessor() {

			@Override
			public boolean keyDown(int keycode) {
				return setKeyUpOrDown(keycode, true);
			}

			@Override
			public boolean keyUp(int keycode) {
				return setKeyUpOrDown(keycode, false);
			}

			@Override
			public boolean keyTyped(char character) {
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				return setTouchUpOrDown(button, true);
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				return setTouchUpOrDown(button, false);
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				return false;
			}

        });
	}
	
}