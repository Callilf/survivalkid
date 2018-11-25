package com.calliltbn.spritesdef;

public enum SpriteAnimationEnum {
    YUNA_RUN(new int[] { 1, 2, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14 }, 0.05f),
    YUNA_JUMP(new int[] { 20, 21, 22 }, 0.1f),
    YUNA_FALL(new int[] { 17, 18, 19 }, 0.1f),
    YUNA_KNOCKBACK(new int[] { 23 }, 0.1f),

    YUGO_RUN(new int[] { 1, 2, 3, 5, 6, 7, 9, 10, 11, 12, 13, 14 }, 0.05f),
    YUGO_JUMP(new int[] { 19, 18, 17 }, 0.1f),
    YUGO_FALL(new int[] { 19 }, 0.1f),
    YUGO_KNOCKBACK(new int[] { 20 }, 0.1f);

    float fps;
    int[] listeFrames;

    SpriteAnimationEnum(int[] listeFrames, float fps) {
        this.fps = fps;
        this.listeFrames = listeFrames;
    }

    public float getFps() {
        return fps;
    }
}
