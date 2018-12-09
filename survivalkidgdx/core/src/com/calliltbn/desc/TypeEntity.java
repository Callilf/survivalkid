package com.calliltbn.desc;

import java.util.ArrayList;
import java.util.List;

import static com.calliltbn.desc.ClassEntity.DECORATION;
import static com.calliltbn.desc.ClassEntity.ENEMY;
import static com.calliltbn.desc.ClassEntity.ITEM;

public enum TypeEntity {
    // enemy
    CATERPILLAR(ENEMY), CIRCULAR_SAW(ENEMY), METEOR(ENEMY), METEOR_FIRE(ENEMY), BULL(ENEMY), FIRE_GROUND(ENEMY),
    // item
    BALLON(ITEM), CORRIDA(ITEM), MEDKIT(ITEM), MEDKIT_SUPER(ITEM), SLOW_MOVE(ITEM), SPEED_UP(ITEM), SPEED_DOWN(ITEM),
    // decoration
    SMOKE_WHITE_LARGE(DECORATION), SMOKE_WHITE_SMALL(DECORATION), SMOKE_BROWN_LARGE(DECORATION),
    METEOR_EXPLOSION(DECORATION), METEOR_FIRE_EXPLOSION(DECORATION), FIRE_GROUND_DIE(DECORATION),
    PARTICLE_DUST_BROWN(DECORATION), PARTICLE_SMOKE_WHITE_ROUND(DECORATION)
    ;

    private int power;
    private ClassEntity category;

    TypeEntity(ClassEntity category) {
        this.category = category;
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
