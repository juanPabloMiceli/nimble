package com.nimble.model.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.protocols.StartPayload;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class StartHandler extends MethodHandler {

	private WebSocketSession session;

	private StartPayload payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(StartHandler.class);

	private ObjectMapper mapper;

	public StartHandler(WebSocketSession session, StartPayload payload, NimbleRepository nimbleRepository,
			ObjectMapper mapper) {
		this.session = session;
		this.payload = payload;
		this.nimbleRepository = nimbleRepository;
		this.mapper = mapper;
	}

	@Override
	public void run() {
		if (!payload.getLobbyId().equals(nimbleRepository.getLobby().getId())) {
			logger.error(String.format("%s quiere iniciar un lobby \"%s\" que no existe!", payload.getName(),
					payload.getLobbyId()));
			return;
		}
		nimbleRepository.getLobby().start();
		broadcastState(mapper, nimbleRepository.getLobby());
		logger.info(String.format("Se inició el lobby \"%s\"", payload.getLobbyId()));
	}

}
