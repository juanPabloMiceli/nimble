package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.game.GameDto;
import com.nimble.dtos.game.UserDto;
import com.nimble.dtos.requests.PlayRequest;
import com.nimble.dtos.responses.GameStateResponse;
import com.nimble.dtos.responses.TieResponse;
import com.nimble.dtos.responses.WinnerResponse;
import com.nimble.dtos.responses.errors.InvalidMoveErrorResponse;
import com.nimble.dtos.responses.errors.UnexpectedErrorResponse;
import com.nimble.exceptions.PlayedWhenPenalizedException;
import com.nimble.model.enums.LobbyState;
import com.nimble.model.server.Lobby;
import com.nimble.model.server.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class PlayHandler extends MethodHandler {

	private WebSocketSession session;

	private PlayRequest payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(PlayHandler.class);

	private Messenger messenger;

	public PlayHandler(
		WebSocketSession session,
		PlayRequest payload,
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
			messenger.send(session, new UnexpectedErrorResponse("Fijate que queres jugar pero no tenes una sesion!"));
			logger.error("Alguien que no existe quiere jugar una carta!");
			return;
		}
		User user = nimbleRepository.getUser(payload.getSessionId());

		if (!nimbleRepository.containsLobbyKey(user.getLobbyId())) {
			logger.info(String.format("%s se quiere jugar en un lobby que no existe!", user.getLobbyId()));
			messenger.send(
				user.getId(),
				new UnexpectedErrorResponse(String.format("El lobby %s no existe!", user.getLobbyId()))
			);
			return;
		}
		Lobby lobby = nimbleRepository.getLobby(user.getLobbyId());

		if (!lobby.isRunning()) {
			logger.info(String.format("%s quiere jugar en un lobby que no tiene una partida en curso!", user.getLobbyId()));
			messenger.send(
				user.getId(),
				new InvalidMoveErrorResponse(String.format("El lobby %s no tiene una partida en curso!", user.getLobbyId()))
			);
			return;
		}

		logger.info(String.format("%s quiere jugar desde la mano al mazo %d", user.getName(), payload.getPlayTo()));

		try {
			if (!lobby.playFromHand(payload.getSessionId(), payload.getPlayTo())) {
				logger.error(String.format("Sabes jugar %s?\n", user.getName()));
				messenger.send(user.getId(), new InvalidMoveErrorResponse("Te equivocaste bajando la carta"));
				return;
			}
		} catch (PlayedWhenPenalizedException e) {
			logger.info(String.format("%s quiere jugar una carta pero está penalizado!", user.getName()));
			//			messenger.send(user.getId(), new InvalidMoveErrorResponse("Jugaste antes de tiempo brodi!"));
			return;
		}

		logger.info(String.format("Bien jugado %s!", user.getName()));

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

		if (lobby.isFinished()) {
			lobby.setLobbyState(LobbyState.READY);
			messenger.broadcastToLobbyOf(
				user.getId(),
				new WinnerResponse(new UserDto(nimbleRepository.getUser(lobby.getWinnerId())))
			);
			return;
		}

		if (lobby.isStuck()) {
			lobby.setLobbyState(LobbyState.READY);
			messenger.broadcastToLobbyOf(user.getId(), new TieResponse());
			return;
		}
	}
}
