package com.calliltbn.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.calliltbn.GameScreen;
import com.calliltbn.components.BlinkComponent;
import com.calliltbn.components.CollideComponent;
import com.calliltbn.components.ExpirationComponent;
import com.calliltbn.components.FollowerComponent;
import com.calliltbn.components.FriendlyFireComponent;
import com.calliltbn.components.GravityComponent;
import com.calliltbn.components.HealthComponent;
import com.calliltbn.components.KnockbackComponent;
import com.calliltbn.components.MoveComponent;
import com.calliltbn.components.MoveComponent.BorderCollision;
import com.calliltbn.components.MoveOnLineComponent;
import com.calliltbn.components.SpriteComponent;
import com.calliltbn.components.SubstitutesComponent;
import com.calliltbn.components.SuccessorComponent;
import com.calliltbn.desc.Enemy;
import com.calliltbn.desc.TypeEntity;
import com.calliltbn.param.Difficulty.DifficultyEnum;
import com.calliltbn.spritesdef.SpriteAnimationEnum;
import com.calliltbn.spritesdef.TextureEnum;
import com.calliltbn.util.Mappers;

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

    private Entity createMeteorTrailFire(Entity meteorEntity, Sprite spriteMeteor, boolean isFlip) {
        Entity entity = engine.createEntity();

        TextureEnum texture = TextureEnum.FIRE_TRAIL;
        texture.getTextureDefault().getRegionWidth();

        float shiftX = spriteMeteor.getWidth() * (isFlip? 1.35f/3f  : 1.65f/3f) - texture.getTextureDefault().getRegionWidth() / 2f;
        float shiftY = texture.getTextureDefault().getRegionHeight() * 1.6f/4f;
        Vector2 shift = new Vector2(shiftX, shiftY);

        Vector2 position = new Vector2(spriteMeteor.getX() + shiftX,
                spriteMeteor.getY() + shiftY);

        //  Random rand = new Random();
        // emitter.setX(fireAnim.getX() + rand.nextInt(fireAnim.getWidth()));
        // emitter.setY(fireAnim.getY() + fireAnim.getHeight()/2);

        SpriteComponent spriteComponent =
                SpriteComponent.make(engine, texture, position, SpriteAnimationEnum.FIRE_TRAIL_DO, 2);
        spriteComponent.setFlip(isFlip);
        entity.add(spriteComponent);

        meteorEntity.add(FollowerComponent.make(engine, shift, entity));

        engine.addEntity(entity);
        return entity;
    }

    public Entity createMeteor() {
        return createCommonMeteor(false);
    }

    public Entity createFireMeteor() {
        return createCommonMeteor(true);
    }

    public Entity createCommonMeteor(boolean isFire) {
        Entity entity = engine.createEntity();

        Enemy enemy = isFire? Enemy.METEOR_FIRE : Enemy.METEOR;

        float speed = enemy.getBaseSpeed();
        int damage = enemy.getDamage(difficulty);
        float recoveryTime = enemy.getRecoveryTime();
        TextureEnum texture = enemy.getTextureEnum();
        Vector2 position = new Vector2();

        SpriteComponent spriteComponent =
                SpriteComponent.make(engine, texture, position, enemy.getBaseAnimation(), enemy.getZindex());
        boolean isFlip = EntityFactory.initPosition(texture, position, false,  false, false);
        if (isFlip) {
            speed *= -1;
            spriteComponent.setFlip(true);
        }

        entity.add(spriteComponent);
        entity.add(
                MoveComponent.make(engine, new Vector2(speed,-2), BorderCollision.DIE_TOUCH));
        entity.add(HealthComponent.make(engine, 1));
        entity.add(CollideComponent.make(engine, damage, recoveryTime, spriteComponent));

        // second state
        entity.add(SubstitutesComponent.make(engine, 1, enemy.getTypeEntity(),
                MoveComponent.make(engine, new Vector2(speed,-14), BorderCollision.DIE_TOUCH)));

        // add fire trail decoration
        Entity fireTrail = createMeteorTrailFire(entity, spriteComponent.getSprite(), !isFlip);

        // successor
        entity.add(SuccessorComponent.make(engine,
                isFire? TypeEntity.METEOR_FIRE_EXPLOSION : TypeEntity.METEOR_EXPLOSION));

        engine.addEntity(entity);
        return entity;
    }

    public Entity createFireGround(Entity parent) {
        Entity entity = engine.createEntity();

        Enemy enemy = Enemy.FIRE_GROUND;
        int damage = enemy.getDamage(difficulty);
        float recoveryTime = enemy.getRecoveryTime();
        TextureEnum texture = enemy.getTextureEnum();

        Sprite spriteParent = Mappers.getComponent(SpriteComponent.class, parent).getSprite();
        Vector2 position = new Vector2(spriteParent.getX() + spriteParent.getWidth() / 2 - texture.getTextureDefault().getRegionWidth() / 1.8f,
                spriteParent.getY());

        SpriteComponent spriteComponent =
                SpriteComponent.make(engine, texture, position, enemy.getBaseAnimation(), enemy.getZindex());
        entity.add(spriteComponent);
        entity.add(HealthComponent.make(engine, 25)); // to die when the bull walk on it
        entity.add(CollideComponent.make(engine, damage, recoveryTime, spriteComponent));
        entity.add(
                MoveComponent.make(engine, new Vector2(0,-2), BorderCollision.STOP));
        entity.add(ExpirationComponent.make(engine, 3f));
        entity.add(SuccessorComponent.make(engine, TypeEntity.FIRE_GROUND_DIE));

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
        boolean isFlip = EntityFactory.initPosition(texture, position, true,  false, false);
        if (isFlip) {
            speed *= -1;
            spriteComponent.setFlip(true);
        }

        entity.add(spriteComponent);

        entity.add(
                MoveComponent.make(engine, new Vector2(speed,0), BorderCollision.DIE_OUT));
        entity.add(GravityComponent.make(engine, 2));
        entity.add(CollideComponent.make(engine, damage, recoveryTime, spriteComponent));
        entity.add(HealthComponent.make(engine, 1));
        entity.add(SuccessorComponent.make(engine, TypeEntity.SMOKE_WHITE_SMALL));

        engine.addEntity(entity);
        return entity;
    }

    public Entity createBull() {
        Entity entity = engine.createEntity();

        Enemy enemy = Enemy.BULL;
        float speed = enemy.getBaseSpeed();
        int damage = enemy.getDamage(difficulty);
        float recoveryTime = enemy.getRecoveryTime();
        TextureEnum texture = enemy.getTextureEnum();
        Vector2 position = new Vector2();
        boolean isFlip = EntityFactory.initPosition(texture, position, true,  true, false);

        // initialize warning sign
        TextureEnum warnText = TextureEnum.BULL_WARNING;
        Vector2 posWarn = new Vector2(5, 120);
        Vector2 speedKnockback = new Vector2(30, 50);
        SpriteComponent spriteWarnComponent =
                SpriteComponent.make(engine, TextureEnum.BULL_WARNING, posWarn, SpriteAnimationEnum.BULL_WARN_ANIM, 10);
        if (isFlip) {
            posWarn.x = GameScreen.SCREEN_W - warnText.getTextureDefault().getRegionWidth() - posWarn.x;
            spriteWarnComponent.setFlip(true);
            speedKnockback.x *= -1;
        }
        entity.add(spriteWarnComponent);
        entity.add(BlinkComponent.make(engine, 1/12f, 1/12f));

        // initialize bull as substitute
        SpriteComponent spriteComponent =
                SpriteComponent.make(engine, texture, position, enemy.getBaseAnimation(), enemy.getZindex());
        if (isFlip) {
            speed *= -1;
            spriteComponent.setFlip(true);
        }
        MoveComponent moveComponent = MoveComponent.make(engine, new Vector2(speed,0), BorderCollision.DIE_OUT);
        CollideComponent collideComponent = CollideComponent.make(engine, damage, recoveryTime, spriteComponent);
        collideComponent.setInitiative(true);
        HealthComponent healthComponent = HealthComponent.make(engine, 30);
        FriendlyFireComponent friendlyFireComponent = FriendlyFireComponent.make(engine);
        KnockbackComponent knockbackComponent = KnockbackComponent.make(engine, speedKnockback,0.4f);
        SuccessorComponent successorComponent = SuccessorComponent.make(engine, TypeEntity.SMOKE_WHITE_LARGE);

        SubstitutesComponent substitutesComponent = SubstitutesComponent.make(engine, 2, TypeEntity.BULL,
                spriteComponent, moveComponent, collideComponent, healthComponent, friendlyFireComponent, knockbackComponent, successorComponent);

        // add substitute component
        entity.add(substitutesComponent);
        engine.addEntity(entity);
        return entity;
    }

    public Entity createCircularSaw() {
        Entity entity = engine.createEntity();

        Enemy enemy = Enemy.CIRCULAR_SAW;
        float speed = enemy.getBaseSpeed();
        int damage = enemy.getDamage(difficulty);
        float recoveryTime = enemy.getRecoveryTime();
        TextureEnum texture = enemy.getTextureEnum();
        Vector2 position = new Vector2();
        boolean isFlip = EntityFactory.initPosition(texture, position, true,  false, true);
        SpriteComponent spriteComponent =
                SpriteComponent.make(engine, texture, position, enemy.getBaseAnimation(), enemy.getZindex());
        if (isFlip) {
            speed *= -1;
            spriteComponent.setFlip(true);
        }
        MoveComponent moveComponent = MoveComponent.make(engine, new Vector2(speed,0), BorderCollision.DIE_TOUCH);
        CollideComponent collideComponent = CollideComponent.make(engine, damage, recoveryTime, spriteComponent);
        HealthComponent healthComponent = HealthComponent.make(engine, 25);
        SuccessorComponent successorComponent = SuccessorComponent.make(engine, TypeEntity.SMOKE_WHITE_SMALL);

        // put all component of the saw in the substitute component to delay the apparition when the line is draw
        SubstitutesComponent substitutesComponent = SubstitutesComponent.make(engine, 1, TypeEntity.CIRCULAR_SAW,
                spriteComponent, moveComponent, collideComponent, healthComponent, successorComponent);
        entity.add(substitutesComponent);
        // add the line component
        entity.add(MoveOnLineComponent.make(engine, spriteComponent.getSprite(), speed));

        engine.addEntity(entity);
        return entity;

    }

}
