package com.survivalkid.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.survivalkid.GameActivity;

import android.util.Log;

public class GameServer extends Thread {

	public static final int SERVER_PORT = 19009;
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

	/**
	 * True if a client is currently connected.
	 */
	private boolean hasClient = false;

	private boolean hasClientTimedOut = false;

	/**
	 * @return the hasClientTimedOut
	 */
	public boolean isHasClientTimedOut() {
		return hasClientTimedOut;
	}

	/**
	 * @param hasClientTimedOut
	 *            the hasClientTimedOut to set
	 */
	public void setHasClientTimedOut(boolean hasClientTimedOut) {
		this.hasClientTimedOut = hasClientTimedOut;
	}

	/**
	 * @return the hasClient
	 */
	public boolean isHasClient() {
		return hasClient;
	}

	/**
	 * @param hasClient
	 *            the hasClient to set
	 */
	public void setHasClient(boolean hasClient) {
		this.hasClient = hasClient;
	}

	public GameServer(GameActivity gameActivity) {
		this.setGameActivity(gameActivity);
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
			Log.d(TAG, "Server started!");
			// listen
			while (!requestedToStop) {

				Socket client = serverSocket.accept();
				Log.d(TAG, "Server received a request!");
				GameServerThread thread = new GameServerThread(client, this);
				thread.start();
			}

			isStopped = true;
			Log.d(TAG, "Server stopped!");
		} catch (IOException e) {
			e.printStackTrace();
			handleError(e, "error on server socket!");
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void handleError(Exception e, String msg) {
		Log.e(TAG, msg, e);
	}

	public void requestStop() {
		requestedToStop = true;
		while (!isStopped)
			;
	}

	public GameActivity getGameActivity() {
		return gameActivity;
	}

	public void setGameActivity(GameActivity gameActivity) {
		this.gameActivity = gameActivity;
	}

	public void onClientDisconnected() {
		// TODO Auto-generated method stub

	}

	public void onClientConnected() {
		// TODO Auto-generated method stub

	}

}
