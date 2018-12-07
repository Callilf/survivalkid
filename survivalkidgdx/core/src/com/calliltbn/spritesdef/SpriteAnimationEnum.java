package com.calliltbn.spritesdef;

public enum SpriteAnimationEnum {
    // personages
    YUNA_RUN(new int[] { 1, 2, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14 }, 20),
    YUNA_JUMP(new int[] { 20, 21, 22 }, 13),
    YUNA_FALL(new int[] { 17, 18, 19 }, 13),
    YUNA_KNOCKBACK(new int[] { 23 }, 13),

    YUGO_RUN(new int[] { 1, 2, 3, 5, 6, 7, 9, 10, 11, 12, 13, 14 }, 20),
    YUGO_JUMP(new int[] { 19, 18, 17 }, 13),
    YUGO_FALL(new int[] { 19 }, 13),
    YUGO_KNOCKBACK(new int[] { 20 }, 13),

    YUNA_CORRIDA_PREP(new int[] { 0, 1, 2, 3, 4 }, 15),
    YUNA_CORRIDA_OLE(new int[] { 5, 6, 7, 8, 9, 10, 11, 12 }, 30),
    YUGO_CORRIDA_PREP(new int[] { 0, 1, 2, 3, 4 }, 15),
    YUGO_CORRIDA_OLE(new int[] { 5, 6, 7, 8, 9, 10, 11, 12 }, 30),

    // enemies
    BULL_MOVE(new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, 30),
    CIRCULAR_SAW_MOVE(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, 20),
    CATERPILLAR_MOVE(new int[] { 0, 1, 2, 3, 2, 1 }, 13),
    // CATERPILLAR_PURPLE_MOVE(new int[] { 0, 1, 2, 3, 2, 1 }, 13), // reuse CATERPILLAR_MOVE
    FIRE_GROUND_DO(new int[] { 0, 1, 2, 3, 4, 5, 6 }, 15),
    FIRE_GROUND_DIE(new int[] { 7, 8, 9, 10, 11 }, 10),

    // enemies decoration and death animations
    BULL_WARN_ANIM(new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, 30),
    METEOR_EXPLODE(new int[] { 0, 1, 2, 3, 4, 5 }, 20),
    METEOR_FIRE_EXPLODE( new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 }, 20),
    FIRE_TRAIL_DO( new int[] { 0, 1, 2, 3, 4, 5 }, 15),

    PARTICLE_DUST_FAINT(new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, 8),
    PARTICLE_SMOKE_FAINT1(new int[] { 0, 1, 2, 3, 4 }, 5),
    PARTICLE_SMOKE_FAINT2(new int[] { 5, 6, 7, 8, 9 }, 5),
    PARTICLE_SMOKE_FAINT3(new int[] { 10, 11, 12, 13, 14 }, 5),

    SMOKE_BROWN_LARGE_DIE(new int[] { 0, 1, 2, 3, 4, 5, 6 }, 25),
    SMOKE_WHITE_LARGE_DIE(new int[] { 0, 1, 2, 3, 4, 5, 6 }, 25),
    SMOKE_WHITE_SMALL_DIE(new int[] { 0, 1, 2, 3, 4, 5, 6 }, 25),

    // manage items
    BALLON_MOVE(new int[] { 0, 1, 2, 1, 0, 3, 4, 3 }, 8),
    BALLON_EXPLODE(new int[] { 5, 6, 7, 8, 9, 10 }, 40), // use loop=false
    CRATE_EXPLODE(new int[] { 0, 1, 2, 3, 4, 5 }, 20),
    BAG_LOCK(new int[] { 1 }, 30),
    BAG_UNLOCK(new int[] { 0 }, 30);


    float frequency;
    int[] listeFrames;

    SpriteAnimationEnum(int[] listeFrames, int fps) {
        this.frequency = 1f / fps;
        this.listeFrames = listeFrames;
    }

    public float getFrequency() {
        return frequency;
    }
}
