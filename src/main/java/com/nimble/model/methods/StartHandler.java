package com.nimble.model.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.requests.StartRequest;
import com.nimble.model.Lobby;
import com.nimble.model.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class StartHandler extends MethodHandler {

	private WebSocketSession session;

	private StartRequest payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(StartHandler.class);

	private ObjectMapper mapper;

	public StartHandler(WebSocketSession session, StartRequest payload, NimbleRepository nimbleRepository,
						ObjectMapper mapper) {
		this.session = session;
		this.payload = payload;
		this.nimbleRepository = nimbleRepository;
		this.mapper = mapper;
	}

	@Override
	public void run() {

		if (!nimbleRepository.containsUserKey(payload.getSessionId())) {
			throw new RuntimeException("Alguien que no existe quiere iniciar!!!");
		}
		User user = nimbleRepository.getUser(payload.getSessionId());
		if (user.getLobbyId().equals("")) {
			throw new RuntimeException("Alguien que no tiene lobby lo quiere iniciar!!!");
		}
		// TODO: Chequear que no se haya iniciado el lobby antes
		// TODO: Chequear que el lobby exista
		Lobby lobby = nimbleRepository.getLobby(user.getLobbyId());
		lobby.start();
		broadcastState(mapper, lobby, nimbleRepository);
		logger.info(String.format("Se inició el lobby \"%s\"", user.getLobbyId()));
	}

}
