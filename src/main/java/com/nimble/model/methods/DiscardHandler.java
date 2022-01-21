package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.game.GameDto;
import com.nimble.dtos.requests.DiscardRequest;
import com.nimble.dtos.responses.GameStateResponse;
import com.nimble.dtos.responses.errors.InvalidMoveErrorResponse;
import com.nimble.dtos.responses.errors.UnexpectedErrorResponse;
import com.nimble.model.server.Lobby;
import com.nimble.model.server.User;
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
		if (!nimbleRepository.containsUserKey(payload.getSessionId())) {
			messenger.send(
				session,
				new UnexpectedErrorResponse("Fijate que queres recuperar una carta pero no tenes una sesion!")
			);
			logger.error("Alguien que no existe quiere recuperar una carta!");
			return;
		}
		User user = nimbleRepository.getUser(payload.getSessionId());

		if (!nimbleRepository.containsLobbyKey(user.getLobbyId())) {
			logger.info(String.format("%s quiere recuperar una carta en un lobby que no existe!", user.getLobbyId()));
			messenger.send(
				user.getId(),
				new UnexpectedErrorResponse(String.format("El lobby %s no existe!", user.getLobbyId()))
			);
			return;
		}
		Lobby lobby = nimbleRepository.getLobby(user.getLobbyId());

		if (!lobby.isRunning()) {
			logger.info(
				String.format("%s quiere recuperar una carta en un lobby que no tiene una partida en curso!", user.getLobbyId())
			);
			messenger.send(
				user.getId(),
				new InvalidMoveErrorResponse(String.format("El lobby %s no tiene una partida en curso!", user.getLobbyId()))
			);
			return;
		}

		lobby.discard(payload.getSessionId());

		for (int playerNumber = 0; playerNumber < lobby.getUsersIds().size(); playerNumber++) {
			messenger.send(
				lobby.getUser(playerNumber),
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
