package com.survivalkid.game.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import android.content.Context;
import android.util.Log;

import com.survivalkid.game.core.Constants;
import com.survivalkid.game.core.Difficulty.DifficultyEnum;
import com.survivalkid.game.singleton.SharedVars;

/**
 * Store data when the application is shutdown
 * 
 * @author Thomas
 */
public final class DataSave implements Serializable {

	private static final long serialVersionUID = 7312795216510753348L;

	private static final String TAG = DataSave.class.getSimpleName();
	
	private static final String SAVE_FILE = "survival-game-save";

	// could make a second file in case of the first become corrupt
	//private static final String SAVE_FILE_BACKUP = "survival-game-save-backup";
	
	private static final int NB_HIGHSCORE = 10;
	
	/** count the number of highscore. In case of two identical highscore, the first remains the best */
	private long nbHighScore; 
	
	/** The highscore */
	private Map<DifficultyEnum,TreeSet<Score>> highScore;
	
	public DataSave() {
		highScore = new HashMap<DifficultyEnum,TreeSet<Score>>();
		nbHighScore = 0;
	}

	/**
	 * Reset the scores
	 */
	public void resetScores(Context context) {
		highScore.clear();
		nbHighScore = 0;
		saveData(context);
	}
	
	/**
	 * Add a new score. Return true if the score is a highscore, false otherwise
	 * 
	 * @param timePassed the score
	 * @return
	 */
	public boolean addScore(long timePassed) {
		DifficultyEnum difficulty = SharedVars.getSingleton().getDifficulty().getDifficulty();
		TreeSet<Score> scores = highScore.get(difficulty);
		if (scores == null) {
			scores = new TreeSet<Score>();
			highScore.put(difficulty, scores);
		}
		// if the number of highscore is not reached
		if (scores.size() < NB_HIGHSCORE) {
			
			scores.add(new Score(++nbHighScore, "Anonymous", timePassed));
			return true;
		}
		
		// else compare if the score is better than the lowest score
		Score first = scores.first();
		if (first.getTimeScore() >= timePassed) {
			// it's not a new highscore
			return false;
		}
		else {
			scores.remove(first);
			scores.add(new Score(++nbHighScore, "Anonymous", timePassed));
			return true;
		}
	}

	@Override
	public String toString() {
		return highScore.toString();
	}
	
	// getter et setter
	
	public TreeSet<Score> getHighScoreOneDiff(DifficultyEnum difficulty) {
		return highScore.get(difficulty);
	}
	
	public TreeSet<Score> getCurrentHighScore() {
		return getHighScoreOneDiff(SharedVars.getSingleton().getDifficulty().getDifficulty());
	}

	public void setHighScore(Map<DifficultyEnum, TreeSet<Score>> highScore) {
		this.highScore = highScore;
	}

	// file managing
	
	public static DataSave getSaveData(Context context) {
		FileInputStream fis = null;
		ObjectInputStream is = null;
		DataSave data = null;
		
		try {
			fis = context.openFileInput(SAVE_FILE);
			is = new ObjectInputStream(fis);
			
//			//exemple to keep compatibility
//			Object tmp = is.readObject();
//			if (tmp instanceof com.survivalkid.DataSave) {
//				data = new DataSave();
//				data.highScore = ((com.survivalkid.DataSave) tmp).getHighScore();
//				data.saveData(context);
//			}
//			else {
//				data = (DataSave) tmp;
//			}
			data = (DataSave) is.readObject();
			
			is.close();
		}
		catch(FileNotFoundException e) {
			if (Constants.DEBUG) {
				Log.d(TAG, "File not found");
			}
			data = new DataSave();
		} catch (StreamCorruptedException e) {
			Log.e(TAG, "Incorrect inputstream stream", e);
		} catch (IOException e) {
			Log.e(TAG, "Incorrect inputstream", e);
		} catch (ClassNotFoundException e) {
			Log.e(TAG, "Error in unserializing", e);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "Error in unserializing", e);
		}
		finally {
			if (fis != null) {
				closeInputStreamSecure(fis, "fis");
				closeInputStreamSecure(is, "is");
			}
			// ignore all problem in deserialization 
			// TODO : to delete when the format will be fixed to avoid overwrite the score if there is a minor problem
			if (data == null) {
				Log.e(TAG, "Old highscore will be erased because of problem of deserialization");
				data = new DataSave();
			}
			else {
				if (Constants.DEBUG) {
					Log.d(TAG, "Scores has been loaded. Nb score : " + data.highScore.size());
				}
			}
		}
		
		return data;
	}

	public void saveData(Context context) {
		FileOutputStream fos = null;
		ObjectOutputStream os = null;
		try {
			fos = context.openFileOutput(SAVE_FILE, Context.MODE_PRIVATE);
			os = new ObjectOutputStream(fos);
			os.writeObject(this);
			os.close();
		} catch (FileNotFoundException e) {
			if (Constants.DEBUG) {
				Log.d(TAG, "File not found");
			}
		} catch (IOException e) {
			if (Constants.DEBUG) {
				Log.d(TAG, "Failed to init save data");
			}
		}
		finally {
			if (fos != null) {
				closeOutputStreamSecure(fos, "fos");
				closeOutputStreamSecure(os, "os");
			}
		}
	}
	
	
	private static void closeOutputStreamSecure(OutputStream str, String toLog) {
		try {
			str.close();
		} catch (IOException e) {
			Log.w(TAG, "OutputStream cannot be closed : "+ toLog, e);
		}
	}
	
	private static void closeInputStreamSecure(InputStream str, String toLog) {
		try {
			str.close();
		} catch (IOException e) {
			Log.w(TAG, "InputStream cannot be closed : "+ toLog, e);
		}
	}
}
