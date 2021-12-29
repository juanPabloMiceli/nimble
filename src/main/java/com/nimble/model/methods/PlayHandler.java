package com.nimble.model.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.protocols.PlayPayload;
import com.nimble.exceptions.service.InvalidPlayFromException;
import com.nimble.model.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class PlayHandler extends MethodHandler {

	private WebSocketSession session;

	private PlayPayload payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(PlayHandler.class);

	private ObjectMapper mapper;

	public PlayHandler(WebSocketSession session, PlayPayload payload, NimbleRepository nimbleRepository,
			ObjectMapper mapper) {
		this.session = session;
		this.payload = payload;
		this.nimbleRepository = nimbleRepository;
		this.mapper = mapper;
	}

	@Override
	public void run() {

		if (!payload.getLobbyId().equals(nimbleRepository.getLobby().getId())) {
			// TODO: Esto se va a re ir
			logger.error(String.format("%s quiere jugar en un lobby \"%s\" que no existe!", payload.getName(),
					payload.getLobbyId()));
			return;
		}

		Boolean result;

		switch (payload.getPlayFrom()) {
		// TODO: Se puede emprolijar esto? Por ahi pasar 2 metodos distintos para hand vs
		// descarte?
		case HAND:
			logger.info(
					String.format("%s quiere jugar desde la mano al mazo %d", payload.getName(), payload.getPlayTo()));
			result = nimbleRepository.getLobby().playFromHand(new User(session, payload.getName()),
					payload.getPlayTo());
			break;
		case DISCARD:
			logger.info(String.format("%s quiere jugar desde el descarte al mazo %d", payload.getName(),
					payload.getPlayTo()));
			result = nimbleRepository.getLobby().playFromDiscard(new User(session, payload.getName()),
					payload.getPlayTo());
			break;
		default:
			throw new InvalidPlayFromException(payload.getPlayFrom().name());
		}

		if (result) {
			logger.info(String.format("Bien jugado %s!", payload.getName()));
			broadcastState(mapper, nimbleRepository.getLobby());
		}
		else {
			logger.error(String.format("Sabes jugar %s?\n", payload.getName()));
			try {
				new User(session, payload.getName()).send("La cagaste amigo");
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
