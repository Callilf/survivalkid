package com.survivalkid.net;

import org.json.JSONException;

import com.survivalkid.GameActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * This class holds all the methods useful for receiving, sending messages, and
 * establishing connections.
 * 
 * @author baaleze
 * 
 */
public class NetworkManagerService extends Service {

	private static final String TAG = NetworkManagerService.class.getSimpleName();

	/**
	 * Holds the server which is connected. <code>null</code> if not connected
	 * to any server.
	 */
	private String serverAddress;

	/**
	 * The local server if the app hosts a game.
	 */
	private GameServer localServer;

	/**
	 * The local client if the app connects to a server to play.
	 */
	private GameClient localClient;

	/**
	 * Link to the game itself.
	 */
	private GameActivity gameActivity;

	/**
	 * Creates the server. Erase the old server if there was one.
	 */
	public void launchServer() {
		localServer.requestStop();
		localServer = new GameServer(gameActivity);
		localServer.start();
	}

	public void connectToServer(String ipAddress) {
		try {
			boolean res = localClient.connectToServer(ipAddress);
			if(!res){
				
			}else{
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e(TAG, "Error while connection", e);
		}
	}

	/** BINDING METHODS **/

	NetworkManagerBinder binder = new NetworkManagerBinder();

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	private class NetworkManagerBinder extends Binder {

		NetworkManagerService getService() {
			return NetworkManagerService.this;
		}

	}

	/**
	 * Use this after binding to the service so it can call back to you.
	 * 
	 * @param activity
	 */
	public void setGameActivity(GameActivity activity) {
		this.gameActivity = activity;

		// create gameClient and gameServer objects
		localClient = new GameClient(gameActivity);
		localServer = new GameServer(gameActivity);
	}

}
