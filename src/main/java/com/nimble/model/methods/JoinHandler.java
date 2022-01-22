package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.requests.JoinRequest;
import com.nimble.dtos.responses.EnteringLobbyResponse;
import com.nimble.dtos.responses.LobbyInfoResponse;
import com.nimble.dtos.responses.errors.JoinErrorResponse;
import com.nimble.dtos.responses.errors.StartErrorResponse;
import com.nimble.dtos.responses.errors.UnexpectedErrorResponse;
import com.nimble.exceptions.NoAvailableColorException;
import com.nimble.model.server.Lobby;
import com.nimble.model.server.User;
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

	public JoinHandler(
		WebSocketSession session,
		JoinRequest payload,
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
			messenger.send(session, new UnexpectedErrorResponse("Fijate que queres joinear pero no tenes una sesion!"));
			logger.error("Alguien que no existe se quiere joinear a un lobby!");
			return;
		}
		User user = nimbleRepository.getUser(payload.getSessionId());

		if (!nimbleRepository.containsLobbyKey(payload.getLobbyId())) {
			logger.info(String.format("%s se quiere unir a un lobby que no existe!", payload.getName()));
			messenger.send(
				user.getId(),
				new JoinErrorResponse(String.format("El lobby %s no existe!", payload.getLobbyId()))
			);
			return;
		}

		Lobby lobby = nimbleRepository.getLobby(payload.getLobbyId());

		if (lobby.isFull()) {
			logger.info(
				String.format("%s se quiere unir al lobby %s que ya está lleno", payload.getName(), payload.getLobbyId())
			);
			messenger.send(
				user.getId(),
				new JoinErrorResponse(String.format("El lobby %s ya está lleno (max: 4)", payload.getLobbyId()))
			);
			return;
		}

		if (lobby.isRunning()) {
			logger.info(
				String.format("%s se quiere unir al lobby %s que ya está corriendo!", user.getName(), user.getLobbyId())
			);
			messenger.send(
				user.getId(),
				new StartErrorResponse(String.format("El lobby %s está corriendo! Intentá en un rato", user.getLobbyId()))
			);
			return;
		}

		if (nimbleRepository.namesAtLobby(payload.getLobbyId()).contains(payload.getName())) {
			logger.info(String.format("%s se quiere unir a un lobby en el que ya existe su nombre", payload.getName()));
			messenger.send(
				user.getId(),
				new JoinErrorResponse(
					String.format("%s ya está en uso en el lobby %s", payload.getName(), payload.getLobbyId())
				)
			);
			return;
		}
		user.setLobbyId(payload.getLobbyId());
		user.setName(payload.getName());
		try {
			user.setColor(lobby.getColor());
		} catch (NoAvailableColorException e) {
			logger.error(
				String.format(
					"%s quiso agarrar un color en el lobby %s pero no habia ninguno!",
					payload.getName(),
					payload.getLobbyId()
				)
			);
			e.printStackTrace();
			return;
		}
		lobby.add(payload.getSessionId());

		messenger.send(user.getId(), new EnteringLobbyResponse());
		lobby
			.getUsersIds()
			.forEach(userId -> {
				messenger.send(
					userId,
					new LobbyInfoResponse(lobby.isOwner(userId), nimbleRepository.usersDtoAtLobby(lobby.getId()), lobby.getId())
				);
			});

		logger.info(String.format("%s creó el lobby \"%s\"", payload.getName(), payload.getLobbyId()));
	}
}
