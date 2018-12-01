package com.calliltbn.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.calliltbn.GameScreen;
import com.calliltbn.components.CollideComponent;
import com.calliltbn.components.GravityComponent;
import com.calliltbn.components.MoveStraightComponent;
import com.calliltbn.components.SpriteComponent;
import com.calliltbn.util.Mappers;

public class MoveSystem extends IteratingSystem {

    private static final int FLOOR_Y = 40;
    private static final int MAX_SPEED_DOWN = -25;

    public MoveSystem() {
        super(Family.all(MoveStraightComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MoveStraightComponent moveStraightComponent = Mappers.getComponent(MoveStraightComponent.class, entity);
        SpriteComponent spriteComponent = Mappers.getComponent(SpriteComponent.class, entity);
        CollideComponent collideComponent = Mappers.getComponent(CollideComponent.class, entity);

        if (spriteComponent != null && moveStraightComponent != null) {
            Vector2 speed = moveStraightComponent.getSpeed();
            Vector2 position = spriteComponent.getPosition();

            // manage gravity
            if (collideComponent != null && !isOnFloor(collideComponent.getHitbox().getRectangle())) {
                GravityComponent gravityComponent = Mappers.getComponent(GravityComponent.class, entity);
                if (gravityComponent != null) {
                    speed.y = Math.max(MAX_SPEED_DOWN, speed.y-gravityComponent.getGravity());
                }
            }

            // if no speed, do nothing
            if (speed.isZero()) {
                return;
            }
            position.add(speed);

            if (collideComponent != null) {
                collideComponent.updateHitbox();
            }
            processSpriteOutOfScreen(moveStraightComponent, collideComponent.getHitbox().getRectangle(),
                    spriteComponent, speed, position);

        }
    }

    private void processSpriteOutOfScreen(MoveStraightComponent moveStraightComponent, Rectangle hitbox,
                                          SpriteComponent spriteComponent, Vector2 speed, Vector2 position) {
        Sprite sprite = spriteComponent.getSprite();
        // touch left or right
        if (hitbox.x <= 0 && speed.x < 0 || hitbox.x + hitbox.width >= GameScreen.SCREEN_W && speed.x > 0) {
            switch (moveStraightComponent.getBorderCollision()) {
                case STOP:
                    speed.x=0;
                    if (hitbox.x <= 0) {
                        position.x -= hitbox.x;
                        hitbox.x = 0;
                    }
                    else {
                        position.x -= hitbox.x - GameScreen.SCREEN_W + hitbox.width;
                        hitbox.x = GameScreen.SCREEN_W - hitbox.width;
                    }
                    break;
                case BOUNCE:
                    speed.x *= -1;
                    spriteComponent.setFlip(hitbox.x > 0);
                    break;
                case DIE_TOUCH:
                    // destroy entity
                    return;
                case DIE_OUT:
                    if (hitbox.x + hitbox.width < 0 || hitbox.x > GameScreen.SCREEN_W) {
                        // destroy entity
                    }
                    return;
            }
        }
        // touch floor or ceil
        if (isOnFloor(hitbox) && speed.y < 0 || hitbox.y + hitbox.height >= GameScreen.SCREEN_H && speed.y > 0) {
            switch (moveStraightComponent.getBorderCollision()) {
                case BOUNCE:
                    speed.y *= -1;
                    break;
                case DIE_TOUCH:
                    // destroy entity
                    return;
                case DIE_OUT:
                    if (hitbox.y > GameScreen.SCREEN_H) {
                        // destroy entity
                    }
                    return;
                case FALL:
                case STOP:
                    if (hitbox.y <= FLOOR_Y) {
                        position.y -= hitbox.y - FLOOR_Y;
                        hitbox.y = FLOOR_Y;
                    }
                    else {
                        speed.y *= -1;
                    }
                    break;
            }
        }
    }

    public static boolean isOnFloor(Rectangle hitbox) {
        return hitbox.y <= FLOOR_Y;
    }
}
