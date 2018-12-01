package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class CollideComponent implements Component {

    /** Generate a CollideComponent */
    public static CollideComponent make(PooledEngine engine, int damage, float recoveryTime,
                                        SpriteComponent spriteComponent) {
        CollideComponent component = engine.createComponent(CollideComponent.class);
        component.damage = damage;
        component.recoveryTime = recoveryTime;
        component.spriteComponent = spriteComponent;
        component.spritePosition = spriteComponent.getPosition();
        component.offsets = spriteComponent.getTexture().getOffsets();
        if (component.offsets == null) {
            // allow to have a full sprite hitbox
            component.offsets = new Rectangle(0, 0,
                    spriteComponent.getSprite().getWidth(), spriteComponent.getSprite().getHeight());
        }
        component.hitbox = new RectangleMapObject(0, 0, component.offsets.width, component.offsets.height);
        component.updateHitbox();
        return component;
    }

    /** Sub rectangle hitbox of the sprite in the position 0,0. Used to recalcul hitbox */
    private Rectangle offsets;

    /* Hitbox of the sprite */
    private RectangleMapObject hitbox;
    private Vector2 spritePosition;
    private SpriteComponent spriteComponent;

    private int damage;

    private float recoveryTime;

    /**
     * Test if the component collide with another component
     *
     * @param component the second component to test
     * @return true if the two components overlaps
     */
    public boolean collideWith(CollideComponent component) {
        return hitbox.getRectangle().overlaps(component.getHitbox().getRectangle());
    }

    /**
     * Update the position of the hitbox (to used when the sprite move)
     */
    public void updateHitbox() {
        if (spriteComponent.isFlip()) {
            hitbox.getRectangle().setPosition(
                    spritePosition.x + spriteComponent.getSprite().getWidth() - offsets.x - offsets.width,
                    spritePosition.y + offsets.y);
        } else {
            hitbox.getRectangle().setPosition(spritePosition.x + offsets.x, spritePosition.y + offsets.y);
        }
    }

    public RectangleMapObject getHitbox() {
        return hitbox;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getRecoveryTime() {
        return recoveryTime;
    }

    public void setRecoveryTime(float recoveryTime) {
        this.recoveryTime = recoveryTime;
    }
}
