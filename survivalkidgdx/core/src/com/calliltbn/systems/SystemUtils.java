package com.calliltbn.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.calliltbn.components.FollowerComponent;
import com.calliltbn.components.SuccessorComponent;
import com.calliltbn.util.Mappers;

public class SystemUtils {

    /**
     * Remove an entity from the game and it's linked entities
     *
     * @param engine the engine
     * @param entity the entity
     */
    public static void removeEntity(Engine engine, Entity entity, boolean spawnSuccessors, String message) {
        if (spawnSuccessors) {
            SuccessorComponent successors = Mappers.getComponent(SuccessorComponent.class , entity);
            if (successors != null) {
                Gdx.app.log("SystemUtils", "active successors on entity " + message);
                successors.setActivated(true);
                // don't remove the entity
                return;
            }
        }
        // destroy follower
        FollowerComponent followerComponent = Mappers.getComponent(FollowerComponent.class, entity);
        if (followerComponent != null) {
            engine.removeEntity(followerComponent.getFollower());
        }
        // destroy entity
        Gdx.app.log("SystemUtils", "remove entity " + message);
        engine.removeEntity(entity);
    }
}
