package com.nimble.model.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.protocols.CreatePayload;
import com.nimble.model.Lobby;
import com.nimble.model.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class CreateHandler extends MethodHandler {

	private WebSocketSession session;

	private CreatePayload payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(CreateHandler.class);

	private ObjectMapper mapper;

	public CreateHandler(WebSocketSession session, CreatePayload payload, NimbleRepository nimbleRepository,
			ObjectMapper mapper) {
		// TODO: Seguramente no haga falta pasar tantos argumentos
		this.session = session;
		this.payload = payload;
		this.nimbleRepository = nimbleRepository;
		this.mapper = mapper;
	}

	@Override
	public void run() {
		nimbleRepository.setLobby(new Lobby(payload.getLobbyId(), new User(session, payload.getName())));
		broadcastState(mapper, nimbleRepository.getLobby());

		logger.info(String.format("%s creó el lobby \"%s\"", payload.getName(), payload.getName()));
	}

}
