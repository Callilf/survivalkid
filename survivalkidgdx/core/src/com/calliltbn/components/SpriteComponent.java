package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.calliltbn.spritesdef.SpriteAnimationEnum;
import com.calliltbn.spritesdef.TextureEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * For Sprite and Sprite Animated component (perhaps I should have split them).
 *
 * @author callil, tbn
 */
public class SpriteComponent implements Component {

    /** Generate a SpriteComponent */
    public static SpriteComponent make(PooledEngine engine, TextureEnum texture, Vector2 position,
        SpriteAnimationEnum currentAnimation, int zindex) {
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.texture = texture;
        component.position = position;
        component.currentAnimation = currentAnimation;
        component.loop = true;
        component.dieWhenAnimFinished = false;
        component.zindex = zindex;
        component.flip = false;
        component.flipShift = 0f;
        component.stateTime = 0f;
        component.initTexture(texture);
        return component;
    }

    private TextureEnum texture;
    private Sprite sprite;
    private SpriteAnimationEnum currentAnimation;
    private boolean loop;

    private boolean dieWhenAnimFinished;

    private boolean flip;
    private float flipShift;

    private Vector2 position;

    private Map<SpriteAnimationEnum, Animation<Sprite>> allAnimations;
    private float stateTime = 0f;

    private int zindex = 0;

    public void initTexture(TextureEnum textureEnum) {
        Map<SpriteAnimationEnum, TextureRegion[]> animations = textureEnum.getTextureAnimation();

        sprite = new Sprite(textureEnum.getTextureDefault());
        if (animations != null) {
            allAnimations = new HashMap<>();
            for (Map.Entry<SpriteAnimationEnum, TextureRegion[]> entryAnim :
                    textureEnum.getTextureAnimation().entrySet()) {
                Animation<Sprite> animation = new Animation<>(entryAnim.getKey().getFrequency(),
                        textureRegionToSprite(entryAnim.getValue()));
                allAnimations.put(entryAnim.getKey(), animation);
            }
        }

        if (textureEnum.getOffsets() != null) {
            Rectangle offsets = textureEnum.getOffsets();
            flipShift = sprite.getWidth() - offsets.width - 2 * offsets.x;
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
        if (this.currentAnimation != animation) {
            // change animation, reset state time
            stateTime = 0f;
        }
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

    public void increaseAnimTime(float deltaTime) {
        if (currentAnimation != null) {
            stateTime += deltaTime;
        }
    }

    public Sprite getSprite() {
        Sprite resultSprite = null;
        if (currentAnimation == null) {
            resultSprite = sprite;
        }
        else {
            Animation<Sprite> anim = allAnimations.get(currentAnimation);
            resultSprite = anim.getKeyFrame(stateTime, loop);
        }
        resultSprite.setFlip(flip, false);
        resultSprite.setPosition(position.x, position.y);
        return resultSprite;
    }

    public void setDieOnFinished() {
        dieWhenAnimFinished = true;
        loop = false;
    }

    public boolean isFinished() {
        return dieWhenAnimFinished &&
                allAnimations.get(currentAnimation).isAnimationFinished(stateTime);
    }

    public TextureEnum getTexture() {
        return texture;
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
        if (this.flip != flip) {
            this.flip = flip;
            // move the sprite center to the hitbox
            position.x += flip? -flipShift : flipShift;
        }
    }

    public int getZindex() {
        return zindex;
    }

    public void setZindex(int zindex) {
        this.zindex = zindex;
    }

}
