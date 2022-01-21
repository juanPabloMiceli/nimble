package com.nimble.exceptions.server;

public class NoAvailableLobbiesException extends Exception {

	public NoAvailableLobbiesException() {
		super("No available lobbies!");
	}
}
