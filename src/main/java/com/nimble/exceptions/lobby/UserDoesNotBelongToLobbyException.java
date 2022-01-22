package com.nimble.exceptions.lobby;

public class UserDoesNotBelongToLobbyException extends RuntimeException {

	public UserDoesNotBelongToLobbyException(String userId, String lobbyId) {
		super(userId + " does not belong to lobby \"" + lobbyId + "\"!!!");
	}
}
