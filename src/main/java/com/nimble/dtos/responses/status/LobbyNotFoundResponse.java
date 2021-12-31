package com.nimble.dtos.responses.status;

public class LobbyNotFoundResponse extends StatusResponse {

	public LobbyNotFoundResponse(String lobbyId) {
		super("error", "El lobby " + lobbyId + " no existe!");
	}

}
