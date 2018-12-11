package com.calliltbn.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.calliltbn.GameScreen;
import com.calliltbn.components.BlinkComponent;
import com.calliltbn.components.CollideComponent;
import com.calliltbn.components.MoveOnLineComponent;
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
            case CIRCULAR_SAW:
                manageCircularSawSubstitutes(entity, substitutes);
                return;
            case BULL:
                manageBullSubstitutes(entity, substitutes, deltaTime);
        }
    }

    private void manageBullSubstitutes(Entity entity, SubstitutesComponent substitutes, float deltaTime) {
        float duration = substitutes.getDuration(deltaTime);
        if (substitutes.getRotation() == 0 && duration > 1.5f) {
            // rotation 1 - increase blink frequency
            substitutes.incRotation();
            BlinkComponent blinkComponent = Mappers.getComponent(BlinkComponent.class, entity);
            blinkComponent.updateBlinkPeriod(1/31f, 1/31f);
        }
        else if (duration > 2f){
            // rotation 2 - enable bull
            entity.remove(BlinkComponent.class);
            replaceAllSubstitutes(entity, substitutes);
        }
    }

    private void manageCircularSawSubstitutes(Entity entity, SubstitutesComponent substitutes) {
        MoveOnLineComponent moveOnLineComponent = Mappers.getComponent(MoveOnLineComponent.class , entity);
        // if the last point is draw
        if (moveOnLineComponent.isAllLineDraw()) {
            // process second state
            replaceAllSubstitutes(entity, substitutes);
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
            // entity.remove(component.getClass()); // no need, the add do it
            entity.add(component);
        }
        if (substitutes.incRotation()) {
            // remove substitute component
            entity.remove(SubstitutesComponent.class);
        }
    }
}
