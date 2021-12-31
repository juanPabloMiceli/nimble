package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.requests.CreateRequest;
import com.nimble.dtos.responses.status.SuccessfulResponse;
import com.nimble.dtos.responses.status.UnexpectedErrorResponse;
import com.nimble.model.Lobby;
import com.nimble.model.User;
import com.nimble.repositories.NimbleRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.util.Locale;

public class CreateHandler extends MethodHandler {

	private WebSocketSession session;

	private CreateRequest payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(CreateHandler.class);

	private Messenger messenger;

	public CreateHandler(WebSocketSession session, CreateRequest payload, NimbleRepository nimbleRepository,
			Messenger messenger) {
		// TODO: Seguramente no haga falta pasar tantos argumentos
		this.session = session;
		this.payload = payload;
		this.nimbleRepository = nimbleRepository;
		this.messenger = messenger;
	}

	@Override
	public void run() {
		String lobbyId = "";
		do {
			lobbyId = RandomStringUtils.random(4, true, false).toUpperCase(Locale.ROOT);
			// TODO: Esto esta feo y hay que hacer un generador de lobbys como clase
			// separada
			// entre otras cosas deberia decir si no hay mas lobbies disponibles
		}
		while (nimbleRepository.containsLobbyKey(lobbyId));

		if (!nimbleRepository.containsUserKey(payload.getSessionId())) {
			logger.error("Alguien que no existe quiere crear partida!");
			messenger.send(session, new UnexpectedErrorResponse());

			return;
		}
		User user = nimbleRepository.getUser(payload.getSessionId());
		user.setName(payload.getName());
		user.setLobbyId(lobbyId);
		nimbleRepository.putLobby(lobbyId, new Lobby(lobbyId, payload.getSessionId()));
		messenger.send(user.getId(), new SuccessfulResponse());
		logger.info(String.format("%s creó el lobby \"%s\"", payload.getName(), lobbyId));
	}

}
