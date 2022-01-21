package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.requests.StartRequest;
import com.nimble.dtos.responses.EnteringGameResponse;
import com.nimble.dtos.responses.errors.StartErrorResponse;
import com.nimble.dtos.responses.errors.UnexpectedErrorResponse;
import com.nimble.model.server.Lobby;
import com.nimble.model.server.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class StartHandler extends MethodHandler {

	private WebSocketSession session;

	private StartRequest payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(StartHandler.class);

	private Messenger messenger;

	public StartHandler(
		WebSocketSession session,
		StartRequest payload,
		NimbleRepository nimbleRepository,
		Messenger messenger
	) {
		this.session = session;
		this.payload = payload;
		this.nimbleRepository = nimbleRepository;
		this.messenger = messenger;
	}

	@Override
	public void run() {
		if (!nimbleRepository.containsUserKey(payload.getSessionId())) {
			messenger.send(session, new UnexpectedErrorResponse());
			logger.error("Alguien que no existe se quiere iniciar la partida!");
			return;
		}
		User user = nimbleRepository.getUser(payload.getSessionId());
		if (user.getLobbyId().equals("")) {
			messenger.send(user.getId(), new UnexpectedErrorResponse());
			logger.error("Alguien que no tiene lobby lo quiere iniciar!!!");
			return;
		}
		if (!nimbleRepository.containsLobbyKey(user.getLobbyId())) {
			logger.error(String.format("El lobby %s no existe!", user.getLobbyId()));
			messenger.send(user.getId(), new StartErrorResponse(String.format("El lobby %s no existe!", user.getLobbyId())));
			return;
		}
		Lobby lobby = nimbleRepository.getLobby(user.getLobbyId());
		if(lobby.isRunning()){
			logger.info(String.format("El lobby %s ya está corriendo!", user.getLobbyId()));
			messenger.send(user.getId(), new StartErrorResponse(String.format("El lobby %s ya está corriendo!", user.getLobbyId())));
			return;
		}
		if(!lobby.isOwner(user.getId())){
			logger.error(String.format("No sos el owner para poder empezar el lobby %s", user.getLobbyId()));
			messenger.send(user.getId(), new StartErrorResponse(String.format("No sos el owner para poder empezar el lobby %s", user.getLobbyId())));
			return;
		}
		lobby.start();
		messenger.broadcastToLobbyOf(user.getId(), new EnteringGameResponse());
		logger.info(String.format("Se inició el lobby \"%s\"", user.getLobbyId()));
	}
}
