package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;

public class FriendlyFireComponent implements Component {

    /** Generate a FriendlyFireComponent */
    public static FriendlyFireComponent make(PooledEngine engine) {
        FriendlyFireComponent component = engine.createComponent(FriendlyFireComponent.class);
        return component;
    }

    // nothing to do, it's just a flag to simplify the collision calculation
}
