package com.survivalkid.game.entity.enemy.impl;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.survivalkid.game.GameManager;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.enemy.EnemyEntity;
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

	private List<Point> linePoints;

	private int drawPointPeriod = 1000;
	private int lastDrawnTime = 0;
	private Point lastDrawnPoint;
	private int lastNumber;
	private int lastPointType;

	private boolean draw;

	/**
	 * Create enemy
	 */
	public FredCircularSaw(Bitmap _bitmap, int _x, int _y, int _nbColums, int _nbRows) {
		super("CircularSaw", _bitmap, _x, _y, _nbColums, _nbRows, 10, 3);

		state = STATE_LINE;
		draw = true;
		linePoints = new ArrayList<Point>();
		lastDrawnPoint = new Point(_x, _y);
		linePoints.add(new Point(lastDrawnPoint));
		lastNumber = 0;

		redefineHitBox((sprite.getWidth() * 12) / 100, (sprite.getHeight() * 12) / 100, (sprite.getWidth() * 80) / 100,
				(sprite.getHeight() * 80) / 100);
		addAnimation("rotate", new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, 20);
		play("rotate", true, true);

	}

	@Override
	public void update(long gameTime) {
		super.update(gameTime);

		
		//DRAW THE LINE
		if (state == STATE_LINE && draw && gameTime > lastDrawnTime + drawPointPeriod) {
			Point newPoint = null;
			if (lastNumber > NB_POINTS_MIN) {
				int randomInt = (int) (Math.random() * 100);

				if (randomInt < 32) {
					// Diag up
					newPoint = new Point(lastDrawnPoint.x + 10, lastDrawnPoint.y + 10);
					linePoints.add(newPoint);
					lastDrawnPoint = newPoint;
					lastPointType = DIAG_UP;
				} else if (randomInt < 66) {
					// straight
					newPoint = new Point(lastDrawnPoint.x + 10, lastDrawnPoint.y);
					linePoints.add(newPoint);
					lastDrawnPoint = newPoint;
					lastPointType = STRAIGHT;
				} else {
					// Diag down
					newPoint = new Point(lastDrawnPoint.x + 10, lastDrawnPoint.y - 10);
					linePoints.add(newPoint);
					lastDrawnPoint = newPoint;
					lastPointType = DIAG_DOWN;
				}
				lastNumber = 1;
			} else {
				if (lastPointType == DIAG_UP) {
					// Diag up
					newPoint = new Point(lastDrawnPoint.x + 10, lastDrawnPoint.y + 10);
					linePoints.add(newPoint);
					lastDrawnPoint = newPoint;
					lastPointType = DIAG_UP;
				} else if (lastPointType == STRAIGHT) {
					// straight
					newPoint = new Point(lastDrawnPoint.x + 10, lastDrawnPoint.y);
					linePoints.add(newPoint);
					lastDrawnPoint = newPoint;
					lastPointType = STRAIGHT;
				} else if(lastPointType == DIAG_DOWN){
					// Diag down
					newPoint = new Point(lastDrawnPoint.x + 10, lastDrawnPoint.y - 10);
					linePoints.add(newPoint);
					lastDrawnPoint = newPoint;
					lastPointType = DIAG_DOWN;
				}
				lastNumber ++;
			}

			if (newPoint.x > MoveUtil.SCREEN_WIDTH || newPoint.y < 0 || newPoint.y > MoveUtil.SCREEN_HEIGHT) {
				draw = false;
				dead = true;
			}
			
			Log.d(TAG,"Draw point at x=" + lastDrawnPoint.x + ", y=" + lastDrawnPoint.y);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (state == STATE_LINE) {
			for (Point p : linePoints) {
				final Paint paint = new Paint();
				paint.setARGB(255, 0, 0, 0);
				canvas.drawCircle(p.x, p.y, 2, paint);
			}
		} else {
			for (Point p : linePoints) {
				final Paint paint = new Paint();
				paint.setARGB(255, 0, 0, 0);
				canvas.drawCircle(p.x, p.y, 2, paint);
			}
			super.draw(canvas);
		}
	}

	@Override
	public void collide(GameEntity _gameEntity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void die() {
		// TODO Auto-generated method stub

	}

}
