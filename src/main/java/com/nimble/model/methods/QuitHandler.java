package com.nimble.model.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.protocols.QuitPayload;
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
		if (!payload.getLobbyId().equals(nimbleRepository.getLobby().getId())) {
			logger.error(String.format("%s se quiere ir de un lobby \"%s\" que no existe!", payload.getName(),
					payload.getLobbyId()));
			return;
		}
		nimbleRepository.getLobby().remove(new User(session, payload.getName()));
		broadcastState(mapper, nimbleRepository.getLobby());
		logger.info(String.format("%s salió del lobby \"%s\"", payload.getName(), payload.getLobbyId()));
	}

}
