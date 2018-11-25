package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;

public class MoveStraightComponent implements Component {

    public enum BorderCollision {
        FALL, BOUNCE, DIE_TOUCH, DIE_OUT, STOP;
    }

    /** Generate a MoveStraightComponent */
    public static MoveStraightComponent make(PooledEngine engine, Vector2 speed, BorderCollision borderCollision) {
        MoveStraightComponent component = engine.createComponent(MoveStraightComponent.class);
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
