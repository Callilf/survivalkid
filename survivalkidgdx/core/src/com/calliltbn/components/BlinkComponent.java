package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;

public class BlinkComponent implements Component {

    /** Generate a BlinkComponent */
    public static BlinkComponent make(PooledEngine engine, float durationVisible, float durationInvisible) {
        BlinkComponent component = engine.createComponent(BlinkComponent.class);
        component.durationVisible = durationVisible;
        component.durationInvisible = durationInvisible;
        component.currentBlinkDuration = 0;
        component.visible = true;
        return component;
    }

    // Manage blink
    private float durationVisible;
    private float durationInvisible;
    private float currentBlinkDuration;

    public boolean visible;

    /**
     * Test if the sprite should be visible
     *
     * @param deltaTime time between last update
     * @return true if the sprite is visible, false otherwise
     */
    public boolean isVisible(float deltaTime) {
        currentBlinkDuration += deltaTime;
        if (visible) {
            if (currentBlinkDuration > durationVisible) {
                currentBlinkDuration =  0;
                visible = false;
            }
        }
        else {
            if (currentBlinkDuration > durationInvisible) {
                currentBlinkDuration =  0;
                visible = true;
            }
        }
        return visible;
    }

    public void updateBlinkPeriod(float blinkDurationVisible, float blinkDurationInvisible) {
        this.durationVisible = blinkDurationVisible;
        this.durationInvisible = blinkDurationInvisible;
    }
}
