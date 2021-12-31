package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.requests.JoinRequest;
import com.nimble.dtos.responses.status.LobbyNotFoundResponse;
import com.nimble.dtos.responses.status.NameAlreadyInLobbyResponse;
import com.nimble.dtos.responses.status.SuccessfulResponse;
import com.nimble.dtos.responses.status.UnexpectedErrorResponse;
import com.nimble.model.Lobby;
import com.nimble.model.User;
import com.nimble.dtos.responses.LobbyInfoResponse;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;


public class JoinHandler extends MethodHandler {

	private WebSocketSession session;

	private JoinRequest payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(JoinHandler.class);

	private Messenger messenger;

	public JoinHandler(WebSocketSession session, JoinRequest payload, NimbleRepository nimbleRepository,
			Messenger messenger) {
		this.session = session;
		this.payload = payload;
		this.nimbleRepository = nimbleRepository;
		this.messenger = messenger;
	}

	@Override
	public void run() {
		// TODO: Chequear que no este iniciado
		if (!nimbleRepository.containsUserKey(payload.getSessionId())) {
			messenger.send(session, new UnexpectedErrorResponse());
			logger.error("Alguien que no existe se quiere joinear a un lobby!");
			return;
		}
		User user = nimbleRepository.getUser(payload.getSessionId());

		if (!nimbleRepository.containsLobbyKey(payload.getLobbyId())) {
			logger.info(String.format("%s se quiere unir a un lobby que no existe!", user.getName()));

			messenger.send(user.getId(), new LobbyNotFoundResponse(payload.getLobbyId()));
			return;
		}

		Lobby lobby = nimbleRepository.getLobby(payload.getLobbyId());

		if (nimbleRepository.namesAtLobby(payload.getLobbyId()).contains(payload.getName())) {
			logger.info(String.format("%s se quiere unir a un lobby en el que ya existe su nombre", payload.getName()));

			messenger.send(user.getId(), new NameAlreadyInLobbyResponse(payload.getLobbyId(), payload.getName()));
			return;
		}
		user.setLobbyId(payload.getLobbyId());
		user.setName(payload.getName());
		lobby.add(payload.getSessionId());

		messenger.send(user.getId(), new SuccessfulResponse());

		nimbleRepository.usersAtLobby(lobby.getId()).forEach(_user -> {
			messenger.send(_user.getId(),
					new LobbyInfoResponse(nimbleRepository.usersDtoAtLobby(lobby.getId()), lobby.getId()));
		});

		logger.info(String.format("%s creó el lobby \"%s\"", payload.getName(), payload.getLobbyId()));
	}

}
