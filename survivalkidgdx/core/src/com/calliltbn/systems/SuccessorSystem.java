package com.calliltbn.systems;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.calliltbn.components.SuccessorComponent;
import com.calliltbn.factory.EntityFactory;
import com.calliltbn.factory.TypeEntity;
import com.calliltbn.util.Mappers;

public class SuccessorSystem extends IteratingSystem {

    private EntityFactory entityFactory;

    public SuccessorSystem(EntityFactory entityFactory) {
        super(Family.all(SuccessorComponent.class).get());
        this.entityFactory = entityFactory;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SuccessorComponent successors = Mappers.getComponent(SuccessorComponent.class , entity);
        if (successors.isActivated()) {
            for (TypeEntity successor : successors.getSuccessors()) {
                entityFactory.createSuccessor(entity, successor);
            }

            // delete the entity
            getEngine().removeEntity(entity);
        }
    }
}
