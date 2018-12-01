package com.calliltbn.spritesdef;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;
import java.util.Map;

public enum TextureEnum {

    YUNA("sprites/yuna.png", new Rectangle(35, 10, 40, 84),
            6, 4, SpriteAnimationEnum.YUNA_RUN, SpriteAnimationEnum.YUNA_JUMP,
            SpriteAnimationEnum.YUNA_FALL, SpriteAnimationEnum.YUNA_KNOCKBACK),
    YUGO("sprites/yugo.png", new Rectangle(30, 10, 40, 84),
            6, 4, SpriteAnimationEnum.YUGO_RUN, SpriteAnimationEnum.YUGO_JUMP,
            SpriteAnimationEnum.YUGO_FALL, SpriteAnimationEnum.YUGO_KNOCKBACK);

    private TextureRegion textureDefault;

    private Rectangle offsets = null;

    private Map<com.calliltbn.spritesdef.SpriteAnimationEnum, TextureRegion[]> textureAnimation;

    /**
     * Construct the TextureEnum
     *
     * @param pathImage path to the image
     * @param hitboxOffsets offsets of the hitbox in % : % start X, % start Y, % width, % height
     * @param nbWidth nb sprite in a line
     * @param nbHeight nb sprite in a column
     * @param listSpriteAnim list animation of the sprite
     */
    TextureEnum(String pathImage, Rectangle hitboxOffsets, int nbWidth, int nbHeight,
                com.calliltbn.spritesdef.SpriteAnimationEnum... listSpriteAnim) {
        Texture texture = new Texture(pathImage);
        if (listSpriteAnim != null) {
            TextureRegion[][] textureRegionMat = TextureRegion.split(texture,
                    texture.getWidth() / nbWidth,
                    texture.getHeight() / nbHeight);

            textureDefault = textureRegionMat[0][0];
            textureAnimation = new HashMap<>();
            for (com.calliltbn.spritesdef.SpriteAnimationEnum spriteAnim: listSpriteAnim) {
                textureAnimation.put(spriteAnim, getTextureRegion(textureRegionMat, spriteAnim.listeFrames));
            }
        }
        else {
            textureDefault = new TextureRegion(texture);
        }
        if (hitboxOffsets != null) {
            offsets = new Rectangle(texture.getWidth() / nbWidth * hitboxOffsets.x / 100,
                    texture.getHeight() / nbHeight * hitboxOffsets.y / 100,
                    texture.getWidth() / nbWidth * hitboxOffsets.width / 100,
                    texture.getHeight() / nbHeight * hitboxOffsets.height / 100);
        }
    }

    private TextureRegion[] getTextureRegion(TextureRegion[][] matrice, int[] positions) {
        TextureRegion[] allFrames = new TextureRegion[positions.length];
        int width = matrice[0].length;
        for (int i=0 ; i < positions.length ; i++) {
            allFrames[i] = matrice[positions[i]/width][positions[i]%width];
        }
        return allFrames;
    }

    public Rectangle getOffsets() {
        return offsets;
    }

    public TextureRegion getTextureDefault() {
        return textureDefault;
    }

    public Map<com.calliltbn.spritesdef.SpriteAnimationEnum, TextureRegion[]> getTextureAnimation() {
        return textureAnimation;
    }
}
