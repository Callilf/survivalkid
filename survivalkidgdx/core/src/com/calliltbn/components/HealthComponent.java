package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Pool;

/**
 * Partially copied from TJ
 *
 * @author callil, tbn
 */
public class HealthComponent implements Component, Pool.Poolable {

    /** Generate a HealthComponent */
    public static HealthComponent make(PooledEngine engine, int maxHp) {
        HealthComponent component = engine.createComponent(HealthComponent.class);
        component.maxHp = maxHp;
        component.hp = maxHp;
        return component;
    }

    /** The max number of h. */
    private int maxHp;

    /** The current number of hp. */
    private int hp;

    /**
     * Whether this entity is dead.
     * @return true if the entity is dead.
     */
    public boolean isDead() {
        return hp <= 0;
    }


    @Override
    public void reset() {
    }

    // Getters and Setters

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public boolean hit(int damage) {
        hp -= damage;
        return isDead();
    }


}
