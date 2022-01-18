package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.game.GameDto;
import com.nimble.dtos.requests.RecoverRequest;
import com.nimble.dtos.responses.GameStateResponse;
import com.nimble.dtos.responses.status.InvalidPlayResponse;
import com.nimble.model.Lobby;
import com.nimble.model.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class RecoverHandler extends MethodHandler {

	private WebSocketSession session;

	private RecoverRequest payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(RecoverHandler.class);

	private Messenger messenger;

	public RecoverHandler(
		WebSocketSession session,
		RecoverRequest payload,
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

		if (!lobby.recover(payload.getSessionId())) {
			messenger.send(payload.getSessionId(), new InvalidPlayResponse(user.getName()));
			logger.info(String.format("%s, no podes recuperar una carta que no descartaste", user.getName()));
			return;
		}

		logger.info(String.format("%s recuperó una carta", user.getName()));

		for (int playerNumber = 0; playerNumber < lobby.getUsersIds().size(); playerNumber++) {
			messenger.send(
				lobby.getUsersIds().get(playerNumber),
				new GameStateResponse(
					playerNumber,
					nimbleRepository.usersDtoAtLobby(lobby.getId()),
					new GameDto(lobby.getGame())
				)
			);
		}
	}
}
