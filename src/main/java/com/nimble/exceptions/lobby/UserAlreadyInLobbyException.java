package com.nimble.exceptions.lobby;

public class UserAlreadyInLobbyException extends RuntimeException {

	public UserAlreadyInLobbyException(String name, String id) {
		super(name + " already in lobby \"" + id + "\"!!!");
	}
}
