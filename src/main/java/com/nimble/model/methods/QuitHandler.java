package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.requests.QuitRequest;
import com.nimble.dtos.responses.LobbyInfoResponse;
import com.nimble.model.Lobby;
import com.nimble.model.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class QuitHandler extends MethodHandler {

	private WebSocketSession session;

	private QuitRequest payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(QuitHandler.class);

	private Messenger messenger;

	public QuitHandler(
		WebSocketSession session,
		QuitRequest payload,
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
		// TODO: Chequear user/lobby existen, chequear user pertenece al lobby
		User user = nimbleRepository.getUser(payload.getSessionId());
		Lobby lobby = nimbleRepository.getLobby(user.getLobbyId());
		lobby.remove(payload.getSessionId());

		for (int playerNumber = 0; playerNumber < lobby.getUsersIds().size(); playerNumber++) {
			messenger.send(
				lobby.getUsersIds().get(playerNumber),
				new LobbyInfoResponse(playerNumber, nimbleRepository.usersDtoAtLobby(lobby.getId()), lobby.getId())
			);
		}

		logger.info(String.format("%s salió del lobby \"%s\"", user.getName(), user.getLobbyId()));
	}
}
