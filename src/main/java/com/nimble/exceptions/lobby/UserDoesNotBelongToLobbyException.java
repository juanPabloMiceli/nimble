package com.nimble.exceptions.lobby;

public class UserDoesNotBelongToLobbyException extends RuntimeException {

	public UserDoesNotBelongToLobbyException(String userName, String lobbyId) {
		super(userName + " does not belong to lobby \"" + lobbyId + "\"!!!");
	}
}
