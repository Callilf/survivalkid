package com.calliltbn.spritesdef;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public enum TextureEnum {

    YUNA("sprites/yuna.png", 6, 4, SpriteAnimationEnum.YUNA_RUN, SpriteAnimationEnum.YUNA_JUMP, SpriteAnimationEnum.YUNA_FALL);

    private TextureRegion textureDefault;

    private Map<com.calliltbn.spritesdef.SpriteAnimationEnum, TextureRegion[]> textureAnimation;

    TextureEnum(String s, int nbWidth, int nbHeight, com.calliltbn.spritesdef.SpriteAnimationEnum... listSpriteAnim) {
        Texture texture = new Texture("sprites/yuna.png");
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
    }

    private TextureRegion[] getTextureRegion(TextureRegion[][] matrice, int[] positions) {
        TextureRegion[] allFrames = new TextureRegion[positions.length];
        int width = matrice[0].length;
        for (int i=0 ; i < positions.length ; i++) {
            allFrames[i] = matrice[positions[i]/width][positions[i]%width];
        }
        return allFrames;
    }


    public TextureRegion getTextureDefault() {
        return textureDefault;
    }

    public Map<com.calliltbn.spritesdef.SpriteAnimationEnum, TextureRegion[]> getTextureAnimation() {
        return textureAnimation;
    }
}
