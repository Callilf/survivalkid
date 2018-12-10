package com.calliltbn.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.calliltbn.InputSingleton;
import com.calliltbn.components.CollideComponent;
import com.calliltbn.components.GravityComponent;
import com.calliltbn.components.MoveComponent;
import com.calliltbn.components.PlayerComponent;
import com.calliltbn.components.PlayerComponent.PlayerAnimation;
import com.calliltbn.components.SpriteComponent;
import com.calliltbn.util.Mappers;

public class PlayerSpeedSystem extends IteratingSystem {

    protected static final float JUMP_DURATION_MAX = 0.47f;
    protected static final float JUMP_SLOW_BEGIN = 0.26f;

    protected static final float VITESSE_MAX_X = 9;
    protected static final float ACCELERATION_X = 0.75f;
    protected static final float DECELERATE_X = 1.3f;
    protected static final float VITESSE_Y = 15;

    /** To simplify access to singleton */
    private InputSingleton input;

    public PlayerSpeedSystem() {
        super(Family.all(PlayerComponent.class).get());
        input = InputSingleton.getInstance();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MoveComponent moveStraightComponent = Mappers.getComponent(MoveComponent.class, entity);
        SpriteComponent spriteComponent = Mappers.getComponent(SpriteComponent.class, entity);
        PlayerComponent playerComponent = Mappers.getComponent(PlayerComponent.class, entity);
        if (moveStraightComponent != null && spriteComponent != null && playerComponent != null) {
            calculatePlayerSpeed(moveStraightComponent.getSpeed(), spriteComponent, playerComponent, entity, deltaTime);
        }
        else {
            System.out.println("PLAYER WITHOUT MANDATORY COMPONENT");
        }
    }


    private void calculatePlayerSpeed(Vector2 speed, SpriteComponent spriteComponent,
                                      PlayerComponent player, Entity entity, float deltaTime) {

        Vector2 position = spriteComponent.getPosition();
        InputSingleton input = InputSingleton.getInstance();

        // the second condition give priority to the last key pressed between left and right
        if (input.leftPressed && (input.leftLastPressed || !input.rightPressed)) {
            // move to left
            speed.x = (Math.max(-VITESSE_MAX_X,speed.x- ACCELERATION_X));

            spriteComponent.setFlip(true);
            spriteComponent.setCurrentAnimation(player.getSpriteAnim(PlayerAnimation.RUN), true);
        }
        else if (input.rightPressed) {
            // move to right
            speed.x = (Math.min(VITESSE_MAX_X,speed.x+ ACCELERATION_X));

            spriteComponent.setFlip(false);
            spriteComponent.setCurrentAnimation(player.getSpriteAnim(PlayerAnimation.RUN), true);
        }
        else {
            slowDown(speed);
        }
        if (input.jumpPressed || speed.y != 0) {
            GravityComponent gravityComponent = Mappers.getComponent(GravityComponent.class, entity);
            CollideComponent collideComponent = Mappers.getComponent(CollideComponent.class, entity);
            float gravity = (gravityComponent != null)? gravityComponent.getGravity() : 0;
            PlayerAnimation anim = jump(player, speed, collideComponent.getHitbox().getRectangle(), gravity, deltaTime);
            spriteComponent.setCurrentAnimation(player.getSpriteAnim(anim), false);
        }
        if (speed.y == 0 && speed.x == 0) {
            spriteComponent.setCurrentAnimation(player.getSpriteAnim(PlayerAnimation.STAND), true);
        }
    }

    private void slowDown(Vector2 speed) {
        if (speed.x != 0) {
            if (speed.x > 0)
            {
                speed.x = Math.max(0, speed.x - DECELERATE_X);
            }
            else
            {
                speed.x = Math.min(0, speed.x + DECELERATE_X);
            }
        }
    }

    public PlayerAnimation jump(PlayerComponent player, Vector2 speed, Rectangle hitbox,
                                float gravity, float deltaTime) {
        if (MoveSystem.isOnFloor(hitbox)) {
            player.setJumpDuration(0);
            if (input.jumpPressed) {
                // start jump
                speed.y = VITESSE_Y + gravity;
                return PlayerAnimation.JUMP;
            }
            else {
                // end jump
                speed.y = 0;
                return PlayerAnimation.STAND;
            }
        }
        float jumpDuration = player.getJumpDuration() + deltaTime;
        player.setJumpDuration(jumpDuration);

        if (input.jumpPressed && jumpDuration < JUMP_DURATION_MAX) {
            // don't fall
            if (jumpDuration > JUMP_SLOW_BEGIN) {
                // speed reduce by half the gravity
                speed.y += gravity / 2;
            } else {
                // continue jumping
                speed.y = VITESSE_Y + gravity;
            }
        }
        // else continue or start falling - don't change speed, the gravity will do it
        if (speed.y > 0) {
            return PlayerAnimation.JUMP;
        }
        else {
            return PlayerAnimation.FALL;
        }
    }
}
