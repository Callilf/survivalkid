package com.calliltbn.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.calliltbn.components.BlinkComponent;
import com.calliltbn.components.StateComponent;
import com.calliltbn.components.StateComponent.State;
import com.calliltbn.util.Mappers;

import java.util.List;
import java.util.Set;

public class StateSystem extends IteratingSystem {

    public StateSystem() {
        super(Family.all(StateComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StateComponent component = Mappers.getComponent(StateComponent.class, entity);
        List<State> endStates = component.checkStatesEnded(deltaTime);
        for (State endState : endStates) {
            if (endState == State.RECOVERY) {
                entity.remove(BlinkComponent.class);
            }
        }
        Set<State> activeStates = component.getActiveStates();
        for (State activeState : activeStates) {
            if (activeState == State.RECOVERY) {
                if (!Mappers.hasAtLeastOneComponent(entity,BlinkComponent.class)) {
                    entity.add(BlinkComponent.make((PooledEngine) getEngine(), 0.03f, 0.015f));
                }
            }
        }
    }
}
