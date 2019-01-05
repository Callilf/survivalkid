/**
 * 
 */
package com.calliltbn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.calliltbn.components.CollideComponent;

/**
 * Singleton used to manager player's inputs.
 * @author Callil
 *
 */
public class InputSingleton {
	
	/** The instance. */
	private static InputSingleton instance;
	
	/** The camera. */
	private OrthographicCamera guicam;
	
	/** The last touched point. */
	private Vector3 touchPoint = new Vector3();
	
	//*****************
	// Mouse / touch
	
	/** Whether the left key has been pressed during this frame. */
    public boolean leftPressed;
    
	/** Whether the right key has been pressed during this frame. */
    public boolean rightPressed;
    
    /** Whether the up key has been pressed this frame. */
    public boolean jumpPressed;

	/** Whether the down key has been pressed this frame. */
	public boolean bagPressed;

	/** If left and right are pressed, priority to the last */
	public boolean leftLastPressed = false;

	// For touch control depending of the position of the screen

	/** Position of the main player */
	private Rectangle playerHitbox;
	private Vector2 shiftToMiddle;

	private int lastPointer;

	/**
	 *  Forbidden constructor since it's a singleton.
	 */
	private InputSingleton() {}
	
	
	/**
	 * Instanciate the InputSingleton with the camera.
	 * @param guicam the game camera
	 */
	public static void createInstance(OrthographicCamera guicam) {
		instance = new InputSingleton();
		instance.guicam = guicam;
		instance.initInputProcessor();
	}
	
	/**
	 * Get the instance.
	 * @return the {@link InputSingleton} instance.
	 */
	public static InputSingleton getInstance() {
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
	
	/**
	 * Get the x location of the touch.
	 * @return the x location of the touch
	 */
	public int getClickX() {
		return (int) touchPoint.x;
	}
	
	/**
	 * Get the y location of the touch.
	 * @return the y location of the touch
	 */
	public int getClickY() {
		return (int) touchPoint.y;
	}
	
	/**
	 * Get the location of the touch.
	 * @return the location of the touch
	 */
	public Vector3 getTouchPoint() {
		return touchPoint;
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

	/*
	private boolean setTouchUpOrDown(int button, boolean touchDown) {
		switch(button) {
			case Input.Buttons.LEFT: return setKeyUpOrDown(Keys.LEFT, touchDown);
			case Input.Buttons.RIGHT: return setKeyUpOrDown(Keys.RIGHT, touchDown);
			case Input.Buttons.FORWARD: return setKeyUpOrDown(Keys.UP, touchDown);
			case Input.Buttons.BACK: return setKeyUpOrDown(Keys.DOWN, touchDown);
			default: return false;
		}
	}*/

	private boolean touchUpOrDown(int screenX, int screenY, boolean touchDown) {
		if (!touchDown) {
			resetEvents();
		}
		else {
			if (screenX < playerHitbox.x + shiftToMiddle.x) {
				leftPressed = true;
				rightPressed = false;
			}
			else {
				rightPressed = true;
				leftPressed = false;
			}
			if (screenY > GameScreen.SCREEN_H / 2) {
				jumpPressed = true;
			}
		}
		return true;
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
				unprojectTouchPoint(screenX, screenY);
				
				if (Input.Buttons.LEFT == button) {
					lastPointer = pointer;
					return touchUpOrDown((int)touchPoint.x, (int)touchPoint.y, true);
				}
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				unprojectTouchPoint(screenX, screenY);

				if (Input.Buttons.LEFT == button) {
					lastPointer = -1;
					return touchUpOrDown((int)touchPoint.x, (int)touchPoint.y, false);
				}
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				unprojectTouchPoint(screenX, screenY);

				if (pointer == lastPointer) {
					return touchUpOrDown((int)touchPoint.x, (int)touchPoint.y, true);
				}
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
	

	/**
	 * Convert screenX and screenY positions into game positions.
	 * I don't know why but the origin of the game is the center of the screen there,
	 * but the hitbox of the player has it's origin on the bottom left corner so I have to
	 * translate again after calling unproject...
	 * @param screenX the screen x pos
	 * @param screenY the screen y pos
	 */
	private void unprojectTouchPoint(int screenX, int screenY) {
		touchPoint.set(screenX, screenY, 0);
		guicam.unproject(touchPoint);
		touchPoint.x = touchPoint.x + GameScreen.SCREEN_W/2;
		touchPoint.y = touchPoint.y + GameScreen.SCREEN_H/2;
	}

	public void initMainPlayerPosition(CollideComponent collideComponent) {
		playerHitbox = collideComponent.getHitbox().getRectangle();
		shiftToMiddle = new Vector2(playerHitbox.width / 2, playerHitbox.height / 2);
	}
}