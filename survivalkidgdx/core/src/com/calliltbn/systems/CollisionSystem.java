package com.calliltbn.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.calliltbn.components.CollideComponent;
import com.calliltbn.components.FriendlyFireComponent;
import com.calliltbn.components.HealthComponent;
import com.calliltbn.components.KnockbackComponent;
import com.calliltbn.components.MoveComponent;
import com.calliltbn.components.PlayerComponent;
import com.calliltbn.components.PlayerComponent.PlayerAnimation;
import com.calliltbn.components.SpriteComponent;
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
            // 0 = same initiative, 1 = target has initiative, -1 = source has initiative
            int compareInitiative = Boolean.compare(collideTarget.hasInitiative(), collideSource.hasInitiative());

            // case when no initiative or target has initiative
            if (compareInitiative >= 0) {
                // process hit target -> source
                boolean isDead = processCollision(listDeadEntity, sourceEntity, stateSource, healthSource, targetEntity, collideTarget);
                if (!isDead || compareInitiative == 0) {
                    // process hit source -> target
                    processCollision(listDeadEntity, targetEntity, stateTarget, healthTarget, sourceEntity, collideSource);
                }
            }
            else {
                // source has initiative
                // process hit source -> target
                boolean isDead = processCollision(listDeadEntity, targetEntity, stateTarget, healthTarget, sourceEntity, collideSource);
                if (!isDead) {
                    // process hit target -> source
                    processCollision(listDeadEntity, sourceEntity, stateSource, healthSource, targetEntity, collideTarget);
                }
            }

        }
    }

    private boolean processCollision(Set<Entity> listDeadEntity, Entity sourceEntity, StateComponent stateSource, HealthComponent healthSource, Entity targetEntity, CollideComponent collideTarget) {
        if (healthSource != null && !StateComponent.hasAtLeastOneState(stateSource, State.INVICIBLE)) {
            if (healthSource.hit(collideTarget.getDamage())) {
                listDeadEntity.add(sourceEntity);
                return true;
            }
            if (stateSource != null) {
                if (collideTarget.getRecoveryTime() != 0) {
                    stateSource.addState(State.RECOVERY, collideTarget.getRecoveryTime());
                }
                KnockbackComponent knockbackComponent = Mappers.getComponent(KnockbackComponent.class, targetEntity);
                if (knockbackComponent != null) {
                    stateSource.addState(State.STUN, knockbackComponent.getStunDuration());
                    MoveComponent moveComponent = Mappers.getComponent(MoveComponent.class, sourceEntity);
                    if (moveComponent != null) {
                        moveComponent.setSpeed(knockbackComponent.getSpeed());
                        SpriteComponent spriteComponent = Mappers.getComponent(SpriteComponent.class, sourceEntity);
                        PlayerComponent playerComponent = Mappers.getComponent(PlayerComponent.class, sourceEntity);
                        spriteComponent.setCurrentAnimation(playerComponent.getSpriteAnim(PlayerAnimation.KNOCK_BACK), false);
                    }
                }
            }

        }
        return false;
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
