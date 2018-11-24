package com.calliltbn.util;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.calliltbn.components.HealthComponent;
import com.calliltbn.components.SpriteComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * The util class containing all ComponentMappers.
 * @author Callil, tbn
 *
 */
public final class Mappers {

	private static final Map<Class<? extends Component>,ComponentMapper<? extends Component>> allMappers = new HashMap<>();

	public final static ComponentMapper<SpriteComponent> spriteComponent = ComponentMapper.getFor(SpriteComponent.class);
	public final static ComponentMapper<HealthComponent> playerComponent = ComponentMapper.getFor(HealthComponent.class);

	public static <T extends Component> T  getComponent(Class<T> clazz, Entity entity) {
		ComponentMapper<T> mapper = (ComponentMapper<T>) allMappers.get(clazz);
		if (mapper == null) {
			mapper = ComponentMapper.getFor(clazz);
			allMappers.put(clazz, mapper);
		}
		return mapper.get(entity);
	}

	/** No constructor. */
	private Mappers() {}
	
}
