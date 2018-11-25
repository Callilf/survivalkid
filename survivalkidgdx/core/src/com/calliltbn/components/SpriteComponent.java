package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.calliltbn.spritesdef.SpriteAnimationEnum;
import com.calliltbn.spritesdef.TextureEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * For Sprite and Sprite Animated component (perhaps I should have split them)
 *
 * @author callil, tbn
 */
public class SpriteComponent implements Component, Poolable {
    public boolean hide;
    private Sprite sprite;
    private SpriteAnimationEnum currentAnimation;
    private boolean loop;

    private boolean flip;

    private Vector2 position;

    private Map<SpriteAnimationEnum, Animation<Sprite>> allAnimations;
    private float stateTime = 0f;

    private int zindex = 0;

    @Override
    public void reset() {
        hide = false;
        currentAnimation = null;
        stateTime = 0f;
    }

    public void initTexture(TextureEnum textureEnum) {
        Map<SpriteAnimationEnum, TextureRegion[]> animations = textureEnum.getTextureAnimation();

        sprite = new Sprite(textureEnum.getTextureDefault());
        if (animations != null) {
            allAnimations = new HashMap<>();
            for (Map.Entry<SpriteAnimationEnum, TextureRegion[]> entryAnim :
                    textureEnum.getTextureAnimation().entrySet()) {
                Animation<Sprite> animation = new Animation<>(entryAnim.getKey().getFps(),
                        textureRegionToSprite(entryAnim.getValue()));
                allAnimations.put(entryAnim.getKey(), animation);
            }
        }
    }

    private Sprite[] textureRegionToSprite(TextureRegion[] regions) {
        Sprite[] sprites = new Sprite[regions.length];
        for (int i = 0; i < regions.length ; i++) {
            sprites[i] = new Sprite(regions[i]);
        }
        return sprites;
    }

    public void setCurrentAnimation(SpriteAnimationEnum animation, boolean loop) {
        this.currentAnimation = animation;
        this.loop = loop;
    }

    /**
     * Return true if the point is inside the sprite.
     * @param point the point to test
     * @return true if the point is inside the sprite
     */
    public boolean containsPoint(Vector2 point) {
        return sprite != null && sprite.getBoundingRectangle().contains(point);
    }

    /**
     * Return true if the point at (x,y) is inside the sprite.
     * @param x the abscissa
     * @param y the ordinate
     * @return true if the point is inside the sprite
     */
    public boolean containsPoint(float x, float y) {
        return sprite != null && sprite.getBoundingRectangle().contains(x, y);
    }

    public Sprite getSprite(float deltaTime) {
        Sprite resultSprite = null;
        if (currentAnimation == null) {
            resultSprite = sprite;
        }
        else {
            Animation<Sprite> anim = allAnimations.get(currentAnimation);
            stateTime += deltaTime; // Accumulate elapsed animation time
            resultSprite = anim.getKeyFrame(stateTime, loop);
        }
        resultSprite.setPosition(position.x, position.y);
        resultSprite.setFlip(flip, false);
        return resultSprite;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public boolean isFlip() {
        return flip;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public int getZindex() {
        return zindex;
    }

    public void setZindex(int zindex) {
        this.zindex = zindex;
    }
}
