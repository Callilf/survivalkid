package com.survivalkid;

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
import java.util.TreeSet;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class DataSave extends Activity implements Serializable {

	private static final long serialVersionUID = 7312795216510753348L;

	private static final String TAG = DataSave.class.getSimpleName();
	
	private static final String SAVE_FILE = "survival-game-save";
	// could make a second file in case of the first become corrupt
	//private static final String SAVE_FILE_BACKUP = "survival-game-save-backup";

	TreeSet<Long> highScore;
	
	private static final int NB_HIGHSCORE = 10;
	
	public DataSave() {
		highScore = new TreeSet<Long>();
	}
	
	/**
	 * Add a new score. Return true if the score is a highscore, false otherwise
	 * 
	 * @param timePassed the score
	 * @return
	 */
	public boolean addScore(long timePassed) {
		
		// if the number of highscore is not reached
		if (highScore.size() < NB_HIGHSCORE) {
			highScore.add(timePassed);
			return true;
		}
		
		// else compare if the score is better than the lowest score
		Long low = highScore.first();
		if (low >= timePassed) {
			return false;
		}
		else {
			highScore.remove(low);
			highScore.add(timePassed);
			return true;
		}
	}

	@Override
	public String toString() {
		return highScore.toString();
	}
	
	// getter et setter
	
	public TreeSet<Long> getHighScore() {
		return highScore;
	}

	public void setHighScore(TreeSet<Long> highScore) {
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
			data = (DataSave) is.readObject();
			is.close();
		}
		catch(FileNotFoundException e) {
			Log.d(TAG, "File not found");
			return data = new DataSave();
		} catch (StreamCorruptedException e) {
			Log.w(TAG, "Incorrect inputstream stream", e);
		} catch (IOException e) {
			Log.w(TAG, "Incorrect inputstream", e);
		} catch (ClassNotFoundException e) {
			Log.w(TAG, "Error in unserializing", e);
		}
		finally {
			if (fis != null) {
				closeInputStreamSecure(fis, "fis");
				closeInputStreamSecure(is, "is");
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
			Log.d(TAG, "File not found");
		} catch (IOException e) {
			Log.d(TAG, "Failed to init save data");
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
			Log.w(TAG, "OutputStream cannot be closed : "+ toLog);
		}
	}
	
	private static void closeInputStreamSecure(InputStream str, String toLog) {
		try {
			str.close();
		} catch (IOException e) {
			Log.w(TAG, "InputStream cannot be closed : "+ toLog);
		}
	}
}
