package com.calliltbn.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.calliltbn.GameScreen;
import com.calliltbn.components.CollideComponent;
import com.calliltbn.components.GravityComponent;
import com.calliltbn.components.HealthComponent;
import com.calliltbn.components.MoveStraightComponent;
import com.calliltbn.components.MoveStraightComponent.BorderCollision;
import com.calliltbn.components.SpriteComponent;
import com.calliltbn.spritesdef.SpriteAnimationEnum;
import com.calliltbn.spritesdef.TextureEnum;

public class EnemyFactory {

    /** The gdx pooled engine. */
    private PooledEngine engine;

    /**
     * Constructor.
     * @param e the engine
     */
    public EnemyFactory(PooledEngine e) {
        this.engine = e;
    }

    public Entity createCaterpillar() {
        Entity entity = engine.createEntity();

        float speed = (float) (Math.random() * (2.8f - 1.8f) + 1.8f);
        int damage = 5;
        float recoveryTime = 0.3f;
        TextureEnum texture = TextureEnum.CATERPILLAR;

        // switch to purple caterpillar is high speed
        if(speed >= 2.5f) {
            texture = TextureEnum.CATERPILLAR_PURPLE;
            damage = 10;
            recoveryTime = 0.5f;
        }

        Vector2 position = new Vector2();
        SpriteComponent spriteComponent =
                SpriteComponent.make(engine, texture, position, SpriteAnimationEnum.CATERPILLAR_MOVE, 5);

        // init position
        int random = (int) (Math.random() * 100);
        if (random < 50) {
            // start at the left
            position.x = - texture.getTextureDefault().getRegionWidth();
        }
        else {
            position.x = GameScreen.SCREEN_W;
            spriteComponent.setFlip(true);
            speed *= -1;
        }
        if (random < 34 || random > 65) {
            position.y = GameScreen.FLOOR_Y; // start on the floor
        }
        else {
            position.y = GameScreen.SCREEN_H - 20; // start on the top
        }

        entity.add(spriteComponent);

        entity.add(
                MoveStraightComponent.make(engine, new Vector2(speed,0), BorderCollision.DIE_OUT));
        entity.add(GravityComponent.make(engine, 2));
        entity.add(CollideComponent.make(engine, damage, recoveryTime, spriteComponent));
        entity.add(HealthComponent.make(engine, 1));


        engine.addEntity(entity);
        return entity;
    }
}
