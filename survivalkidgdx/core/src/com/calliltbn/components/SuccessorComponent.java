package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;
import com.calliltbn.desc.TypeEntity;

import java.util.Arrays;
import java.util.List;

public class SuccessorComponent implements Component {

    private List<TypeEntity> successors;

    private boolean activated;

    /** Generate a SuccessorComponent */
    public static SuccessorComponent make(PooledEngine engine, TypeEntity... typeEntities) {
        SuccessorComponent component = engine.createComponent(SuccessorComponent.class);
        component.successors = Arrays.asList(typeEntities);
        component.activated = false;
        return component;
    }

    public List<TypeEntity> getSuccessors() {
        return successors;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
