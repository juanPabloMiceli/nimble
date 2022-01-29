package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.game.GameDto;
import com.nimble.dtos.requests.QuitRequest;
import com.nimble.dtos.responses.GameStateResponse;
import com.nimble.dtos.responses.LobbyInfoResponse;
import com.nimble.dtos.responses.errors.UnexpectedErrorResponse;
import com.nimble.exceptions.game.InvalidPlayerNumberException;
import com.nimble.model.server.Lobby;
import com.nimble.model.server.LobbyIdGenerator;
import com.nimble.model.server.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class QuitHandler extends MethodHandler {

	private final WebSocketSession session;

	private final QuitRequest payload;

	private final NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(QuitHandler.class);

	private final Messenger messenger;

	private final LobbyIdGenerator lobbyIdGenerator = LobbyIdGenerator.getLobbyIdGeneratorInstance();

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
		if (!nimbleRepository.containsUserKey(payload.getSessionId())) {
			logger.error("Alguien que no existe quiere quitear!");
			messenger.send(session, new UnexpectedErrorResponse("Fijate que queres quitear pero no tenes una sesion!"));
			return;
		}
		User user = nimbleRepository.getUser(payload.getSessionId());
		if (user.getLobbyId().equals("")) {
			messenger.send(
				user.getId(),
				new UnexpectedErrorResponse("Fijate que queres quitear pero no perteneces a ningun lobby")
			);
			logger.error(String.format("%s quiere salir de un lobby pero no pertenece a ninguno!!!", user.getName()));
			return;
		}
		if (!nimbleRepository.containsLobbyKey(user.getLobbyId())) {
			logger.error(String.format("%s quiere salir del lobby %s que no existe!", user.getName(), user.getLobbyId()));
			messenger.send(
				session,
				new UnexpectedErrorResponse(
					String.format("Fijate que queres salirte de un lobby que no existe (%s)", user.getLobbyId())
				)
			);
			return;
		}
		Lobby lobby = nimbleRepository.getLobby(user.getLobbyId());
		lobby.remove(user.getId());
		lobby.restoreColor(user.getColor());
		nimbleRepository.removeUser(user.getId());

		for (int playerNumber = 0; playerNumber < lobby.getUsersIds().size(); playerNumber++) {
			if (lobby.isRunning()) {
				try {
					messenger.send(
						lobby.getUserId(playerNumber),
						new GameStateResponse(
							playerNumber,
							nimbleRepository.usersDtoAtLobby(lobby.getId()),
							new GameDto(lobby.getGame())
						)
					);
				} catch (InvalidPlayerNumberException e) {
					throw new RuntimeException("WUT");
				}
				continue;
			}
			if (lobby.isReady()) {
				try {
					messenger.send(
						lobby.getUserId(playerNumber),
						new LobbyInfoResponse(
							playerNumber == 0,
							nimbleRepository.usersDtoAtLobby(lobby.getId()),
							lobby.getId(),
							lobby.getPenalties()
						)
					);
				} catch (InvalidPlayerNumberException e) {
					throw new RuntimeException("WUT");
				}
				continue;
			}
			logger.error(
				String.format(
					"%s quiere salirse del lobby %s pero el lobby no está ready ni running!!",
					user.getName(),
					lobby.getId()
				)
			);
			messenger.send(
				user.getId(),
				new UnexpectedErrorResponse(
					String.format(
						"%s quiere salirse del lobby %s pero el lobby no está ready ni running!!",
						user.getName(),
						lobby.getId()
					)
				)
			);
			return;
		}
		logger.info(String.format("%s salió del lobby \"%s\"", user.getName(), user.getLobbyId()));

		if (lobby.isEmpty()) {
			nimbleRepository.removeLobby(lobby.getId());
			lobbyIdGenerator.restore(lobby.getId());
		}
	}
}
