package com.calliltbn.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
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
        switch (enemy) {
            case CIRCULAR_SAW:
                return enemyFactory.createCircularSaw();
            case CATERPILLAR:
                return enemyFactory.createCaterpillar();
            case METEOR:
                return enemyFactory.createMeteor();
            case BULL:
                return enemyFactory.createBull();
            case METEOR_FIRE:
                return enemyFactory.createFireMeteor();
        }
        return enemyFactory.createCircularSaw();
    }

    public Entity createSuccessor(Entity parentEntity, TypeEntity childEntity) {
        switch (childEntity) {
            case SMOKE_WHITE_SMALL:
                return decorationFactory.createSmokeWhiteSmall(parentEntity);
            case SMOKE_WHITE_LARGE:
                return decorationFactory.createSmokeWhiteLarge(parentEntity);
            case SMOKE_BROWN_LARGE:
                return decorationFactory.createSmokeBrownLarge(parentEntity);
            case METEOR_EXPLOSION:
                return decorationFactory.createMeteorExplosion(parentEntity);
            case METEOR_FIRE_EXPLOSION:
                return decorationFactory.createMeteorFireExplosion(parentEntity);
            case FIRE_GROUND:
                return enemyFactory.createFireGround(parentEntity);
            case FIRE_GROUND_DIE:
                return decorationFactory.createFireGoundDie(parentEntity);
        }
        Gdx.app.log("EntityFactory", "no successor define " + childEntity);
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
        playerEntity.add(HealthComponent.make(engine, 50));
        playerEntity.add(CollideComponent.make(engine, 1, 1, spriteComponent));
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
     * @return a random X position in the game, excluded the marginPct of each side
     */
    public static int getRandomPositionX() {
        int marginPct = 1;
        int margin = GameScreen.SCREEN_W * marginPct / 100;
        return (int)(Math.random() * (GameScreen.SCREEN_W - 2 * margin)) + margin;
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

    /**
     * Init position of the enemy
     *
     * @param texture texture of the enemy (used for the size of the sprite)
     * @param position the position to set
     * @param startOnSide if true, start on the right of left.
     * @return if the enemy is flipped (move to the left)
     */
    static boolean initPosition(TextureEnum texture, Vector2 position, boolean startOnSide,
                                boolean startOnFloor, boolean randomY) {
        boolean isFlip;
        if (startOnSide) {
            int random = (int) (Math.random() * 100);
            isFlip = random < 50;

            if (isFlip) {
                position.x = GameScreen.SCREEN_W; // start at the right
            } else {
                position.x = -texture.getTextureDefault().getRegionWidth(); // start at the left
            }

            if (startOnFloor || random < 34 || random > 65) {
                position.y = GameScreen.FLOOR_Y; // start on the floor
            } else {
                // case where an entity can start by falling from the top of the screen
                position.y = GameScreen.SCREEN_H - 20; // start on the top
            }
        }
        else {
            // start at the top. Generate a random X position
            position.x = getRandomPositionX();
            isFlip = position.x > GameScreen.SCREEN_W / 2;
            position.y = GameScreen.SCREEN_H + texture.getTextureDefault().getRegionHeight();
        }
        if (randomY) {
            position.y = getRandomPositionY();
        }
        return isFlip;
    }

    //public static Vector2 getPositionInMiddle(Sprite parent, TextureEnum)
}
