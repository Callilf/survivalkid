package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;

/**
 * The entity collide with it's ally (used for enemies that destroys other enemies)
 *
 * @author tbn
 */
public class FriendlyFireComponent implements Component {

    /** Generate a FriendlyFireComponent */
    public static FriendlyFireComponent make(PooledEngine engine) {
        FriendlyFireComponent component = engine.createComponent(FriendlyFireComponent.class);
        return component;
    }

    // nothing to do, it's just a flag to simplify the collision calculation
}
