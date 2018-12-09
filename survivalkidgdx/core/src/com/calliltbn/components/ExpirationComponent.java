package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;

/**
 * Entity that disappear after an amount of time
 *
 * @author tbn
 */
public class ExpirationComponent implements Component {

    private float timeout;

    /** Generate a ExpirationComponent */
    public static ExpirationComponent make(PooledEngine engine, float timeout) {
        ExpirationComponent component = engine.createComponent(ExpirationComponent.class);
        component.timeout = timeout;
        return component;
    }

    public boolean isDead(float deltaTime) {
        timeout -= deltaTime;
        return timeout <= 0;
    }
}
