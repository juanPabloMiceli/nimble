package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.responses.errors.UnexpectedErrorResponse;
import com.nimble.model.server.Lobby;
import com.nimble.model.server.User;
import com.nimble.repositories.NimbleRepository;
import org.springframework.web.socket.WebSocketSession;

public abstract class MethodHandler {

	public abstract void run();

	public User forceGetUser(
		NimbleRepository nimbleRepository,
		String userId,
		Messenger messenger,
		WebSocketSession session
	) {
		if (!nimbleRepository.containsUserKey(userId)) {
			messenger.send(session, new UnexpectedErrorResponse(String.format("La sesion %s no existe!", userId)));
			return null;
		}
		return nimbleRepository.getUser(userId);
	}

	public Lobby forceGetLobby(
		NimbleRepository nimbleRepository,
		String lobbyId,
		Messenger messenger,
		WebSocketSession session
	) {
		if (!nimbleRepository.containsLobbyKey(lobbyId)) {
			messenger.send(session, new UnexpectedErrorResponse(String.format("El lobby %s no existe!", lobbyId)));
			return null;
		}
		return nimbleRepository.getLobby(lobbyId);
	}
}
