package com.survivalkid.game.entity.enemy.impl;

import static com.survivalkid.game.util.MoveUtil.normX;
import static com.survivalkid.game.util.MoveUtil.normY;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

import com.survivalkid.game.core.AnimatedSprite;
import com.survivalkid.game.core.Constants.DirectionConstants;
import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.entity.Life.EnumLife;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.util.DesignUtil;
import com.survivalkid.game.util.MoveUtil;

public class FredCircularSaw extends EnemyEntity {
	private static final String TAG = FredCircularSaw.class.getSimpleName();

	private static int STATE_LINE = 0;
	private static int STATE_SAW = 1;
	private static int NB_POINTS_MIN = 5;

	private static int DIAG_UP = 0;
	private static int STRAIGHT = 1;
	private static int DIAG_DOWN = 2;

	private int state;
	private int speed = 2;

	private List<Point> linePoints;
	private List<Integer> directionList;
	private List<Boolean> drawList;
	private int directionIndex;

	// DRAW attributes
	private int drawPointPeriod = 1000;
	private int lastDrawnTime = 0;
	private Point lastDrawnPoint;
	private int lastNumber;
	private int lastPointType;
	private boolean drawSaw;

	// SAW attributes
	private int sawFramesPeriod = 5;
	private int sawFramesCurrent;

	private boolean draw;

	// explosion when the saw touch the ground
	private AnimatedSprite deathAnim;

	/** Default constructor. */
	public FredCircularSaw() {
		super("FredCircularSaw",SpriteEnum.CIRCULAR_SAW, -20, -20, 20, 1);
	}

	/** Initialize the enemy. */
	private void init() {
		state = STATE_LINE;
		draw = true;
		linePoints = new ArrayList<Point>();
		lastDrawnPoint = new Point(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2);
		linePoints.add(new Point(lastDrawnPoint));
		lastNumber = 0;
		directionList = new ArrayList<Integer>();
		drawList = new ArrayList<Boolean>();
		directionIndex = 0;
		sawFramesCurrent = 0;
		drawSaw = false;

		affectedByFloor = false;
		affectedByWalls = false;
		affectedByCeiling = false;

		deathAnim = new AnimatedSprite(SpriteEnum.SMOKE_WHITE_SMALL, 0, 0);

		redefineHitBox((sprite.getWidth() * 12) / 100, (sprite.getHeight() * 12) / 100, (sprite.getWidth() * 80) / 100,
				(sprite.getHeight() * 80) / 100);
		play("rotate", true, true);
	}

	@Override
	public void update(long gameDuration) {
		if (state == STATE_SAW) {
			drawSaw = true;
			if (dying) {
				deathAnim.update(gameDuration, DirectionConstants.RIGHT);
				if (deathAnim.isAnimationFinished()) {
					dead = true;
				}
				return;
			}

			if (sawFramesCurrent == sawFramesPeriod) {
				sawFramesCurrent = 0;
				drawList.set(directionIndex, false);
				directionIndex++;
			}

			if (directionIndex < directionList.size()) {
				if (directionList.get(directionIndex) == DIAG_UP) {
					setSpeedY(speed);
				} else if (directionList.get(directionIndex) == STRAIGHT) {
					setSpeedY(0);
				} else if (directionList.get(directionIndex) == DIAG_DOWN) {
					setSpeedY(-speed);
				}
				sawFramesCurrent++;
			} else {
				die();
			}

			// Destroyed if touches the floor
			if (onFloor()) {
				die();
			}
			
			super.update(gameDuration);
		}

		// DRAW THE LINE
		else if (state == STATE_LINE && draw && gameDuration > lastDrawnTime + drawPointPeriod) {
			Point newPoint = null;

			int newX = lastDrawnPoint.x + sawFramesPeriod*normX(speed);
			int moveY = sawFramesPeriod*normY(speed);
			if (lastNumber > NB_POINTS_MIN) {
				int randomInt = (int) (Math.random() * 100);

				if (randomInt < 32) {
					// Diag up
					newPoint = new Point(newX, lastDrawnPoint.y + moveY);
					linePoints.add(newPoint);
					directionList.add(DIAG_UP);
					drawList.add(true);
					lastDrawnPoint = newPoint;
					lastPointType = DIAG_UP;
				} else if (randomInt < 66) {
					// straight
					newPoint = new Point(newX, lastDrawnPoint.y);
					linePoints.add(newPoint);
					directionList.add(STRAIGHT);
					drawList.add(true);
					lastDrawnPoint = newPoint;
					lastPointType = STRAIGHT;
				} else {
					// Diag down
					newPoint = new Point(newX, lastDrawnPoint.y - moveY);
					linePoints.add(newPoint);
					directionList.add(DIAG_DOWN);
					drawList.add(true);
					lastDrawnPoint = newPoint;
					lastPointType = DIAG_DOWN;
				}
				lastNumber = 1;
			} else {
				if (lastPointType == DIAG_UP) {
					// Diag up
					newPoint = new Point(newX, lastDrawnPoint.y + moveY);
					linePoints.add(newPoint);
					directionList.add(DIAG_UP);
					drawList.add(true);
					lastDrawnPoint = newPoint;
					lastPointType = DIAG_UP;
				} else if (lastPointType == STRAIGHT) {
					// straight
					newPoint = new Point(newX, lastDrawnPoint.y);
					linePoints.add(newPoint);
					directionList.add(STRAIGHT);
					drawList.add(true);
					lastDrawnPoint = newPoint;
					lastPointType = STRAIGHT;
				} else if (lastPointType == DIAG_DOWN) {
					// Diag down
					newPoint = new Point(newX, lastDrawnPoint.y - moveY);
					linePoints.add(newPoint);
					directionList.add(DIAG_DOWN);
					drawList.add(true);
					lastDrawnPoint = newPoint;
					lastPointType = DIAG_DOWN;
				}
				lastNumber++;
			}

			if (newPoint.x > MoveUtil.BACKGROUND_RIGHT + sprite.getWidth() || newPoint.y < MoveUtil.BACKGROUND_TOP - sprite.getHeight()
					|| newPoint.y + sprite.getHeight() / 2 >= MoveUtil.GROUND) {
				draw = false;
				state = STATE_SAW;
			}

			// Log.d(TAG, "Draw point at x=" + lastDrawnPoint.x + ", y=" +
			// lastDrawnPoint.y);
		}
		else {
			Log.d(TAG, "State incorrect : " + state);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (state == STATE_LINE) {
			for (Point p : linePoints) {
				canvas.drawCircle(p.x, p.y, 2, DesignUtil.PAINT_CIRCULARSAW_POINT);
			}
		} else {

			if (dying) {
				deathAnim.draw(canvas, DirectionConstants.RIGHT);
			} else {
				int i = 0;
				for (Point p : linePoints) {
					if (i < drawList.size() && drawList.get(i)) {
						canvas.drawCircle(p.x, p.y, 2, DesignUtil.PAINT_CIRCULARSAW_POINT);
					}
					i++;
				}
				if (drawSaw) {
					super.draw(canvas);
				}
			}
		}
	}

	@Override
	public void applyCollisionCharacter(Personage _personage) {
			_personage.takeDamage(dammage, EnumLife.TAKE_DAMAGE);
	}

	@Override
	public void die() {
		dying = true;

		deathAnim.setX((sprite.getX() + sprite.getWidth() / 2) - deathAnim.getWidth() / 2);
		deathAnim.setY((sprite.getY() + sprite.getHeight() / 2) - deathAnim.getHeight() / 2);
		deathAnim.play("die", false, true);
	}

	/**
	 * Initialize the position, speed and misc. attributes of the enemy.
	 */
	@Override
	public void initRandomPositionAndSpeed(Point playerPosition) {
		// Random spawn position
		int randomY = (int) (MoveUtil.getRandomPositionY(5));
		sprite.setX(MoveUtil.BACKGROUND_LEFT - sprite.getWidth());
		sprite.setY(randomY);

		// init speed
		setSpeedX(speed);
		
		init();
	}
}
