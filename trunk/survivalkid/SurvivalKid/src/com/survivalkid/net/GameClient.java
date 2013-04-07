package com.survivalkid.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.survivalkid.GameActivity;
import com.survivalkid.game.core.Constants;

public class GameClient {

	private static final String TAG = GameClient.class.getSimpleName();

	GameActivity game;

	private Socket connection;

	private String serverIP;

	private PrintWriter out;

	private BufferedReader in;

	public GameClient(GameActivity game) {
		this.game = game;
	}

	public void close() throws IOException {
		connection.close();
		out.close();
		in.close();
	}

	public boolean connectToServer(String ipAddress) throws JSONException {
		// Connect to the server
		try {
			connection = new Socket(ipAddress, GameServer.SERVER_PORT);
			out = new PrintWriter(connection.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			// if it is ok store ip
			serverIP = ipAddress;
			
			// Send connect command
			return sendConnect();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Log.e(TAG, "host not found :" + serverIP, e);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;

	}
	
	public void disconnect() throws IOException, JSONException{
		sendDisconnect();
		close();
	}

	/**
	 * Send CONNECT command to the server.
	 * @throws JSONException
	 * @throws IOException 
	 */
	private boolean sendConnect() throws JSONException, IOException {
		JSONObject command = new JSONObject();
		command.accumulate("command", "CONNECT");
		
		JSONObject res = send(command);
		
		return res.getBoolean("connected");
		
	}
	
	/**
	 * Send CONNECT command to the server.
	 * @throws JSONException
	 * @throws IOException 
	 */
	private boolean sendDisconnect() throws JSONException, IOException {
		JSONObject command = new JSONObject();
		command.accumulate("command", "DISCONNECT");
		
		JSONObject res = send(command);
		
		return res.getBoolean("connected");
		
	}

	/**
	 * Sends JSON, and then returns the response from the server.
	 * @param command
	 * @return
	 * @throws IOException
	 * @throws JSONException 
	 */
	public JSONObject send(JSONObject command) throws IOException, JSONException {
		if (Constants.DEBUG) {
			Log.d(TAG,"Sending : "+command.toString());
		}
		out.append(command.toString());
		
		// read response
		StringBuffer res = new StringBuffer();
		String nextLine = "";
		while((nextLine = in.readLine()) != null){
			res.append(nextLine);
		}
		if (Constants.DEBUG) {
			Log.d(TAG,"Received : "+res);
		}
		return new JSONObject(res.toString());
	}
	
	public JSONObject sendUpdate(ArrayList<String> inputs) throws JSONException, IOException{
		JSONObject command = new JSONObject();
		command.accumulate("command", "UPDATE");
		command.accumulate("inputs", new JSONArray(inputs));
		
		return send(command);
	}

}
