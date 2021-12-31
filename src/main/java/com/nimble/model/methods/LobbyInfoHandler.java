package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.requests.LobbyInfoRequest;
import com.nimble.model.Lobby;
import com.nimble.model.User;
import com.nimble.dtos.responses.LobbyInfoResponse;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class LobbyInfoHandler extends MethodHandler {

	private WebSocketSession session;

	private LobbyInfoRequest payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(LobbyInfoHandler.class);

	private Messenger messenger;

	public LobbyInfoHandler(WebSocketSession session, LobbyInfoRequest payload, NimbleRepository nimbleRepository,
			Messenger messenger) {
		this.session = session;
		this.payload = payload;
		this.nimbleRepository = nimbleRepository;
		this.messenger = messenger;
	}

	@Override
	public void run() {
		User user = nimbleRepository.getUser(payload.getSessionId());
		Lobby lobby = nimbleRepository.getLobby(user.getLobbyId());

		messenger.send(user.getId(),
				new LobbyInfoResponse(nimbleRepository.usersDtoAtLobby(lobby.getId()), lobby.getId()));

		logger.info(String.format("Listing players for %s", user.getName()));

	}

}
