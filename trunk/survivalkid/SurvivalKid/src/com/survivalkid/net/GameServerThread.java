package com.survivalkid.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
		} catch (JSONException e) {
			// error while reading the socket!
			try {
				close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			gameServer.handleError(e, "Error while reading the JSON message");
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
	 * @throws JSONException
	 *             If the test is not valid
	 * @throws IOException
	 */
	private String processMessage(String message) throws JSONException,
			IOException {
		JSONObject response = null;

		ServerCommand command = parseCommand(message);

		if ("UPDATE".equals(command.getCommand())) {

			// get game informations!
			GameManager game = gameServer.getGameActivity().getGamePanel();

			// build the response
			response = buildUpdateResponse(game);
		} else if ("CONNECT".equals(command.getCommand())) {
			if (!gameServer.isHasClient()) {
				gameServer.onClientConnected();
			}
		} else if ("DISCONNECT".equals(command.getCommand())) {
			gameServer.onClientDisconnected();
		} else {
			// wrong command
			close();
			gameServer.handleError(null,
					"command not supported : " + command.getCommand());
		}
		return response.toString();
	}

	private JSONObject buildUpdateResponse(GameManager game)
			throws JSONException {
		JSONObject res = new JSONObject();
		// list all entity coordinates
		// personnages
		JSONArray personnages = new JSONArray();
		res.put("personnages", personnages);
		for (Personage p : game.getCharacterManager().getCharacterList()) {
			personnages.put(buildPersonnageJSON(p));
		}
		// enemies
		JSONArray enemies = new JSONArray();
		res.put("enemies", enemies);
		for (EnemyEntity e : game.getEnemyManager().getEnemyList()) {
			enemies.put(buildEnemyJSON(e));
		}
		// items
		JSONArray items = new JSONArray();
		res.put("items",items);
		for (ItemEntity i : game.getItemManager().getItemList()) {
			items.put(buildItemJSON(i));
		}
		//get game state
		// TODO
		return res;
	}

	private JSONObject buildItemJSON(ItemEntity i) throws JSONException {
		JSONObject j = new JSONObject();
		j.accumulate("x", i.getHitBox().left);
		j.accumulate("y", i.getHitBox().top);
		j.accumulate("direction", i.getDirection());
		j.accumulate("id", i.getId());
		j.accumulate("baloon", i.isInBalloon());
		return j;
	}

	private JSONObject buildEnemyJSON(EnemyEntity e) throws JSONException {
		JSONObject j = new JSONObject();
		j.accumulate("x", e.getHitBox().left);
		j.accumulate("y", e.getHitBox().top);
		j.accumulate("direction", e.getDirection());
		j.accumulate("id", e.getId());
		return j;
	}

	private JSONObject buildPersonnageJSON(Personage p) throws JSONException {
		JSONObject j = new JSONObject();
		j.accumulate("x", p.getHitBox().left);
		j.accumulate("y", p.getHitBox().top);
		j.accumulate("item", p.getBag().getStoredItem());
		j.accumulate("life", p.getLife());
		j.accumulate("direction", p.getDirection());
		j.accumulate("type", p.getPersoType());
		j.accumulate("dead", p.isDead());
		j.accumulate("id", p.getId());
		return j;
	}

	private ServerCommand parseCommand(String message) throws JSONException {
		ServerCommand command = new ServerCommand();
		JSONObject jsonMsg = new JSONObject(message);
		command.setCommand(jsonMsg.getString("command"));
		command.setInputs(jsonMsg.getJSONArray("inputs"));
		return command;
	}

}
