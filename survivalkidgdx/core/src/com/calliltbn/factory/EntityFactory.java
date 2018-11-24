package com.calliltbn.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.calliltbn.components.MoveStraightComponent;
import com.calliltbn.components.SpriteComponent;
import com.calliltbn.spritesdef.SpriteAnimationEnum;
import com.calliltbn.spritesdef.TextureEnum;

/**
 * Factory used to create presets of entities.
 *
 * @author tbn
 *
 */
public class EntityFactory {

    /** The gdx pooled engine. */
    public PooledEngine engine;

    /**
     * Constructor.
     * @param e the engine
     */
    public EntityFactory(PooledEngine e) {
        this.engine = e;
    }

    /**
     * Create the player.
     * @param pos the position
     * @return the player entity
     */
    public Entity createPlayer(Vector2 pos) {
        Entity playerEntity = engine.createEntity();

        SpriteComponent spriteCompo = engine.createComponent(SpriteComponent.class);
        spriteCompo.initTexture(TextureEnum.YUNA);
        spriteCompo.setCurrentAnimation(SpriteAnimationEnum.YUNA_RUN, true);
        spriteCompo.setPosition(pos);
        playerEntity.add(spriteCompo);

        MoveStraightComponent moveStraightCompo = engine.createComponent(MoveStraightComponent.class);
        moveStraightCompo.setSpeed(new Vector2(4,3));
        moveStraightCompo.setBorderCollision(MoveStraightComponent.BorderCollision.BOUNCE);
        playerEntity.add(moveStraightCompo);

        engine.addEntity(playerEntity);

        return playerEntity;
    }

    private TextureRegion[] getTextureRegion(TextureRegion[][] matrice, int width, int[] positions) {
        TextureRegion[] allFrames = new TextureRegion[positions.length];
        for (int i=0 ; i < positions.length ; i++) {
            allFrames[i] = matrice[positions[i]/width-1][positions[i]%width];
        }
        return allFrames;
    }
}
