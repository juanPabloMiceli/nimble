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
		if (payload.getLobbyId().equals("")) {
			throw new RuntimeException("Invalid lobbyId!!!");
		}
		if (!nimbleRepository.containsUserKey(payload.getId())) {
			logger.error("Alguien que no existe quiere crear partida!");
			return;
		}
		User user = nimbleRepository.getUser(session.getId());
		user.setName(payload.getName());
		user.setLobbyId(payload.getLobbyId());
		nimbleRepository.putLobby(payload.getLobbyId(), new Lobby(payload.getLobbyId(), user));
		broadcastState(mapper, nimbleRepository.getLobby(payload.getLobbyId()));

		logger.info(String.format("%s creó el lobby \"%s\"", payload.getName(), payload.getLobbyId()));
	}

}
