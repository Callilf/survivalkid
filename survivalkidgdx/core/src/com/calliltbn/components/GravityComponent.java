package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;

/**
 * The entity fall faster without vertical speed
 *
 * @author tbn
 */
public class GravityComponent implements Component {

    /** Generate a GravityComponent */
    public static GravityComponent make(PooledEngine engine, float gravity) {
        GravityComponent component = engine.createComponent(GravityComponent.class);
        component.gravity = gravity;
        return component;
    }

    private float gravity;

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

}
