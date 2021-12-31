package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.game.GameDto;
import com.nimble.dtos.requests.PlayRequest;
import com.nimble.dtos.responses.GameStateResponse;
import com.nimble.dtos.responses.status.InvalidPlayResponse;
import com.nimble.exceptions.service.InvalidPlayFromException;
import com.nimble.model.Lobby;
import com.nimble.model.User;
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

	public PlayHandler(WebSocketSession session, PlayRequest payload, NimbleRepository nimbleRepository,
			Messenger messenger) {
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

		Boolean result;

		switch (payload.getPlayFrom()) {
		// TODO: Se puede emprolijar esto? Por ahi pasar 2 metodos distintos para hand vs
		// descarte?
		case HAND:
			logger.info(String.format("%s quiere jugar desde la mano al mazo %d", user.getName(), payload.getPlayTo()));
			result = lobby.playFromHand(payload.getSessionId(), payload.getPlayTo());
			break;
		case DISCARD:
			logger.info(
					String.format("%s quiere jugar desde el descarte al mazo %d", user.getName(), payload.getPlayTo()));
			result = lobby.playFromDiscard(payload.getSessionId(), payload.getPlayTo());
			break;
		default:
			throw new InvalidPlayFromException(payload.getPlayFrom().name());
		}

		if (result) {
			logger.info(String.format("Bien jugado %s!", user.getName()));
			messenger.broadcastToLobbyOf(user.getId(), new GameStateResponse(0,
					nimbleRepository.usersDtoAtLobby(lobby.getId()), new GameDto(lobby.getGame())));
		}
		else {
			logger.error(String.format("Sabes jugar %s?\n", user.getName()));
			messenger.send(user.getId(), new InvalidPlayResponse(user.getName()));
		}
	}

}
