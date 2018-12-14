package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.PooledEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class StateComponent implements Component {

    public enum State {
        /** Can't collide neither be collide. (currently used after the corrida state) */
        UNTOUCHABLE,
        /** Recovery mode : temporary invincibility after a hit! */
        RECOVERY,
        /** Can collide but not be collide. */
        INVICIBLE,
        /** Stun by an enemy (like the bull), movement disable. */
        STUN,
        /** Low speed mode */
        LOW_SPEED(),
        /** High speed enemy mode */
        HIGH_SPEED_ENEMIES(),
        /** Low speed enemy mode */
        LOW_SPEED_ENEMIES();
    }

    /** Generate a StateComponent */
    public static StateComponent make(PooledEngine engine) {
        StateComponent component = engine.createComponent(StateComponent.class);
        component.permanentState = false;
        component.stateDuration.clear();
        return component;
    }

    public static boolean hasAtLeastOneState(StateComponent component, State... states) {
        return component != null && component.hasAtLeastOneState(states);
    }

    /** State - Remaining duration. */
    Map<State, Float> stateDuration = new HashMap<>();

    private boolean permanentState;

    /** Add a state with it's duration. Set -1 if all state of the component are permanent */
    public void addState(State state, float duration) {
        stateDuration.put(state, duration);
        if (duration == -1f) {
            permanentState = true;
        }
    }

    public boolean hasAtLeastOneState(State... states) {
        for (State state : states) {
            if (stateDuration.containsKey(state)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Subtract the deltaTime for each state duration. Return all ended states.
     *
     * @param deltaTime duration
     * @return a list of ended states
     */
    public List<State> checkStatesEnded(float deltaTime) {
        List<State> result = new ArrayList<>();
        if (permanentState) {
            return result;
        }
        for (Entry<State, Float> entry : stateDuration.entrySet()) {
            float newValue = entry.getValue() - deltaTime;
            if (newValue < 0) {
                result.add(entry.getKey());
            }
            else {
                entry.setValue(newValue);
            }
        }
        for (State state: result) {
            stateDuration.remove(state);
        }
        return result;
    }

    public Set<State> getActiveStates() {
        return stateDuration.keySet();
    }
}
