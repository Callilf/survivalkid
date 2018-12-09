package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;

/**
 * Other entity that follow the source entity with a shift position.
 * The follower die when the entity die
 *
 * @author tbn
 */
public class FollowerComponent implements Component {

    private Entity follower;

    private Vector2 shift;

    /** Generate a FollowerComponent */
    public static FollowerComponent make(PooledEngine engine, Vector2 shift, Entity follower) {
        FollowerComponent component = engine.createComponent(FollowerComponent.class);
        component.follower = follower;
        component.shift = shift;
        return component;
    }

    public Vector2 getShift() {
        return shift;
    }

    public Entity getFollower() {
        return follower;
    }
}
