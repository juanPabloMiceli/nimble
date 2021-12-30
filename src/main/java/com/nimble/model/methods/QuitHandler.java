package com.nimble.model.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.protocols.QuitPayload;
import com.nimble.model.Lobby;
import com.nimble.model.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class QuitHandler extends MethodHandler {

	private WebSocketSession session;

	private QuitPayload payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(QuitHandler.class);

	private ObjectMapper mapper;

	public QuitHandler(WebSocketSession session, QuitPayload payload, NimbleRepository nimbleRepository,
			ObjectMapper mapper) {
		this.session = session;
		this.payload = payload;
		this.nimbleRepository = nimbleRepository;
		this.mapper = mapper;
	}

	@Override
	public void run() {
		// TODO: Chequear user/lobby existen, chequear user pertenece al lobby
		User user = nimbleRepository.getUser(payload.getId());
		Lobby lobby = nimbleRepository.getLobby(user.getLobbyId());
		lobby.remove(user);
		broadcastState(mapper, lobby);
		logger.info(String.format("%s salió del lobby \"%s\"", user.getName(), user.getLobbyId()));
	}

}
