package com.nimble.model.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.requests.PlayRequest;
import com.nimble.exceptions.service.InvalidPlayFromException;
import com.nimble.model.Lobby;
import com.nimble.model.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class PlayHandler extends MethodHandler {

	private WebSocketSession session;

	private PlayRequest payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(PlayHandler.class);

	private ObjectMapper mapper;

	public PlayHandler(WebSocketSession session, PlayRequest payload, NimbleRepository nimbleRepository,
					   ObjectMapper mapper) {
		this.session = session;
		this.payload = payload;
		this.nimbleRepository = nimbleRepository;
		this.mapper = mapper;
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
			broadcastState(mapper, lobby, nimbleRepository);
		}
		else {
			logger.error(String.format("Sabes jugar %s?\n", user.getName()));
			try {
				user.send("La cagaste amigo");
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
