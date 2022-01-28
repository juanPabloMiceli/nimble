package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.game.GameDto;
import com.nimble.dtos.requests.ReconnectRequest;
import com.nimble.dtos.responses.GameStateResponse;
import com.nimble.dtos.responses.LobbyInfoResponse;
import com.nimble.dtos.responses.errors.UnexpectedErrorResponse;
import com.nimble.model.server.Lobby;
import com.nimble.model.server.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class ReconnectHandler extends MethodHandler {

	private WebSocketSession session;

	private ReconnectRequest payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(PlayHandler.class);

	private Messenger messenger;

	public ReconnectHandler(
		WebSocketSession session,
		ReconnectRequest payload,
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
			logger.error("Alguien quiere reconectarse sin tener una sesion!");
			messenger.send(
				session,
				new UnexpectedErrorResponse("Fijate que te queres reconectar pero no tenias una sesion!")
			);
			return;
		}
		User user = nimbleRepository.getUser(payload.getSessionId());
		user.setSession(session);

		if (!nimbleRepository.containsLobbyKey(user.getLobbyId()) || user.getLobbyId().equals("")) {
			logger.error(String.format("%s quiere reconectarse pero no pertenece a ningun lobby!", user.getLobbyId()));
			messenger.send(session, new UnexpectedErrorResponse("Fijate que te queres reconectar pero no tenias un lobby!"));
			return;
		}
		Lobby lobby = nimbleRepository.lobbyOf(user.getId());
		nimbleRepository.putUser(payload.getSessionId(), user);
		if (lobby.isRunning()) {
			logger.info(String.format("%s quiere reconectarse a la partida %s", user.getName(), lobby.getId()));
			messenger.send(
				user.getId(),
				new GameStateResponse(
					lobby.getPlayerNumber(user.getId()),
					nimbleRepository.usersDtoAtLobby(lobby.getId()),
					new GameDto(lobby.getGame())
				)
			);
			return;
		}
		if (lobby.isReady()) {
			logger.info(String.format("%s quiere reconectarse al lobby %s", user.getName(), lobby.getId()));
			messenger.send(
				user.getId(),
				new LobbyInfoResponse(
					lobby.isOwner(user.getId()),
					nimbleRepository.usersDtoAtLobby(lobby.getId()),
					lobby.getId(),
					lobby.getPenalties()
				)
			);
			return;
		}
		logger.error(
			String.format(
				"%s quiere reconectarse al lobby %s pero el lobby no está ready ni running!!",
				user.getName(),
				lobby.getId()
			)
		);
		messenger.send(
			user.getId(),
			new UnexpectedErrorResponse(
				String.format(
					"%s quiere reconectarse al lobby %s pero el lobby no está ready ni running!!",
					user.getName(),
					lobby.getId()
				)
			)
		);
		return;
	}
}
