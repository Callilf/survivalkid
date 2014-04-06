package com.survivalkid.game.core;

import com.survivalkid.BuildConfig;

public final class Constants {

	private Constants() {
	}

	// if the debug is Enable
	public static final boolean DEBUG = BuildConfig.DEBUG;
	
	/**
	 * Constants relatives to the characters.
	 * 
	 * @author Callil
	 * 
	 */
	public class PersonageConstants {
		public static final int PERSO_YUGO = 0;
		public static final int PERSO_YUNA = 1;

		public static final String ANIM_STAND = "stand";
		public static final String ANIM_RUN = "run";
		public static final String ANIM_JUMPUP = "jumpup";
		public static final String ANIM_JUMPDOWN = "jumpdown";
		public static final String ANIM_KNOCK_BACK = "knockback";
		public static final int PERSO_SEPTH = 2;
	}

	/**
	 * Constants for sprite directions.
	 * 
	 * @author Callil
	 */
	public class DirectionConstants {
		public static final int LEFT = 0;
		public static final int RIGHT = 1;
	}
	
	
	/**
	 * Constants for collisions.
	 * 
	 * @author Callil
	 */
	public class CollisionConstants {
		public static final int MAX_FRAMES_OF_COLLISION = 2;
	}
	
	public class PreferencesConstants {
		public static final String DISPLAY_HITBOX = "displayHitbox";
		public static final String ANTIALIAS_TEXT = "antialiasText";
		public static final String RESCALING_ENABLED = "scalingEnabled";
		public static final String SOUND_ENABLED = "soundEnabled";
		public static final String PARTICLE_ENABLED = "particleEnabled";
		public static final String SLIDE_MOVE_ENABLED = "slideEnabled";
		public static final String KEY_POSITION_CENTER = "survival-kid-buttonPosition";
		public static final String KEY_POSITION_STRETCH = "survival-kid-buttonPosition-stretch";
		public static final String DIFFICULTY = "survival-kid-difficulty";
	}
}