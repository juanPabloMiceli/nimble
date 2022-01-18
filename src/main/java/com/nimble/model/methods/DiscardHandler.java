package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.game.GameDto;
import com.nimble.dtos.requests.DiscardRequest;
import com.nimble.dtos.responses.GameStateResponse;
import com.nimble.model.Lobby;
import com.nimble.model.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class DiscardHandler extends MethodHandler {

	private WebSocketSession session;

	private DiscardRequest payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(DiscardHandler.class);

	private Messenger messenger;

	public DiscardHandler(
		WebSocketSession session,
		DiscardRequest payload,
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
		// TODO: Chequear user/lobby existen, chequear que este iniciado
		User user = nimbleRepository.getUser(payload.getSessionId());
		Lobby lobby = nimbleRepository.getLobby(user.getLobbyId());
		lobby.discard(payload.getSessionId());

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

		logger.info(String.format("%s levantó una carta", user.getName()));
	}
}
