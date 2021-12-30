package com.nimble.model.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.requests.DiscardRequest;
import com.nimble.model.Lobby;
import com.nimble.model.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class DiscardHandler extends MethodHandler {

	private WebSocketSession session;

	private DiscardRequest payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(DiscardHandler.class);

	private ObjectMapper mapper;

	public DiscardHandler(WebSocketSession session, DiscardRequest payload, NimbleRepository nimbleRepository,
						  ObjectMapper mapper) {
		this.session = session;
		this.payload = payload;
		this.nimbleRepository = nimbleRepository;
		this.mapper = mapper;
	}

	@Override
	public void run() {
		// TODO: Chequear user/lobby existen, chequear que este iniciado
		User user = nimbleRepository.getUser(payload.getSessionId());
		Lobby lobby = nimbleRepository.getLobby(user.getLobbyId());
		lobby.discard(payload.getSessionId());
		broadcastState(mapper, lobby, nimbleRepository);
		logger.info(String.format("%s levantó una carta", user.getName()));
	}

}
