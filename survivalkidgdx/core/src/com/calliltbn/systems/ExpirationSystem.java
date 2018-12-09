package com.calliltbn.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.calliltbn.components.ExpirationComponent;
import com.calliltbn.util.Mappers;

public class ExpirationSystem extends IteratingSystem {

    public ExpirationSystem() {
        super(Family.all(ExpirationComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ExpirationComponent expirComponent = Mappers.getComponent(ExpirationComponent.class , entity);
        if (expirComponent != null) {
            if (expirComponent.isDead(deltaTime)) {
                SystemUtils.removeEntity(getEngine(), entity, true, "expired");
            }
        }
    }
}
