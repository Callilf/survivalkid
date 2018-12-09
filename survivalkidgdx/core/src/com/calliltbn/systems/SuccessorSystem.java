package com.calliltbn.systems;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.calliltbn.components.SuccessorComponent;
import com.calliltbn.factory.EntityFactory;
import com.calliltbn.desc.TypeEntity;
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
                Gdx.app.log("SUCCESSOR", "Create successor " + successor);
                entityFactory.createSuccessor(entity, successor);
            }

            // delete the entity
            SystemUtils.removeEntity(getEngine(), entity, false, "with successor");
        }
    }
}
