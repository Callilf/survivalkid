package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;

public class CollideComponent implements Component {

    /** Generate a CollideComponent */
    public static CollideComponent make(PooledEngine engine, int damage, float recoveryTime) {
        CollideComponent component = engine.createComponent(CollideComponent.class);
        component.damage = damage;
        component.recoveryTime = recoveryTime;
        return component;
    }

    private int damage;

    private float recoveryTime;

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
