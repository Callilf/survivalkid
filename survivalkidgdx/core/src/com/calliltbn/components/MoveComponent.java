package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;

/**
 * Set a constant vertical and horizontal speed to the entity. Can be change to adapt changes of speed
 *
 * @author tbn
 */
public class MoveComponent implements Component {

    public enum BorderCollision {
        FALL, BOUNCE, DIE_TOUCH, DIE_OUT, STOP;
    }

    /** Generate a MoveComponent */
    public static MoveComponent make(PooledEngine engine, Vector2 speed, BorderCollision borderCollision) {
        MoveComponent component = engine.createComponent(MoveComponent.class);
        component.speed = speed;
        component.borderCollision = borderCollision;
        return component;
    }

    private Vector2 speed;

    private BorderCollision borderCollision = BorderCollision.DIE_OUT;

    public void setSpeed(Vector2 speed) {
        this.speed = speed;
    }

    public Vector2 getSpeed() {
        return speed;
    }

    public BorderCollision getBorderCollision() {
        return borderCollision;
    }

    public void setBorderCollision(BorderCollision borderCollision) {
        this.borderCollision = borderCollision;
    }

}
