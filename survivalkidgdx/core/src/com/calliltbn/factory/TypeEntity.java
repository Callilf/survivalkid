package com.calliltbn.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.calliltbn.factory.ClassEntity.*;

public enum TypeEntity {
    // enemy
    CATERPILLAR(ENEMY, 0), CIRCULAR_SAW(ENEMY, 1), METEOR(ENEMY, 1), FIRE_METEOR(ENEMY, 2), BULL(ENEMY, 2), FIRE_GROUND(ENEMY),
    // item
    BALLON(ITEM), CORRIDA(ITEM), MEDKIT(ITEM), MEDKIT_SUPER(ITEM), SLOW_MOVE(ITEM), SPEED_UP(ITEM), SPEED_DOWN(ITEM),
    // decoration
    SMOKE_WHITE_LARGE(DECORATION), SMOKE_WHITE_SMALL(DECORATION), SMOKE_BROWN_LARGE(DECORATION),
    PARTICLE_DUST_BROWN(DECORATION), PARTICLE_SMOKE_WHITE_ROUND(DECORATION)
    ;

    private int power;
    private ClassEntity category;

    TypeEntity(ClassEntity category, int power) {
        this.power = power;
        this.category = category;
    }

    TypeEntity(ClassEntity category) {
        this(category, -1);
    }

    /**
     * @return A map with the enemy sort by difficulty
     */
    public static Map<Integer, List<TypeEntity>> getEnemyDifficultyMap() {
        Map<Integer, List<TypeEntity>> result = new HashMap<>();
        for (TypeEntity enemy : TypeEntity.values()) {
            if (enemy.category == ENEMY && enemy.power >= 0) {
                List<TypeEntity> listEnemy = result.get(enemy.power);
                if (listEnemy == null) {
                    listEnemy = new ArrayList<>();
                    result.put(enemy.power, listEnemy);
                }
                listEnemy.add(enemy);
            }
        }
        return result;
    }

    /**
     * @return A list with all the items
     */
    public static List<TypeEntity> getAllItems() {
        List<TypeEntity> result = new ArrayList<>();
        for (TypeEntity item : TypeEntity.values()) {
            if (item.category == ITEM) {
                result.add(item);
            }
        }
        return result;
    }
}
