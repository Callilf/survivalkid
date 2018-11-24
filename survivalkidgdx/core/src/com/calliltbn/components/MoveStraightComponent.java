package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MoveStraightComponent implements Component {

    public enum BorderCollision {
        FALL, BOUNCE, DIE_TOUCH, DIE_OUT, STOP;
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
