package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;

public class KnockbackComponent implements Component {

    /** Generate a KnockbackComponent */
    public static KnockbackComponent make(PooledEngine engine, Vector2 speed, float stunDuration) {
        KnockbackComponent component = engine.createComponent(KnockbackComponent.class);
        component.stunDuration = stunDuration;
        component.speed = speed;
        return component;
    }

    /** duration of the stun */
    private float stunDuration;

    /** speed apply on hit */
    private Vector2 speed;

    public float getStunDuration() {
        return stunDuration;
    }

    public Vector2 getSpeed() {
        return speed;
    }
}
