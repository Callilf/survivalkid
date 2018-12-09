package com.calliltbn.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.calliltbn.components.CollideComponent;
import com.calliltbn.components.FriendlyFireComponent;
import com.calliltbn.components.HealthComponent;
import com.calliltbn.components.PlayerComponent;
import com.calliltbn.components.StateComponent;
import com.calliltbn.components.StateComponent.State;
import com.calliltbn.util.Mappers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CollisionSystem extends IteratingSystem {

    private List<Entity> collideEntity;
    private List<Entity> collidedEntity;

    public CollisionSystem() {
        super(Family.all(CollideComponent.class).get());

        collideEntity = new ArrayList<>();
        collidedEntity = new ArrayList<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Set<Entity> listDeadEntity = new HashSet<>();

        for (int i = 0 ; i < collideEntity.size() ; i++) {
            Entity sourceEntity = collideEntity.get(i);

            CollideComponent collideSource = Mappers.getComponent(CollideComponent.class , sourceEntity);
            HealthComponent healthSource = Mappers.getComponent(HealthComponent.class , sourceEntity);
            StateComponent stateSource = Mappers.getComponent(StateComponent.class , sourceEntity);
            if (collideSource == null || StateComponent.hasAtLeastOneState(stateSource, State.UNTOUCHABLE, State.RECOVERY)) {
                continue;
            }

            // loop inside collidedEntity
            for (Entity targetEntity :  collidedEntity) {
                checkAndProcessCollision(listDeadEntity, sourceEntity, collideSource, stateSource, healthSource, targetEntity);
            }

            // loop what's next inside collideEntity
            for (int j = i+1 ; j < collideEntity.size() ; j++) {
                Entity targetEntity = collideEntity.get(j);
                if (Mappers.hasAtLeastOneComponent(targetEntity, FriendlyFireComponent.class)
                        || Mappers.hasAtLeastOneComponent(sourceEntity, FriendlyFireComponent.class)) {
                    checkAndProcessCollision(listDeadEntity, sourceEntity, collideSource, stateSource, healthSource, targetEntity);
                }
            }
        }

        for (Entity deadEntity : listDeadEntity) {
            SystemUtils.removeEntity(getEngine(), deadEntity, true,"destroyed");
        }

        collideEntity.clear();
        collidedEntity.clear();
    }

    private void checkAndProcessCollision(Set<Entity> listDeadEntity, Entity sourceEntity, CollideComponent collideSource,
                                          StateComponent stateSource, HealthComponent healthSource, Entity targetEntity) {
        CollideComponent collideTarget = Mappers.getComponent(CollideComponent.class , targetEntity);
        HealthComponent healthTarget = Mappers.getComponent(HealthComponent.class , targetEntity);
        StateComponent stateTarget = Mappers.getComponent(StateComponent.class , targetEntity);

        if (collideTarget == null || healthSource == null && healthTarget == null
                || StateComponent.hasAtLeastOneState(stateTarget, State.UNTOUCHABLE, State.RECOVERY)) {
            return;
        }

        if (collideSource.collideWith(collideTarget)) {
            // process hit target -> source
            if (healthSource != null && !StateComponent.hasAtLeastOneState(stateSource, State.INVICIBLE)) {
                if (healthSource.hit(collideTarget.getDamage())) {
                    listDeadEntity.add(sourceEntity);
                }
                else if (stateSource != null && collideTarget.getRecoveryTime() != 0) {
                    stateSource.addState(State.RECOVERY, collideTarget.getRecoveryTime());
                }
            }

            if (healthTarget != null && !StateComponent.hasAtLeastOneState(stateTarget, State.INVICIBLE)) {
                if (healthTarget.hit(collideSource.getDamage())) {
                    listDeadEntity.add(targetEntity);
                }
                else if (stateTarget != null && collideSource.getRecoveryTime() != 0) {
                    stateTarget.addState(State.RECOVERY, collideSource.getRecoveryTime());
                }
            }
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (Mappers.hasAtLeastOneComponent(entity, PlayerComponent.class,
                FriendlyFireComponent.class)) {
            collideEntity.add(entity);
        }
        else {
            collidedEntity.add(entity);
        }
    }
}
