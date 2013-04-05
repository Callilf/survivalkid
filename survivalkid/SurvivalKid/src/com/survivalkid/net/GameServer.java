package com.survivalkid.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.survivalkid.GameActivity;

import android.util.Log;

public class GameServer extends Thread {
	
	private static final int SERVER_PORT = 19009;
	private static final String TAG = GameServer.class.getSimpleName();
	/**
	 * The socket that listens for requests.
	 */
	private ServerSocket serverSocket;
	private boolean requestedToStop = false;
	private boolean isStopped = false;
	
	/**
	 * The Game. 
	 */
	private GameActivity gameActivity;
	
	
	public GameServer(GameActivity gameActivity) {
		this.setGameActivity(gameActivity);
	}


	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
			Log.d(TAG, "Server started!");
			//listen
			while(!requestedToStop){
				
				Socket client = serverSocket.accept();
				Log.d(TAG, "Server received a request!");
				GameServerThread thread = new GameServerThread(client, this);
				thread.start();
			}
			
			isStopped  = true;
			Log.d(TAG, "Server stopped!");
		} catch (IOException e) {
			e.printStackTrace();
			handleError(e, "error on server socket!");
		}
	}


	public void handleError(Exception e, String msg) {
		Log.e(TAG, msg, e);
	}


	public void requestStop() {
		requestedToStop = true;
		while(!isStopped);
	}


	public GameActivity getGameActivity() {
		return gameActivity;
	}


	public void setGameActivity(GameActivity gameActivity) {
		this.gameActivity = gameActivity;
	}

}
