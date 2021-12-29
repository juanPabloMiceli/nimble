package com.nimble.model.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.LobbyDto;
import com.nimble.model.Lobby;

import java.io.IOException;

public abstract class MethodHandler {

	public abstract void run();

	public void broadcastState(ObjectMapper mapper, Lobby lobby) {
		// TODO: Mover esto a alguna clase encargada de mandar mensajes?
		lobby.getUsers().forEach(user -> {
			try {
				user.send(mapper.writeValueAsString(new LobbyDto(lobby)));
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}
