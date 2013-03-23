package com.survivalkid.game.manager;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

import com.survivalkid.game.particle.ParticleEmitter;

public class ParticleManager {
	private static final String TAG = ParticleManager.class.getSimpleName();

	private List<ParticleEmitter> emitterList;
	private List<ParticleEmitter> deadEmitters;
	private List<ParticleEmitter> emittersToAdd;

	public ParticleManager() {
		emitterList = new ArrayList<ParticleEmitter>();
		deadEmitters = new ArrayList<ParticleEmitter>();
		emittersToAdd = new ArrayList<ParticleEmitter>();
	}

	public void create() {
		emitterList = new ArrayList<ParticleEmitter>();
	}

	public void update(long gameDuration) {
		updateEmitters(gameDuration);
		
		// remove the dead enemies from the list so that they are removed from the game
		if (!deadEmitters.isEmpty()) {
			for (ParticleEmitter emitter : deadEmitters) {
				emitterList.remove(emitter);
			}
			deadEmitters.clear();
		}
		
		// add the enemies create during the update to the game
		if (!emittersToAdd.isEmpty()) {
			for (ParticleEmitter emitter : emittersToAdd) {
				addEmitter(emitter);
			}
			emittersToAdd.clear();
		}
	}
	
	/**
	 * do one update of all enemy
	 * 
	 * @param gameDuration the game duration
	 */
	private void updateEmitters(long gameDuration) {
		for (ParticleEmitter emitter : emitterList) {
			if (emitter.isDead()) {
				deadEmitters.add(emitter);
			} else {
				emitter.update(gameDuration);
			}
		}
	}

	public void draw(Canvas canvas) {
		for (ParticleEmitter emitter : emitterList) {
			emitter.draw(canvas);
		}
	}
	
	public void addEmitter(ParticleEmitter ee) {
		emitterList.add(ee);
	}

}
