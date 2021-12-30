package com.nimble.model.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.protocols.JoinPayload;
import com.nimble.model.Lobby;
import com.nimble.model.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class JoinHandler extends MethodHandler {

	private WebSocketSession session;

	private JoinPayload payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(JoinHandler.class);

	private ObjectMapper mapper;

	public JoinHandler(WebSocketSession session, JoinPayload payload, NimbleRepository nimbleRepository,
			ObjectMapper mapper) {
		this.session = session;
		this.payload = payload;
		this.nimbleRepository = nimbleRepository;
		this.mapper = mapper;
	}

	@Override
	public void run() {
		// TODO: Chequear user/lobby existen, chequear que no este iniciado
		User user = nimbleRepository.getUser(payload.getSessionId());
		Lobby lobby = nimbleRepository.getLobby(payload.getLobbyId());

		user.setLobbyId(payload.getLobbyId());
		lobby.add(payload.getSessionId());
		broadcastState(mapper, lobby, nimbleRepository);
		logger.info(String.format("%s creó el lobby \"%s\"", payload.getName(), payload.getLobbyId()));
	}

}
