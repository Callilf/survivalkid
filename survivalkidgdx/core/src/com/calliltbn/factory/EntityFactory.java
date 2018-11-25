package com.calliltbn.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.calliltbn.components.GravityComponent;
import com.calliltbn.components.MoveStraightComponent;
import com.calliltbn.components.PlayerComponent;
import com.calliltbn.components.PlayerComponent.Perso;
import com.calliltbn.components.SpriteComponent;
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
    public Entity createPlayer(Vector2 pos, Perso personage) {
        Entity playerEntity = engine.createEntity();

        SpriteComponent spriteCompo = engine.createComponent(SpriteComponent.class);
        if (Perso.YUNA == personage) {
            spriteCompo.initTexture(TextureEnum.YUNA);
        }
        else {
            spriteCompo.initTexture(TextureEnum.YUGO);
        }
        spriteCompo.setCurrentAnimation(null, true);
        spriteCompo.setPosition(pos);
        playerEntity.add(spriteCompo);

        MoveStraightComponent moveStraightCompo = engine.createComponent(MoveStraightComponent.class);
        moveStraightCompo.setSpeed(new Vector2(0,0));
        moveStraightCompo.setBorderCollision(MoveStraightComponent.BorderCollision.STOP);
        playerEntity.add(moveStraightCompo);

        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        playerComponent.perso = personage;
        playerEntity.add(playerComponent);

        GravityComponent gravityComponent = engine.createComponent(GravityComponent.class);
        gravityComponent.setGravity(4);
        playerEntity.add(gravityComponent);

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
