package com.nimble.model.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.requests.CreateRequest;
import com.nimble.dtos.responses.StatusResponse;
import com.nimble.model.Lobby;
import com.nimble.model.User;
import com.nimble.repositories.NimbleRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class CreateHandler extends MethodHandler {

	private WebSocketSession session;

	private CreateRequest payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(CreateHandler.class);

	private ObjectMapper mapper;

	public CreateHandler(WebSocketSession session, CreateRequest payload, NimbleRepository nimbleRepository,
						 ObjectMapper mapper) {
		// TODO: Seguramente no haga falta pasar tantos argumentos
		this.session = session;
		this.payload = payload;
		this.nimbleRepository = nimbleRepository;
		this.mapper = mapper;
	}

	@Override
	public void run() {
		String lobbyId = "";
		do{
			lobbyId = RandomStringUtils.random(2, true, false);
			//TODO: Esto esta feo y hay que hacer un generador de lobbys como clase separada
			//entre otras cosas deberia decir si no hay mas lobbies disponibles
		}while(nimbleRepository.containsLobbyKey(lobbyId));

		if (!nimbleRepository.containsUserKey(payload.getSessionId())) {
			logger.error("Alguien que no existe quiere crear partida!");
			return;
		}
		User user = nimbleRepository.getUser(payload.getSessionId());
		user.setName(payload.getName());
		user.setLobbyId(lobbyId);
		nimbleRepository.putLobby(lobbyId, new Lobby(lobbyId, payload.getSessionId()));
		try {
			user.send(mapper.writeValueAsString(StatusResponse.SuccessfulResponse("operation_status")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info(String.format("%s creó el lobby \"%s\"", payload.getName(), lobbyId));
	}
}
