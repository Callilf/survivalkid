package com.calliltbn.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.calliltbn.components.CollideComponent;
import com.calliltbn.components.GravityComponent;
import com.calliltbn.components.HealthComponent;
import com.calliltbn.components.MoveStraightComponent;
import com.calliltbn.components.MoveStraightComponent.BorderCollision;
import com.calliltbn.components.PlayerComponent;
import com.calliltbn.components.PlayerComponent.Perso;
import com.calliltbn.components.SpriteComponent;
import com.calliltbn.components.StateComponent;
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
    public Entity createPlayer(Vector2 pos, Perso personage) {
        Entity playerEntity = engine.createEntity();

        TextureEnum texture = (Perso.YUNA == personage)? TextureEnum.YUNA : TextureEnum.YUGO;
        playerEntity.add(
                SpriteComponent.make(engine, texture, pos, null, 10));
        playerEntity.add(
                MoveStraightComponent.make(engine, new Vector2(0,0), BorderCollision.STOP));
        playerEntity.add(PlayerComponent.make(engine, personage));
        playerEntity.add(StateComponent.make(engine));
        playerEntity.add(GravityComponent.make(engine, 4));
        playerEntity.add(HealthComponent.make(engine, 30));
        playerEntity.add(CollideComponent.make(engine, 10, 1));

        engine.addEntity(playerEntity);

        return playerEntity;
    }

    /**
     * Function for test
     */
    public Entity createBouncePlayer() {
        Entity bounceEntity = engine.createEntity();

        bounceEntity.add(
                SpriteComponent.make(engine, TextureEnum.YUNA, new Vector2(150,200),
                        SpriteAnimationEnum.YUNA_RUN, 8));
        bounceEntity.add(
                MoveStraightComponent.make(engine, new Vector2(5,4), BorderCollision.BOUNCE));
        bounceEntity.add(CollideComponent.make(engine, 10, 2));

        engine.addEntity(bounceEntity);

        return bounceEntity;
    }

    private TextureRegion[] getTextureRegion(TextureRegion[][] matrice, int width, int[] positions) {
        TextureRegion[] allFrames = new TextureRegion[positions.length];
        for (int i=0 ; i < positions.length ; i++) {
            allFrames[i] = matrice[positions[i]/width-1][positions[i]%width];
        }
        return allFrames;
    }
}
