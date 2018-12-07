package com.calliltbn.spritesdef;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;
import java.util.Map;

import static com.calliltbn.spritesdef.SpriteAnimationEnum.*;

public enum TextureEnum {

    // personages
    YUNA("sprites/yuna.png", new Rectangle(35, 8, 40, 84),
            6, 4, YUNA_RUN, YUNA_JUMP, YUNA_FALL, YUNA_KNOCKBACK),
    YUGO("sprites/yugo.png", new Rectangle(30, 8, 40, 84),
            6, 4, YUGO_RUN, YUGO_JUMP, YUGO_FALL, YUGO_KNOCKBACK),

    YUNA_CORRIDA("sprites/yuna_corrida.png", null, 7, 2, YUNA_CORRIDA_PREP, YUNA_CORRIDA_OLE),
    YUGO_CORRIDA("sprites/yugo_corrida.png", null, 7, 2, YUGO_CORRIDA_PREP, YUGO_CORRIDA_OLE),

    // enemies
    BULL("sprites/bull.png",  new Rectangle(25, 0, 70, 80), 4, 2, BULL_MOVE),
    CATERPILLAR("sprites/caterpillar.png",  new Rectangle(18, 0, 74, 35), 4, 1, CATERPILLAR_MOVE),
    CATERPILLAR_PURPLE("sprites/caterpillar_purple.png",  new Rectangle(18, 0, 74, 35), 4, 1, CATERPILLAR_MOVE),
    CIRCULAR_SAW("sprites/enemy_circular_saw.png",  new Rectangle(12, 8, 80, 80), 10, 1, CIRCULAR_SAW_MOVE),
    METEOR("images/meteor.png",  new Rectangle(18, 8, 74, 74), 1, 1),
    METEOR_FIRE("images/meteor_fire.png",  new Rectangle(18, 8, 74, 74), 1, 1),
    FIRE_GROUND("sprites/fire_ground.png",  new Rectangle(18, 8, 74, 74), 7, 2, FIRE_GROUND_DO, FIRE_GROUND_DIE),

    // enemies decorations and death animations
    BULL_WARNING("sprites/bull_warning.png", null, 4, 2, BULL_WARN_ANIM),
    METEOR_EXPLOSION("sprites/meteor_explosion.png",  null, 6, 2, METEOR_EXPLODE),
    METEOR_FIRE_EXPLOSION("sprites/meteor_fire_explosion.png",  null, 6, 2, METEOR_FIRE_EXPLODE),
    FIRE_TRAIL("sprites/fire.png", null, 6, 1, FIRE_TRAIL_DO),

    PARTICLE_DUST_BROWN("sprites/particle_dust_brown.png", null, 8, 1, PARTICLE_DUST_FAINT),
    PARTICLE_SMOKE_WHITE_ROUND("sprites/particle_smoke_white_round_short2.png", null, 5, 3,
            PARTICLE_SMOKE_FAINT1, PARTICLE_SMOKE_FAINT2, PARTICLE_SMOKE_FAINT3),

    SMOKE_BROWN_LARGE("sprites/dead_anim_black.png", null, 7, 1, SMOKE_BROWN_LARGE_DIE),
    SMOKE_WHITE_LARGE("sprites/dead_anim.png", null, 7, 1, SMOKE_WHITE_LARGE_DIE),
    SMOKE_WHITE_SMALL("sprites/dead_anim_small.png", null, 7, 1, SMOKE_WHITE_SMALL_DIE),

    // items
    CORRIDA("images/corrida.png", new Rectangle(5, 0, 90, 95), 1, 1), // muleta
    MEDKIT("images/medkit.png", new Rectangle(10, 0, 80, 90), 1, 1),
    MEDKIT_SMALL("images/medkit_small.png", new Rectangle(5, 0, 90, 95), 1, 1),
    RED_CLOCK("images/item_red_clock.png", new Rectangle(5, 0, 90, 95), 1, 1),
    HOURGLASS_RED("images/hourglass_red.png", new Rectangle(5, 0, 90, 95), 1, 1),
    HOURGLASS_GREEN("images/hourglass_green.png", new Rectangle(5, 0, 90, 95), 1, 1),

    // items manipulation
    BAG("images/bag.png", null, 1, 1),
    BAG_SLOT("sprites/bag_slot.png", null, 2, 1, BAG_LOCK, BAG_UNLOCK),
    BALLOON_CRATE("sprites/balloon_crate.png", new Rectangle(20, 50, 60, 50), 6, 2,
            BALLON_MOVE, BALLON_EXPLODE),
    CRATE_EXPLOSION("sprites/crate_explosion.png", null, 6, 1, CRATE_EXPLODE)
    ;

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
