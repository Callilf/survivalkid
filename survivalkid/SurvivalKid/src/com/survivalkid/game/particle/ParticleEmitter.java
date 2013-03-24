package com.survivalkid.game.particle;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import android.graphics.Canvas;

import com.survivalkid.game.core.enums.SpriteEnum;
import com.survivalkid.game.singleton.GameContext;

/**
 * The particles emitter.
 * 
 * When a particle emitter is created, it will contains an empty pool of
 * particles. Each time a particle is generated, the pool will be checks to see
 * if there is an unused particles and select it. If there is no particle and
 * the pool's size is < NUMBER_OF_PARTICLES_IN_POOL, a particle will be created,
 * added to the pool and generated. Therefore, the particles are re used all the
 * time to minimize the performance consumption.
 * 
 * @author Callil
 * 
 */
public class ParticleEmitter {

	private static final int NUMBER_OF_PARTICLES_IN_POOL = 100;

	/** The pool of particles. */
	private Set<Particle> pool;

	/** The frequency of generation. */
	private int generationFrequency;
	/** x position of the emitter. */
	private int x;
	/** y position of the emitter. */
	private int y;
	/** Whether the particles will fade out or not. */
	private boolean particleFadeOut;
	/** The time to live of the particle. */
	private int particleTimeOut;
	/** The sprite used by the particles (only one sprite so far). */
	private SpriteEnum particleSpriteEnum;
	/** The direction. */
	private int particleDirection;
	/** The particle alpha. */
	private int particleAlpha;
	
	/** Whether the emitter is generating or not. */
	private boolean generate;
	private boolean stopping;
	private boolean dead;
	
	/** Number of particle generated at each generation. */
	private int numberOfParticlePerGeneration = 1;
	
	private boolean deleteParticleWhenAnimFinished;
	
	private int particleSpeedXMin;
	private int particleSpeedXMax;
	private int particleSpeedYMin;
	private int particleSpeedYMax;

	private int speedChangeXMin;
	private int speedChangeXMax;
	private int speedChangeYMin;
	private int speedChangeYMax;

	Random rand;

	/** the time of the next generation. */
	private long nextGenerationTime;

	public ParticleEmitter(SpriteEnum _particleSpriteEnum, int _generationFrequency) {
		pool = new HashSet<Particle>();
		particleSpriteEnum = _particleSpriteEnum;
		generationFrequency = _generationFrequency;
		nextGenerationTime = GameContext.getSingleton().gameDuration + generationFrequency;
		generate = true;
		particleAlpha = 255;
	}

	public void setSpeedChange(int _speedChangeXMin, int _speedChangeXMax, int _speedChangeYMin, int _speedChangeYMax) {
		rand = new Random();
		speedChangeXMin = _speedChangeXMin;
		speedChangeXMax = _speedChangeXMax;
		speedChangeYMin = _speedChangeYMin;
		speedChangeYMax = _speedChangeYMax;
	}

	public void update(long gameTime) {
		if (gameTime > nextGenerationTime) {
			// Generate a particle
			if(generate) {
				for(int i=0 ; i<numberOfParticlePerGeneration ; i++) {
					generateAParticle();
				}
			}
			nextGenerationTime = gameTime + generationFrequency;
		}

		boolean particleActive = false;
		for (Particle particle : pool) {
			if (!particle.isVisible()) {
				continue;
			}
			particleActive = true;
			particle.update(gameTime);
		}
		
		if(stopping && !particleActive) {
			dead = true;
		}

	}

	public void draw(Canvas canvas) {
		for (Particle particle : pool) {
			if (particle.isVisible()) {
				particle.draw(canvas);
			}
		}
	}

	/**
	 * Generate a particle.
	 */
	private void generateAParticle() {
		Particle particleToGenerate = null;
		// First, check if we can reuse a particle from the pool
		for (Particle particle : pool) {
			if (!particle.isVisible()) {
				particleToGenerate = particle;
			}
		}

		// Generate random speed
		Random rand = new Random();
		int randomSpeedX = rand.nextInt(particleSpeedXMax - particleSpeedXMin + 1) + particleSpeedXMin;
		int randomSpeedY = particleSpeedYMin;
		if(particleSpeedYMax - particleSpeedYMin + 1 != 0) {
			randomSpeedY = rand.nextInt(particleSpeedYMax - particleSpeedYMin + 1) + particleSpeedYMin;
		}

		if (particleToGenerate == null) {
			// no particle available in the pool
			if (pool.size() >= NUMBER_OF_PARTICLES_IN_POOL) {
				// The pool is full, can't generate any new particle.
				return;
			} else {
				particleToGenerate = new Particle(particleSpriteEnum, particleTimeOut, randomSpeedX, randomSpeedY);
			}
		} else {
			particleToGenerate.setTimeOut(particleTimeOut);
		}

		particleToGenerate.setVisible(true);
		particleToGenerate.setX(x - particleSpriteEnum.getWidth()/2);
		particleToGenerate.setY(y - particleSpriteEnum.getHeight()/2);
		particleToGenerate.setFadeOut(particleFadeOut);
		particleToGenerate.setSpeedX(randomSpeedX);
		particleToGenerate.setSpeedY(randomSpeedY);
		particleToGenerate.setSpeedXChangePeriod(rand.nextInt(speedChangeXMax - speedChangeXMin + 1) + speedChangeXMin);
		particleToGenerate.setSpeedYChangePeriod(rand.nextInt(speedChangeYMax - speedChangeYMin + 1) + speedChangeYMin);
		particleToGenerate.setDeleteParticleWhenAnimFinished(deleteParticleWhenAnimFinished);
		particleToGenerate.setDirection(particleDirection);
		particleToGenerate.setAlpha(particleAlpha);
		
		int randomAnimIndex = rand.nextInt(particleSpriteEnum.getAnimations().keySet().size());
		String randomAnim = null;
		int i = 0;
		for(String str : particleSpriteEnum.getAnimations().keySet()) {
			if(i == randomAnimIndex) {
				randomAnim = str;
				break;
			}
			i += 1;
		}
		particleToGenerate.getSprite().play(randomAnim, false, true, true);

		pool.add(particleToGenerate);
	}

	// -------------------------------------GETTERS AND SETTERS
	/**
	 * @return the particleSprite
	 */
	public SpriteEnum getParticleSprite() {
		return particleSpriteEnum;
	}

	/**
	 * @param particleSprite
	 *            the particleSprite to set
	 */
	public void setParticleSprite(SpriteEnum particleSprite) {
		this.particleSpriteEnum = particleSprite;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the particleFadeOut
	 */
	public boolean isParticleFadeOut() {
		return particleFadeOut;
	}

	/**
	 * @param particleFadeOut
	 *            the particleFadeOut to set
	 */
	public void setParticleFadeOut(boolean particleFadeOut) {
		this.particleFadeOut = particleFadeOut;
	}

	/**
	 * @return the particleTimeOut
	 */
	public int getParticleTimeOut() {
		return particleTimeOut;
	}

	/**
	 * @param particleTimeOut
	 *            the particleTimeOut to set
	 */
	public void setParticleTimeOut(int particleTimeOut) {
		this.particleTimeOut = particleTimeOut;
	}

	/**
	 * @return the particleSpeedXMin
	 */
	public int getParticleSpeedXMin() {
		return particleSpeedXMin;
	}

	/**
	 * @param particleSpeedXMin
	 *            the particleSpeedXMin to set
	 */
	public void setParticleSpeedXMin(int particleSpeedXMin) {
		this.particleSpeedXMin = particleSpeedXMin;
	}

	/**
	 * @return the particleSpeedXMax
	 */
	public int getParticleSpeedXMax() {
		return particleSpeedXMax;
	}

	/**
	 * @param particleSpeedXMax
	 *            the particleSpeedXMax to set
	 */
	public void setParticleSpeedXMax(int particleSpeedXMax) {
		this.particleSpeedXMax = particleSpeedXMax;
	}

	/**
	 * @return the particleSpeedYMin
	 */
	public int getParticleSpeedYMin() {
		return particleSpeedYMin;
	}

	/**
	 * @param particleSpeedYMin
	 *            the particleSpeedYMin to set
	 */
	public void setParticleSpeedYMin(int particleSpeedYMin) {
		this.particleSpeedYMin = particleSpeedYMin;
	}

	/**
	 * @return the particleSpeedYMax
	 */
	public int getParticleSpeedYMax() {
		return particleSpeedYMax;
	}

	/**
	 * @param particleSpeedYMax
	 *            the particleSpeedYMax to set
	 */
	public void setParticleSpeedYMax(int particleSpeedYMax) {
		this.particleSpeedYMax = particleSpeedYMax;
	}

	/**
	 * @return the generate
	 */
	public boolean isGenerate() {
		return generate;
	}

	/**
	 * @param generate the generate to set
	 */
	public void setGenerate(boolean generate) {
		this.generate = generate;
	}

	/**
	 * @return the dead
	 */
	public boolean isDead() {
		return dead;
	}

	/**
	 * @param dead the dead to set
	 */
	public void setDead(boolean dead) {
		this.dead = dead;
	}

	/**
	 * @return the stopping
	 */
	public boolean isStopping() {
		return stopping;
	}

	/**
	 * @param stopping the stopping to set
	 */
	public void setStopping(boolean stopping) {
		this.stopping = stopping;
		if(stopping){
			generate = false;
		}
	}

	/**
	 * @return the numberOfParticlePerGeneration
	 */
	public int getNumberOfParticlePerGeneration() {
		return numberOfParticlePerGeneration;
	}

	/**
	 * @param numberOfParticlePerGeneration the numberOfParticlePerGeneration to set
	 */
	public void setNumberOfParticlePerGeneration(int numberOfParticlePerGeneration) {
		this.numberOfParticlePerGeneration = numberOfParticlePerGeneration;
	}

	/**
	 * @return the deleteParticleWhenAnimFinished
	 */
	public boolean isDeleteParticleWhenAnimFinished() {
		return deleteParticleWhenAnimFinished;
	}

	/**
	 * @param deleteParticleWhenAnimFinished the deleteParticleWhenAnimFinished to set
	 */
	public void setDeleteParticleWhenAnimFinished(boolean deleteParticleWhenAnimFinished) {
		this.deleteParticleWhenAnimFinished = deleteParticleWhenAnimFinished;
	}

	/**
	 * @return the particleDirection
	 */
	public int getParticleDirection() {
		return particleDirection;
	}

	/**
	 * @param particleDirection the particleDirection to set
	 */
	public void setParticleDirection(int particleDirection) {
		this.particleDirection = particleDirection;
	}

	/**
	 * @return the particleAlpha
	 */
	public int getParticleAlpha() {
		return particleAlpha;
	}

	/**
	 * @param particleAlpha the particleAlpha to set
	 */
	public void setParticleAlpha(int particleAlpha) {
		this.particleAlpha = particleAlpha;
	}

	
}
