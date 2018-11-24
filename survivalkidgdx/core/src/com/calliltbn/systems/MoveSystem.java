package com.calliltbn.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.calliltbn.GameScreen;
import com.calliltbn.components.MoveStraightComponent;
import com.calliltbn.components.SpriteComponent;
import com.calliltbn.util.Mappers;

public class MoveSystem extends IteratingSystem {

    public MoveSystem() {
        super(Family.all(MoveStraightComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MoveStraightComponent moveStraightComponent = Mappers.getComponent(MoveStraightComponent.class, entity);
        SpriteComponent spriteComponent = Mappers.getComponent(SpriteComponent.class, entity);

        if (spriteComponent != null && moveStraightComponent != null) {
            Vector2 speed = moveStraightComponent.getSpeed();
            if (speed.isZero()) {
                return;
            }
            Vector2 position = spriteComponent.getPosition();
            position.add(speed);
            processSpriteOutOfScreen(moveStraightComponent, spriteComponent, speed, position);
        }
    }

    private void processSpriteOutOfScreen(MoveStraightComponent moveStraightComponent,
                                          SpriteComponent spriteComponent, Vector2 speed, Vector2 position) {
        Sprite sprite = spriteComponent.getSprite(0);
        // touch left or right
        if (position.x <= 0 && speed.x < 0 || position.x + sprite.getWidth() >= GameScreen.SCREEN_W && speed.x > 0) {
            switch (moveStraightComponent.getBorderCollision()) {
                case BOUNCE:
                    speed.x *= -1;
                    spriteComponent.setFlip(position.x > 0);
                    break;
                case DIE_TOUCH:
                    // destroy entity
                    return;
                case DIE_OUT:
                    if (position.x + sprite.getWidth() < 0 || position.x > GameScreen.SCREEN_W) {
                        // destroy entity
                    }
                    return;
            }
        }
        // touch floor or ceil
        if (position.y <= 40 && speed.y < 0 || position.y + sprite.getHeight() >= GameScreen.SCREEN_H && speed.y > 0) {
            switch (moveStraightComponent.getBorderCollision()) {
                case BOUNCE:
                    speed.y *= -1;
                    break;
                case DIE_TOUCH:
                    // destroy entity
                    return;
                case DIE_OUT:
                    if (position.y > GameScreen.SCREEN_H) {
                        // destroy entity
                    }
                    return;
                case FALL:
                    if (position.y <= 40) {
                        position.y = 40;
                        speed.x = 0;
                        speed.y = 0;
                    }
                    else {
                        speed.y *= -1;
                        speed.x = 0;
                    }
                    break;
            }
        }
    }
}
