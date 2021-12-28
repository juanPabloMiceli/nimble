package com.nimble.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.LobbyDto;
import com.nimble.dtos.enums.PlayFrom;
import com.nimble.exceptions.service.InvalidPlayFromException;
import com.nimble.model.Lobby;
import com.nimble.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NimbleService {

	private Lobby lobby;

	private final ObjectMapper mapper = new ObjectMapper();

	private final Logger logger = LoggerFactory.getLogger(NimbleService.class);


	public void create(String lobbyId, User user) {
		lobby = new Lobby(lobbyId, user);
		broadcastState(lobby);

		logger.info(String.format("%s creó el lobby \"%s\"", user.getName(), lobbyId));
	}

	public void join(String lobbyId, User user) {
		if (!lobbyId.equals(lobby.getId())) {
			logger.error(String.format("%s se quiere unir al lobby \"%s\" que no existe!", user.getName(), lobbyId));
			return;
		}
		lobby.add(user);
		broadcastState(lobby);
		logger.info(String.format("%s creó el lobby \"%s\"", user.getName(), lobbyId));

	}

	public void quit(String lobbyId, User user) {
		if (!lobbyId.equals(lobby.getId())) {
			logger.error(String.format("%s se quiere ir de un lobby \"%s\" que no existe!", user.getName(), lobbyId));
			return;
		}
		lobby.remove(user);
		broadcastState(lobby);
		logger.info(String.format("%s salió del lobby \"%s\"", user.getName(), lobbyId));
	}

	public void start(String lobbyId, User user) {
		if (!lobbyId.equals(lobby.getId())) {
			logger.error(String.format("%s quiere iniciar un lobby \"%s\" que no existe!", user.getName(), lobbyId));
			return;
		}
		lobby.start();
		broadcastState(lobby);
		logger.info(String.format("Se inició el lobby \"%s\"", lobbyId));
	}

	public void draw(String lobbyId, User user) {
		if (!lobbyId.equals(lobby.getId())) {
			logger.error(
					String.format("%s quiere levantar de un lobby \"%s\" que no existe!", user.getName(), lobbyId));
			return;
		}
		lobby.draw(user);
		broadcastState(lobby);
		logger.info(String.format("%s levantó una carta", user.getName()));
	}

	public void play(String lobbyId, User user, PlayFrom playFrom, int playTo) {
		if (!lobbyId.equals(lobby.getId())) {// TODO: Esto se va a re ir
			logger.error(String.format("%s quiere jugar en un lobby \"%s\" que no existe!", user.getName(), lobbyId));
			return;
		}

		Boolean result;

		switch (playFrom) {
		// TODO: Se puede emprolijar esto?
		case HAND:
			logger.info(String.format("%s quiere jugar desde la mano al mazo %d", user.getName(), playTo));
			result = lobby.playFromHand(user, playTo);
			break;
		case DISCARD:
			logger.info(String.format("%s quiere jugar desde el descarte al mazo %d", user.getName(), playTo));
			result = lobby.playFromDiscard(user, playTo);
			break;
		default:
			throw new InvalidPlayFromException(playFrom.name());
		}

		if (result) {
			logger.info(String.format("Bien jugado %s!", user.getName()));
			broadcastState(lobby);
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

	private void broadcastState(Lobby lobby) {
		lobby.getUsers().forEach(user -> {
			try {
				user.send(mapper.writeValueAsString(new LobbyDto(lobby)));
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}
