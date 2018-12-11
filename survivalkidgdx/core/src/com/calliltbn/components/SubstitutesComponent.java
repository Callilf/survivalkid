package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.calliltbn.desc.TypeEntity;

import java.util.Arrays;
import java.util.List;

/**
 * Stock futures components that should be added to the entity.
 * {@link com.calliltbn.systems.SubstituteSystem} manage the change of state depending on the TypeEntity which has this component.
 *
 * @author tbn
 */
public class SubstitutesComponent implements Component {

    private List<Component> substitutes;

    private TypeEntity typeEntity;

    private int rotation;
    private int nbRotation;

    private float duration;

    /** Generate a ReplaceComponent */
    public static SubstitutesComponent make(PooledEngine engine, int nbRotation, TypeEntity typeEntity, Component... components) {
        SubstitutesComponent component = engine.createComponent(SubstitutesComponent.class);
        component.typeEntity =  typeEntity;
        component.substitutes = Arrays.asList(components);
        component.nbRotation = nbRotation;
        component.rotation = 0;
        component.duration = 0;
        return component;
    }

    public List<Component> getSubstitutes() {
        return substitutes;
    }

    public TypeEntity getTypeEntity() {
        return typeEntity;
    }

    /***
     * @return true if all rotation has been done
     */
    public boolean incRotation() {
        return ++rotation == nbRotation;
    }

    public float getDuration(float deltaTime) {
        duration += deltaTime;
        return duration;
    }

    public int getRotation() {
        return rotation;
    }


}
