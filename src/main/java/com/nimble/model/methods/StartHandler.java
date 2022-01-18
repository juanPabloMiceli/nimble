package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.LobbyDto;
import com.nimble.dtos.requests.StartRequest;
import com.nimble.dtos.responses.status.SuccessfulResponse;
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
		messenger.broadcastToLobbyOf(user.getId(), new SuccessfulResponse());
		messenger.send(user.getId(), new LobbyDto(lobby, nimbleRepository));
		logger.info(String.format("Se inició el lobby \"%s\"", user.getLobbyId()));
	}
}
