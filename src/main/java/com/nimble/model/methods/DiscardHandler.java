package com.nimble.model.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.protocols.DiscardPayload;
import com.nimble.model.Lobby;
import com.nimble.model.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class DiscardHandler extends MethodHandler {

	private WebSocketSession session;

	private DiscardPayload payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(DiscardHandler.class);

	private ObjectMapper mapper;

	public DiscardHandler(WebSocketSession session, DiscardPayload payload, NimbleRepository nimbleRepository,
			ObjectMapper mapper) {
		this.session = session;
		this.payload = payload;
		this.nimbleRepository = nimbleRepository;
		this.mapper = mapper;
	}

	@Override
	public void run() {
		if (!payload.getLobbyId().equals(nimbleRepository.getLobby().getId())) {
			logger.error(String.format("%s quiere levantar de un lobby \"%s\" que no existe!", payload.getName(),
					payload.getLobbyId()));
			return;
		}
		nimbleRepository.getLobby().discard(new User(session, payload.getName()));
		broadcastState(mapper, nimbleRepository.getLobby());
		logger.info(String.format("%s levantó una carta", payload.getName()));
	}

}
