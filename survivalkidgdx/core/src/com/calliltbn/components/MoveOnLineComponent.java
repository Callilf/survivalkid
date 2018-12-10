package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.calliltbn.GameScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Draw a line of points for the circular saw and make the sprite of the saw follow it
 *
 * @author tbn
 */
public class MoveOnLineComponent implements Component {

    private static final float DRAW_DURATION = 1f;

    private Vector2 pointToMove;
    private Vector2 shift;

    private List<Vector2> listPoints;

    /** Next point to follow */
    private int nextPoint;
    /** Nb point draw */
    private float timeSinceCreated;

    /** Generate a MoveOnLineComponent */
    public static MoveOnLineComponent make(PooledEngine engine, Sprite sprite, float speed) {
        MoveOnLineComponent component = engine.createComponent(MoveOnLineComponent.class);
        component.shift = new Vector2(sprite.getWidth() / 2, sprite.getHeight() / 2);
        component.listPoints = new ArrayList<>();
        component.nextPoint = 0;
        component.timeSinceCreated = 0;
        component.pointToMove = new Vector2(sprite.getX(), sprite.getY());
        component.initListPoints(speed, sprite);
        return component;
    }

    /**
     * Init random points line
     *
     * @param speed horizontal speed of the circular saw
     */
    private void initListPoints(float speed, Sprite sprite) {
        float space = 5f * speed;
        int nbDrawInDir = 0;
        int minToDrawInDir = 5;

        // add the current position
        Vector2 currentPoint = new Vector2(sprite.getX() + shift.x, sprite.getY() + shift.y);
        listPoints.add(currentPoint);

        // random between -1 (go down), 0 (go straight) and 1 (go top)
        // reverse down and top if speed is negative, but whatever
        int lastDirection = (int) (Math.random() * 3) - 1;

        // loop until reach opposite side of screen, crash on floor, or disappear on top
        while (!(speed > 0 && currentPoint.x > GameScreen.SCREEN_W + shift.x
                || speed < 0 && currentPoint.x < 0 - sprite.getWidth() - shift.x
                || currentPoint.y > GameScreen.SCREEN_H + shift.y || currentPoint.y < GameScreen.FLOOR_Y)) {
            if (++nbDrawInDir > minToDrawInDir) {
                int newLastDirection =  (int) (Math.random() * 3) - 1;
                if (newLastDirection != lastDirection) {
                    lastDirection = newLastDirection;
                    nbDrawInDir = 0;
                }
            }
            currentPoint = new Vector2(currentPoint.x + space, currentPoint.y + space * lastDirection);
            listPoints.add(currentPoint);
        }
    }

    public int getLastPointToDraw(float deltaTime) {
        timeSinceCreated += deltaTime;
        if (timeSinceCreated >= DRAW_DURATION) {
            return listPoints.size();
        }
        else {
            return (int) (listPoints.size() * timeSinceCreated / DRAW_DURATION);
        }
    }

    public boolean isAllLineDraw() {
        return timeSinceCreated >= DRAW_DURATION;
    }

    public Vector2 getPointToFollow(Vector2 position) {
        if (listPoints.size() - 1 != nextPoint) {
            float diff = Math.abs(position.x - pointToMove.x);
            if (diff < 0.01f) {
                Vector2 nextPointV = listPoints.get(++nextPoint);
                pointToMove.x = nextPointV.x - shift.x;
                pointToMove.y = nextPointV.y - shift.y;
            }
        }
        return pointToMove;
    }

    public List<Vector2> getListPoints() {
        return listPoints;
    }

    public int getNextPoint() {
        return nextPoint;
    }
}
