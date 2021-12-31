package com.nimble.dtos.responses.status;

public class NameAlreadyInLobbyResponse extends StatusResponse {

	public NameAlreadyInLobbyResponse(String lobbyId, String name) {
		super("error", name + " ya está en uso en el lobby " + lobbyId);
	}

}
