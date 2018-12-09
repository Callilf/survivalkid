package com.calliltbn.desc;

import com.calliltbn.spritesdef.SpriteAnimationEnum;
import com.calliltbn.spritesdef.TextureEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Enemy {
    CATERPILLAR(TextureEnum.CATERPILLAR, TypeEntity.CATERPILLAR, SpriteAnimationEnum.CATERPILLAR_MOVE, 0, 5, 1.8f, 0.3f, "catterDesc", 5, 5, 10, 15),
    CATERPILLAR_PURPLE(TextureEnum.CATERPILLAR_PURPLE, TypeEntity.CATERPILLAR, SpriteAnimationEnum.CATERPILLAR_MOVE, -1, 5, 2.5f, 0.5f, "catter2Desc", 10, 10, 15, 20),
    CIRCULAR_SAW(TextureEnum.CIRCULAR_SAW, TypeEntity.CIRCULAR_SAW, SpriteAnimationEnum.CIRCULAR_SAW_MOVE, 1, 5, 2f, 0.7f, "sawDesc", 15, 20, 25, 30),
    METEOR(TextureEnum.METEOR, TypeEntity.METEOR, null, 1, 5, 1f, 0.5f, "meteorDesc", 10, 10, 15, 20),
    FIRE_GROUND(TextureEnum.FIRE_GROUND, TypeEntity.FIRE_GROUND, SpriteAnimationEnum.FIRE_GROUND_DO, -1, 5, 0f, 0.2f, "groundFireDesc", 1, 1, 2, 3),
    METEOR_FIRE(TextureEnum.METEOR_FIRE, TypeEntity.METEOR_FIRE, null, 2, 5, 1f, 0.5f, "meteorFireDesc", 10, 10, 15, 20),
    BULL(TextureEnum.BULL, TypeEntity.BULL, SpriteAnimationEnum.BULL_MOVE, 2, 5, 15f, 0.9f, "bullDesc", 25, 30, 40, 50);

    private String name;
    private String txtDesc;
    private TextureEnum textureEnum;

    private SpriteAnimationEnum baseAnimation;
    private TypeEntity typeEntity;
    private int level; // the difficulty of the enemy, not the difficulty of the game

    private int zindex;

    private float baseSpeed;
    private float recoveryTime;
    private List<Integer> damage;

    private Enemy(TextureEnum textureEnum, TypeEntity typeEntity, SpriteAnimationEnum baseAnimation, int level, int zindex, float baseSpeed, float recoveryTime, String keyDesc, int... damages) {
        this.name = keyDesc;
        this.textureEnum = textureEnum;
        this.typeEntity = typeEntity;
        this.baseAnimation = baseAnimation;
        this.level = level;
        this.zindex = zindex;
        this.baseSpeed = baseSpeed;
        this.recoveryTime = recoveryTime;
        this.damage = new ArrayList<>();
        for (int damage : damages) {
            this.damage.add(damage);
        }
    }

    public String getName() {
        return name;
    }

    public String getTxtDesc() {
        return txtDesc;
    }

    public TextureEnum getTextureEnum() {
        return textureEnum;
    }

    public SpriteAnimationEnum getBaseAnimation() {
        return baseAnimation;
    }

    public int getLevel() {
        return level;
    }

    public int getZindex() {
        return zindex;
    }

    public TypeEntity getTypeEntity() {
        return typeEntity;
    }

    public float getBaseSpeed() {
        return baseSpeed;
    }

    public float getRecoveryTime() {
        return recoveryTime;
    }

    public int getDamage(int difficulty) {
        // difficulty 1 = easy => return the element at index 0
        // we can set any level of difficulty, it take the last element if not exist
        return damage.get(Math.min(difficulty, damage.size())-1);
    }

    /**
     * @return A map with the enemy sort by level / power
     */
    public static Map<Integer, List<Enemy>> getEnemyDifficultyMap() {
        Map<Integer, List<Enemy>> result = new HashMap<>();
        for (Enemy enemy : Enemy.values()) {
            if (enemy.level >= 0) {
                List<Enemy> listEnemy = result.get(enemy.level);
                if (listEnemy == null) {
                    listEnemy = new ArrayList<>();
                    result.put(enemy.level, listEnemy);
                }
                listEnemy.add(enemy);
            }
        }
        return result;
    }

}
