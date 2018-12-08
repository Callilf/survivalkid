package com.calliltbn.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.calliltbn.GameScreen;
import com.calliltbn.components.CollideComponent;
import com.calliltbn.components.GravityComponent;
import com.calliltbn.components.HealthComponent;
import com.calliltbn.components.MoveStraightComponent;
import com.calliltbn.components.MoveStraightComponent.BorderCollision;
import com.calliltbn.components.PlayerComponent;
import com.calliltbn.components.PlayerComponent.Perso;
import com.calliltbn.components.SpriteComponent;
import com.calliltbn.components.StateComponent;
import com.calliltbn.components.SuccessorComponent;
import com.calliltbn.desc.TypeEntity;
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
    private PooledEngine engine;

    private EnemyFactory enemyFactory;
    private DecorationFactory decorationFactory;

    /**
     * Constructor.
     * @param e the engine
     */
    public EntityFactory(PooledEngine e) {
        this.engine = e;
        enemyFactory = new EnemyFactory(e);
        decorationFactory = new DecorationFactory(e);
    }

    public Entity createEnemy(TypeEntity enemy) {
        return enemyFactory.createCaterpillar();
    }

    public Entity createSuccessor(Entity parentEntity, TypeEntity childEntity) {
        switch (childEntity) {
            case SMOKE_WHITE_SMALL:
                return decorationFactory.createSmokeWhiteSmall(parentEntity);
            case SMOKE_WHITE_LARGE:
                return decorationFactory.createSmokeWhiteLarge(parentEntity);
        }
        return enemyFactory.createCaterpillar();
    }

    /**
     * Create the player.
     * @param pos the position
     * @return the player entity
     */
    public Entity createPlayer(Vector2 pos, Perso personage) {
        Entity playerEntity = engine.createEntity();

        TextureEnum texture = (Perso.YUNA == personage)? TextureEnum.YUNA : TextureEnum.YUGO;
        SpriteComponent spriteComponent =
                SpriteComponent.make(engine, texture, pos, null, 10);
        playerEntity.add(spriteComponent);
        playerEntity.add(
                MoveStraightComponent.make(engine, new Vector2(0,0), BorderCollision.STOP));
        playerEntity.add(PlayerComponent.make(engine, personage));
        playerEntity.add(StateComponent.make(engine));
        playerEntity.add(GravityComponent.make(engine, 4));
        playerEntity.add(HealthComponent.make(engine, 30));
        playerEntity.add(CollideComponent.make(engine, 10, 1, spriteComponent));
        playerEntity.add(SuccessorComponent.make(engine, TypeEntity.SMOKE_WHITE_LARGE));

        engine.addEntity(playerEntity);

        return playerEntity;
    }

    /**
     * Function for test
     */
    public Entity createBouncePlayer() {
        Entity bounceEntity = engine.createEntity();

        SpriteComponent spriteComponent =
                SpriteComponent.make(engine, TextureEnum.YUNA, new Vector2(150,200),
                        SpriteAnimationEnum.YUNA_RUN, 8);
        bounceEntity.add(spriteComponent);
        bounceEntity.add(
                MoveStraightComponent.make(engine, new Vector2(5,4), BorderCollision.BOUNCE));
        bounceEntity.add(CollideComponent.make(engine, 10, 2, spriteComponent));

        engine.addEntity(bounceEntity);

        return bounceEntity;
    }

    public void createTestSprite() {
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.BALLOON_CRATE, new Vector2(0,300),
                        SpriteAnimationEnum.BALLON_MOVE, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.BALLOON_CRATE, new Vector2(75,300),
                        SpriteAnimationEnum.BALLON_EXPLODE, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.CATERPILLAR, new Vector2(160,300),
                        SpriteAnimationEnum.CATERPILLAR_MOVE, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.CATERPILLAR_PURPLE, new Vector2(230,300),
                        SpriteAnimationEnum.CATERPILLAR_MOVE, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.BULL, new Vector2(180,360),
                        SpriteAnimationEnum.BULL_MOVE, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.BULL_WARNING, new Vector2(300,300),
                        SpriteAnimationEnum.BULL_WARN_ANIM, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.CIRCULAR_SAW, new Vector2(380,300),
                        SpriteAnimationEnum.CIRCULAR_SAW_MOVE, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.METEOR, new Vector2(450,400),
                        null, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.METEOR_FIRE, new Vector2(450,300),
                        null, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.METEOR_EXPLOSION, new Vector2(550,300),
                        SpriteAnimationEnum.METEOR_EXPLODE, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.METEOR_FIRE_EXPLOSION, new Vector2(700,300),
                        SpriteAnimationEnum.METEOR_FIRE_EXPLODE, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.FIRE_TRAIL, new Vector2(460,350),
                        SpriteAnimationEnum.FIRE_TRAIL_DO, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.FIRE_GROUND, new Vector2(150,400),
                        SpriteAnimationEnum.FIRE_GROUND_DO, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.FIRE_GROUND, new Vector2(150,350),
                        SpriteAnimationEnum.FIRE_GROUND_DIE, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.SMOKE_WHITE_LARGE, new Vector2(550,200),
                        SpriteAnimationEnum.SMOKE_ALL_DIE, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.SMOKE_BROWN_LARGE, new Vector2(700,200),
                        SpriteAnimationEnum.SMOKE_ALL_DIE, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.SMOKE_WHITE_SMALL, new Vector2(450,230),
                        SpriteAnimationEnum.SMOKE_ALL_DIE, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.PARTICLE_DUST_BROWN, new Vector2(370,230),
                        SpriteAnimationEnum.PARTICLE_DUST_FAINT, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.PARTICLE_SMOKE_WHITE_ROUND, new Vector2(170,230),
                        SpriteAnimationEnum.PARTICLE_SMOKE_FAINT1, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.PARTICLE_SMOKE_WHITE_ROUND, new Vector2(235,230),
                        SpriteAnimationEnum.PARTICLE_SMOKE_FAINT2, 8));
        createTestEntityFromSprite(
                SpriteComponent.make(engine, TextureEnum.PARTICLE_SMOKE_WHITE_ROUND, new Vector2(300,230),
                        SpriteAnimationEnum.PARTICLE_SMOKE_FAINT3, 8));
    }

    private void createTestEntityFromSprite(SpriteComponent spriteComponent) {
        Entity testEntity = engine.createEntity();
        testEntity.add(spriteComponent);
        testEntity.add(CollideComponent.make(engine, 0, 0, spriteComponent));
        engine.addEntity(testEntity);
    }


    private TextureRegion[] getTextureRegion(TextureRegion[][] matrice, int width, int[] positions) {
        TextureRegion[] allFrames = new TextureRegion[positions.length];
        for (int i=0 ; i < positions.length ; i++) {
            allFrames[i] = matrice[positions[i]/width-1][positions[i]%width];
        }
        return allFrames;
    }

    /**
     * Generate a random X position.
     * @param isFlip if false, generate a position in the first half. Else in the second half
     * @return a random X position in the game, excluded the marginPct of each side
     */
    public static int getRandomPositionX(boolean isFlip) {
        int marginPct = 1;
        int margin = GameScreen.SCREEN_W * marginPct / 100;
        int result = (int)(Math.random() * (GameScreen.SCREEN_W - 2 * margin)) + margin;
        if (result < GameScreen.SCREEN_W / 2 && isFlip) {
            result = GameScreen.SCREEN_W - result;
        }
        return result;
    }

    /**
     * Generate a random Y position.
     *
     * @return a random Y position in the game (from ground to top), excluded the marginPct of each side
     */
    public static int getRandomPositionY() {
        int marginPct = 5;
        int heightScreenPlay = GameScreen.SCREEN_H - GameScreen.FLOOR_Y;
        int margin = heightScreenPlay * marginPct / 100;
        return (int)(Math.random() * (heightScreenPlay - 2 * margin)) + margin;
    }
}
