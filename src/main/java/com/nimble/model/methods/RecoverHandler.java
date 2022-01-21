package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.game.GameDto;
import com.nimble.dtos.requests.RecoverRequest;
import com.nimble.dtos.responses.GameStateResponse;
import com.nimble.dtos.responses.errors.InvalidMoveErrorResponse;
import com.nimble.dtos.responses.errors.UnexpectedErrorResponse;
import com.nimble.model.server.Lobby;
import com.nimble.model.server.User;
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
		if (!nimbleRepository.containsUserKey(payload.getSessionId())) {
			messenger.send(session, new UnexpectedErrorResponse("Fijate que queres descartar pero no tenes una sesion!"));
			logger.error("Alguien que no existe quiere descartar una carta!");
			return;
		}
		User user = nimbleRepository.getUser(payload.getSessionId());

		if (!nimbleRepository.containsLobbyKey(user.getLobbyId())) {
			logger.info(String.format("%s se quiere descartar en un lobby que no existe!", user.getLobbyId()));
			messenger.send(
				user.getId(),
				new UnexpectedErrorResponse(String.format("El lobby %s no existe!", user.getLobbyId()))
			);
			return;
		}
		Lobby lobby = nimbleRepository.getLobby(user.getLobbyId());

		if (!lobby.isRunning()) {
			logger.info(
				String.format("%s quiere descartar en un lobby que no tiene una partida en curso!", user.getLobbyId())
			);
			messenger.send(
				user.getId(),
				new InvalidMoveErrorResponse(String.format("El lobby %s no tiene una partida en curso!", user.getLobbyId()))
			);
			return;
		}

		lobby.recover(user.getId());

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
