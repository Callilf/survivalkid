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
import com.calliltbn.components.SubstitutesComponent;
import com.calliltbn.components.SuccessorComponent;
import com.calliltbn.desc.Enemy;
import com.calliltbn.desc.TypeEntity;
import com.calliltbn.param.Difficulty.DifficultyEnum;
import com.calliltbn.spritesdef.TextureEnum;

public class EnemyFactory {

    /** The gdx pooled engine. */
    private PooledEngine engine;

    private int difficulty;

    /**
     * Constructor.
     * @param e the engine
     */
    public EnemyFactory(PooledEngine e) {
        this.engine = e;
        this.difficulty = DifficultyEnum.NORMAL.getValue();
    }

    public Entity createMeteor() {
        Entity entity = engine.createEntity();

        Enemy enemy = Enemy.METEORE;

        float speed = enemy.getBaseSpeed();
        int damage = enemy.getDamage(difficulty);
        float recoveryTime = enemy.getRecoveryTime();
        TextureEnum texture = enemy.getTextureEnum();
        Vector2 position = new Vector2();

        SpriteComponent spriteComponent =
                SpriteComponent.make(engine, texture, position, enemy.getBaseAnimation(), enemy.getZindex());
        boolean isFlip = initPosition(texture, position, false,  false, false);
        if (isFlip) {
            speed *= -1;
            spriteComponent.setFlip(true);
        }

        entity.add(spriteComponent);
        entity.add(
                MoveStraightComponent.make(engine, new Vector2(speed,-2), BorderCollision.DIE_TOUCH));
        entity.add(CollideComponent.make(engine, damage, recoveryTime, spriteComponent));
        entity.add(SuccessorComponent.make(engine, TypeEntity.METEOR_EXPLOSION));

        // second state
        entity.add(SubstitutesComponent.make(engine, 1, enemy.getTypeEntity(),
                MoveStraightComponent.make(engine, new Vector2(speed,-14), BorderCollision.DIE_TOUCH)));

        engine.addEntity(entity);
        return entity;
    }

    public Entity createCaterpillar() {
        Entity entity = engine.createEntity();

        Enemy enemy = Enemy.CATERPILLAR;
        float speed = enemy.getBaseSpeed();
        speed = (float) (Math.random() * 1f) + speed;
        if (speed > 2.5f) {
            enemy = Enemy.CATERPILLAR_PURPLE;
        }

        int damage = enemy.getDamage(difficulty);
        float recoveryTime = enemy.getRecoveryTime();
        TextureEnum texture = enemy.getTextureEnum();

        Vector2 position = new Vector2();
        SpriteComponent spriteComponent =
                SpriteComponent.make(engine, texture, position, enemy.getBaseAnimation(), enemy.getZindex());

        // init position
        boolean isFlip = initPosition(texture, position, true,  false, false);
        if (isFlip) {
            speed *= -1;
            spriteComponent.setFlip(true);
        }

        entity.add(spriteComponent);

        entity.add(
                MoveStraightComponent.make(engine, new Vector2(speed,0), BorderCollision.DIE_OUT));
        entity.add(GravityComponent.make(engine, 2));
        entity.add(CollideComponent.make(engine, damage, recoveryTime, spriteComponent));
        entity.add(HealthComponent.make(engine, 1));
        entity.add(SuccessorComponent.make(engine, TypeEntity.SMOKE_WHITE_SMALL));

        engine.addEntity(entity);
        return entity;
    }

    /**
     * Init position of the enemy
     *
     * @param texture texture of the enemy (used for the size of the sprite)
     * @param position the position to set
     * @param startOnSide if true, start on the right of left.
     * @return if the enemy is flipped (move to the left)
     */
    private boolean initPosition(TextureEnum texture, Vector2 position, boolean startOnSide,
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
            position.x = EntityFactory.getRandomPositionX();
            isFlip = position.x > GameScreen.SCREEN_W / 2;
            position.y = GameScreen.SCREEN_H + texture.getTextureDefault().getRegionHeight();
        }
        if (randomY) {
            position.y = EntityFactory.getRandomPositionY();
        }
        return isFlip;
    }


}
