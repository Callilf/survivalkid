package com.survivalkid.net;

import org.json.JSONArray;

public class ServerCommand {
	
	private String command;
	private JSONArray inputs;
	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}
	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}
	/**
	 * @return the inputs
	 */
	public JSONArray getInputs() {
		return inputs;
	}
	/**
	 * @param inputs the inputs to set
	 */
	public void setInputs(JSONArray inputs) {
		this.inputs = inputs;
	}
	
	

}
