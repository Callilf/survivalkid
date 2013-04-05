package com.survivalkid.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.survivalkid.game.GameManager;
import com.survivalkid.game.entity.enemy.EnemyEntity;
import com.survivalkid.game.entity.item.ItemEntity;
import com.survivalkid.game.entity.personage.Personage;

import android.util.Log;

public class GameServerThread extends Thread {

	private static final String TAG = GameServerThread.class.getSimpleName();

	private PrintWriter out;
	private BufferedReader in;
	private GameServer gameServer;
	private Socket socket;

	public GameServerThread(Socket socket, GameServer gameServer)
			throws IOException {

		this.gameServer = gameServer;
		this.socket = socket;
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	}

	@Override
	public void run() {
		// read input
		StringBuffer input = new StringBuffer();
		String nextLine;
		try {
			while ((nextLine = in.readLine()) != null) {
				input.append(nextLine);
			}
			Log.d(TAG, "Received : " + input.toString());
			String response = processMessage(input.toString());
			// send back response
			out.print(response);
			Log.d(TAG, "Sent : " + response);
			// finish
			close();
		} catch (IOException e) {
			// error while reading the socket!
			try {
				close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			gameServer.handleError(e, "Error while reading the socket");
		}

	}

	/**
	 * Closes everything.
	 * 
	 * @throws IOException
	 */
	private void close() throws IOException {
		out.close();
		in.close();
		socket.close();
	}

	/**
	 * @param message
	 * @return response
	 */
	private String processMessage(String message) {
		// get game informations!
		GameManager game = gameServer.getGameActivity().getGamePanel();

		// build the response
		// list all entity coordinates
		// personnages
		for (Personage p : game.getCharacterManager().getCharacterList()) {
			p.getHitBox();
		}
		// enemies
		for(EnemyEntity e : game.getEnemyManager().getEnemyList()){
			
		}
		//items
		for(ItemEntity i : game.getItemManager().getItemList()){
			
		}

		String response = null;
		return response;
	}

}
