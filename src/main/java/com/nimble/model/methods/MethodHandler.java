package com.nimble.model.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.LobbyDto;
import com.nimble.model.Lobby;
import com.nimble.repositories.NimbleRepository;

import java.io.IOException;

public abstract class MethodHandler {

	public abstract void run();

	public void broadcastState(ObjectMapper mapper, Lobby lobby, NimbleRepository nimbleRepository) {
		// TODO: Mover esto a alguna clase encargada de mandar mensajes?
		lobby.getUsersIds().forEach(userId -> {
			try {

				nimbleRepository.getUser(userId).send(mapper.writeValueAsString(new LobbyDto(lobby, nimbleRepository)));
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}
