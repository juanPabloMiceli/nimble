package com.nimble.model.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.protocols.JoinPayload;
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
		if (!payload.getLobbyId().equals(nimbleRepository.getLobby().getId())) {
			logger.error(String.format("%s se quiere unir al lobby \"%s\" que no existe!", payload.getName(),
					payload.getLobbyId()));
			return;
		}
		nimbleRepository.getLobby().add(new User(session, payload.getName()));
		broadcastState(mapper, nimbleRepository.getLobby());
		logger.info(String.format("%s creó el lobby \"%s\"", payload.getName(), payload.getLobbyId()));
	}

}
