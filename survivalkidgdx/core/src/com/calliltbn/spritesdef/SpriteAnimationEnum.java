package com.calliltbn.spritesdef;

public enum SpriteAnimationEnum {
    YUNA_RUN(new int[] { 1, 2, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14 }, 0.05f),
    YUNA_JUMP(new int[] { 20, 21, 22 }, 0.1f);


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
