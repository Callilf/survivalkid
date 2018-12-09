package com.calliltbn.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.calliltbn.components.CollideComponent;
import com.calliltbn.components.SpriteComponent;
import com.calliltbn.components.SuccessorComponent;
import com.calliltbn.desc.TypeEntity;
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

    public Entity createMeteorExplosion(Entity parentEntity) {
        return createDyingAnimation(parentEntity, TextureEnum.METEOR_EXPLOSION, SpriteAnimationEnum.METEOR_EXPLODE, 25);
    }

    public Entity createMeteorFireExplosion(Entity parentEntity) {
        Entity entity = createDyingAnimation(parentEntity, TextureEnum.METEOR_FIRE_EXPLOSION, SpriteAnimationEnum.METEOR_FIRE_EXPLODE, 35);
        // the end of the explosion produce the new enemy fire ground
        entity.add(SuccessorComponent.make(engine, TypeEntity.FIRE_GROUND));
        return entity;
    }

    public Entity createFireGoundDie(Entity parentEntity) {
        return createDyingAnimation(parentEntity, TextureEnum.FIRE_GROUND, SpriteAnimationEnum.FIRE_GROUND_DIE, 0);
    }

    public Entity createSmokeWhiteSmall(Entity parentEntity) {
        return createDyingAnimation(parentEntity, TextureEnum.SMOKE_WHITE_SMALL, SpriteAnimationEnum.SMOKE_ALL_DIE, 10);
    }

    public Entity createSmokeWhiteLarge(Entity parentEntity) {
        return createDyingAnimation(parentEntity, TextureEnum.SMOKE_WHITE_LARGE, SpriteAnimationEnum.SMOKE_ALL_DIE, 10);
    }

    public Entity createSmokeBrownLarge(Entity parentEntity) {
        return createDyingAnimation(parentEntity, TextureEnum.SMOKE_BROWN_LARGE, SpriteAnimationEnum.SMOKE_ALL_DIE, 10);
    }

    private Entity createDyingAnimation(Entity parentEntity, TextureEnum texture, SpriteAnimationEnum anim, float shiftY) {
        Entity entity = engine.createEntity();
        CollideComponent collideComponent = Mappers.getComponent(CollideComponent.class, parentEntity);
        Rectangle hitbox = collideComponent.getHitbox().getRectangle();

        TextureRegion region =  texture.getTextureDefault();
        Vector2 position = new Vector2(hitbox.x + hitbox.width/2 - region.getRegionWidth() / 2,
                hitbox.y + hitbox.height/2 - region.getRegionHeight() / 2 + shiftY);
        SpriteComponent spriteComponent = SpriteComponent.make(engine, texture, position, anim, 2);
        spriteComponent.setDieOnFinished();
        entity.add(spriteComponent);

        engine.addEntity(entity);

        return entity;
    }
}
