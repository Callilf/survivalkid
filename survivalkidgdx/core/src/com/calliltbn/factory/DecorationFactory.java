package com.calliltbn.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.calliltbn.components.CollideComponent;
import com.calliltbn.components.SpriteComponent;
import com.calliltbn.spritesdef.SpriteAnimationEnum;
import com.calliltbn.spritesdef.TextureEnum;
import com.calliltbn.util.Mappers;

public class DecorationFactory {

    /** The gdx pooled engine. */
    private PooledEngine engine;

    /**
     * Constructor.
     * @param e the engine
     */
    public DecorationFactory(PooledEngine e) {
        this.engine = e;
    }


    public Entity createSmokeWhiteSmall(Entity parentEntity) {
        return createDyingAnimation(parentEntity, TextureEnum.SMOKE_WHITE_SMALL);
    }

    public Entity createSmokeWhiteLarge(Entity parentEntity) {
        return createDyingAnimation(parentEntity, TextureEnum.SMOKE_WHITE_LARGE);
    }

    public Entity createSmokeBrownLarge(Entity parentEntity) {
        return createDyingAnimation(parentEntity, TextureEnum.SMOKE_BROWN_LARGE);
    }

    private Entity createDyingAnimation(Entity parentEntity, TextureEnum texture) {
        Entity entity = engine.createEntity();
        CollideComponent collideComponent = Mappers.getComponent(CollideComponent.class, parentEntity);
        Rectangle hitbox = collideComponent.getHitbox().getRectangle();

        TextureRegion region =  texture.getTextureDefault();
        Vector2 position = new Vector2(hitbox.x + hitbox.width/2 - region.getRegionWidth() / 2,
                hitbox.y + hitbox.height/2 - region.getRegionHeight() / 2);
        SpriteComponent spriteComponent = SpriteComponent.make(engine, texture, position, SpriteAnimationEnum.SMOKE_ALL_DIE, 2);
        spriteComponent.setDieOnFinished();
        entity.add(spriteComponent);

        engine.addEntity(entity);

        return entity;
    }
}
