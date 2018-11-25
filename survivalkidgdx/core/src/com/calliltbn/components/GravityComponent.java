package com.calliltbn.components;

import com.badlogic.ashley.core.Component;

public class GravityComponent implements Component {

    private float gravity;

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

}
