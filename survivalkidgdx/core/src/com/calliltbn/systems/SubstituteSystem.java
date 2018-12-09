package com.calliltbn.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.calliltbn.GameScreen;
import com.calliltbn.components.CollideComponent;
import com.calliltbn.components.SubstitutesComponent;
import com.calliltbn.desc.TypeEntity;
import com.calliltbn.util.Mappers;

public class SubstituteSystem extends IteratingSystem {

    public SubstituteSystem() {
        super(Family.all(SubstitutesComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SubstitutesComponent substitutes = Mappers.getComponent(SubstitutesComponent.class , entity);
        TypeEntity typeEntity = substitutes.getTypeEntity();
        switch (typeEntity) {
            case METEOR:
            case METEOR_FIRE:
                manageMeteorSubstitutes(entity, substitutes);
                return;
        }
    }

    private void manageMeteorSubstitutes(Entity entity, SubstitutesComponent substitutes) {
        CollideComponent collideComponent = Mappers.getComponent(CollideComponent.class , entity);
        Rectangle hitbox = collideComponent.getHitbox().getRectangle();
        // one rotation only
        if (hitbox.y + hitbox.height < GameScreen.SCREEN_H) {
            // process second state
            replaceAllSubstitutes(entity, substitutes);
        }

    }

    private void replaceAllSubstitutes(Entity entity, SubstitutesComponent substitutes) {
        // add or replace all component inside the substitutes component
        for (Component component : substitutes.getSubstitutes()) {
            entity.remove(component.getClass());
            entity.add(component);
        }
        if (substitutes.incRotation()) {
            // remove substitute component
            entity.remove(SubstitutesComponent.class);
        }
    }
}
