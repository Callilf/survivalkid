package com.survivalkid.game.entity.enemy.impl;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.survivalkid.R;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.personage.Personage;
import com.survivalkid.game.util.BitmapUtil;
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

	// SAW attributes
	private int sawFramesPeriod = 5;
	private int sawFramesCurrent;

	private boolean draw;
	final Paint paint;

	/**
	 * Create enemy
	 */
	public FredCircularSaw(Bitmap _bitmap, int _x, int _y, int _nbColums,
			int _nbRows) {
		super("CircularSaw", _bitmap, _x, _y, _nbColums, _nbRows, 10, 3);

		state = STATE_LINE;
		draw = true;
		linePoints = new ArrayList<Point>();
		lastDrawnPoint = new Point(_x + sprite.getWidth() / 2, _y
				+ sprite.getHeight() / 2);
		linePoints.add(new Point(lastDrawnPoint));
		lastNumber = 0;
		directionList = new ArrayList<Integer>();
		drawList = new ArrayList<Boolean>();
		directionIndex = 0;
		sawFramesCurrent = 0;

		affectedByFloor = false;
		affectedByWalls = false;

		redefineHitBox((sprite.getWidth() * 12) / 100,
				(sprite.getHeight() * 12) / 100,
				(sprite.getWidth() * 80) / 100, (sprite.getHeight() * 80) / 100);
		addAnimation("rotate", new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, 20);
		play("rotate", true, true);

		paint = new Paint();
		paint.setARGB(128, 0, 0, 0);

	}

	@Override
	public void update(long gameTime) {
		if (state == STATE_SAW) {
			super.update(gameTime);

			if (sawFramesCurrent == sawFramesPeriod) {
				sawFramesCurrent = 0;
				drawList.set(directionIndex, false);
				directionIndex++;
			}

			if (directionIndex < directionList.size()) {
				if (directionList.get(directionIndex) == DIAG_UP) {
					sprite.offset(speed, speed);
				} else if (directionList.get(directionIndex) == STRAIGHT) {
					sprite.offset(speed, 0);
				} else if (directionList.get(directionIndex) == DIAG_DOWN) {
					sprite.offset(speed, -speed);
				}
				sawFramesCurrent++;
			} else {
				dead = true;
			}

			// Destroyed if touches the floor
			if (onFloor()) {
				dead = true;
			}
		}

		// DRAW THE LINE
		if (state == STATE_LINE && draw
				&& gameTime > lastDrawnTime + drawPointPeriod) {
			Point newPoint = null;
			if (lastNumber > NB_POINTS_MIN) {
				int randomInt = (int) (Math.random() * 100);

				if (randomInt < 32) {
					// Diag up
					newPoint = new Point(lastDrawnPoint.x + 10,
							lastDrawnPoint.y + 10);
					linePoints.add(newPoint);
					directionList.add(DIAG_UP);
					drawList.add(true);
					lastDrawnPoint = newPoint;
					lastPointType = DIAG_UP;
				} else if (randomInt < 66) {
					// straight
					newPoint = new Point(lastDrawnPoint.x + 10,
							lastDrawnPoint.y);
					linePoints.add(newPoint);
					directionList.add(STRAIGHT);
					drawList.add(true);
					lastDrawnPoint = newPoint;
					lastPointType = STRAIGHT;
				} else {
					// Diag down
					newPoint = new Point(lastDrawnPoint.x + 10,
							lastDrawnPoint.y - 10);
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
					newPoint = new Point(lastDrawnPoint.x + 10,
							lastDrawnPoint.y + 10);
					linePoints.add(newPoint);
					directionList.add(DIAG_UP);
					drawList.add(true);
					lastDrawnPoint = newPoint;
					lastPointType = DIAG_UP;
				} else if (lastPointType == STRAIGHT) {
					// straight
					newPoint = new Point(lastDrawnPoint.x + 10,
							lastDrawnPoint.y);
					linePoints.add(newPoint);
					directionList.add(STRAIGHT);
					drawList.add(true);
					lastDrawnPoint = newPoint;
					lastPointType = STRAIGHT;
				} else if (lastPointType == DIAG_DOWN) {
					// Diag down
					newPoint = new Point(lastDrawnPoint.x + 10,
							lastDrawnPoint.y - 10);
					linePoints.add(newPoint);
					directionList.add(DIAG_DOWN);
					drawList.add(true);
					lastDrawnPoint = newPoint;
					lastPointType = DIAG_DOWN;
				}
				lastNumber++;
			}

			if (newPoint.x > MoveUtil.SCREEN_WIDTH + sprite.getWidth()
					|| newPoint.y < 0 - sprite.getHeight()
					|| newPoint.y + sprite.getHeight() / 2 >= MoveUtil.GROUND) {
				draw = false;
				state = STATE_SAW;
			}

			// Log.d(TAG, "Draw point at x=" + lastDrawnPoint.x + ", y=" +
			// lastDrawnPoint.y);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (state == STATE_LINE) {
			for (Point p : linePoints) {
				canvas.drawCircle(p.x, p.y, 2, paint);
			}
		} else {
			int i = 0;
			for (Point p : linePoints) {
				if (i < drawList.size() && drawList.get(i)) {
					canvas.drawCircle(p.x, p.y, 2, paint);
				}
				i++;
			}
			super.draw(canvas);
		}
	}

	@Override
	public void collide(GameEntity _gameEntity) {
		if (_gameEntity instanceof Personage) {
			((Personage) _gameEntity).getLife().looseLife(1);
		}
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub

	}

	public static EnemyEntity generateRandowStartingPosition(
			Point playerPosition) {
		// y in [20 ~ Ground-40]
		int randomY = (int) (20 + Math.random() * (MoveUtil.GROUND - 40));
		FredCircularSaw saw = new FredCircularSaw(
				BitmapUtil.createBitmap(R.drawable.enemy_circular_saw), -20,
				randomY, 10, 1);
		return saw;
	}
}
