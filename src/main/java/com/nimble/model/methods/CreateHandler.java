package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.requests.CreateRequest;
import com.nimble.dtos.responses.EnteringLobbyResponse;
import com.nimble.dtos.responses.errors.CreateErrorResponse;
import com.nimble.dtos.responses.errors.UnexpectedErrorResponse;
import com.nimble.exceptions.NoAvailableColorException;
import com.nimble.exceptions.server.NoAvailableLobbiesException;
import com.nimble.model.server.Lobby;
import com.nimble.model.server.LobbyIdGenerator;
import com.nimble.model.server.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class CreateHandler extends MethodHandler {

	private final WebSocketSession session;

	private final CreateRequest payload;

	private final NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(CreateHandler.class);

	private final Messenger messenger;

	private final LobbyIdGenerator lobbyIdGenerator = LobbyIdGenerator.getLobbyIdGeneratorInstance();

	public CreateHandler(
		WebSocketSession session,
		CreateRequest payload,
		NimbleRepository nimbleRepository,
		Messenger messenger
	) {
		// TODO: Seguramente no haga falta pasar tantos argumentos
		this.session = session;
		this.payload = payload;
		this.nimbleRepository = nimbleRepository;
		this.messenger = messenger;
	}

	@Override
	public void run() {
		if (!nimbleRepository.containsUserKey(payload.getSessionId())) {
			logger.error("Alguien que no existe quiere crear partida!");
			messenger.send(session, new UnexpectedErrorResponse("Fijate que querias crear pero no tenes una sesion!"));
			return;
		}
		String lobbyId = null;
		try {
			lobbyId = lobbyIdGenerator.getId();
			logger.info("Creando el lobby");
		} catch (NoAvailableLobbiesException e) {
			logger.warn("Quisieron crear un lobby pero ya estaba todo lleno!");
			messenger.send(session, new CreateErrorResponse("No hay lobbies, volvé a probar en un rato"));
			return;
		}

		User user = nimbleRepository.getUser(payload.getSessionId());
		user.setName(payload.getName());
		user.setLobbyId(lobbyId);
		Lobby lobby = new Lobby(lobbyId, payload.getSessionId());
		try {
			user.setColor(lobby.getColor());
		} catch (NoAvailableColorException e) {
			logger.error(
				String.format("%s quiso agarrar un color en el lobby %s pero no habia ninguno!", payload.getName(), lobbyId)
			);
			e.printStackTrace();
			return;
		}
		nimbleRepository.putLobby(lobbyId, lobby);
		messenger.send(user.getId(), new EnteringLobbyResponse());
		logger.info(String.format("%s creó el lobby \"%s\"", payload.getName(), lobbyId));
	}
}
