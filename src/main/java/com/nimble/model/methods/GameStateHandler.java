package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.game.GameDto;
import com.nimble.dtos.requests.GameStateRequest;
import com.nimble.dtos.responses.GameStateResponse;
import com.nimble.model.Lobby;
import com.nimble.model.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class GameStateHandler extends MethodHandler {

	private WebSocketSession session;

	private GameStateRequest payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(LobbyInfoHandler.class);

	private Messenger messenger;

	public GameStateHandler(
		WebSocketSession session,
		GameStateRequest payload,
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
		User user = nimbleRepository.getUser(payload.getSessionId());
		Lobby lobby = nimbleRepository.getLobby(user.getLobbyId());

		messenger.send(
			user.getId(),
			new GameStateResponse(
				lobby.getPlayerNumber(user.getId()),
				nimbleRepository.usersDtoAtLobby(lobby.getId()),
				new GameDto(lobby.getGame())
			)
		);

		logger.info(String.format("Listing players for %s", user.getName()));
	}
}
