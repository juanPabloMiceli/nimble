package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.requests.SetPenaltiesRequest;
import com.nimble.dtos.responses.LobbyInfoResponse;
import com.nimble.exceptions.SetPenaltiesErrorResponse;
import com.nimble.model.server.Lobby;
import com.nimble.model.server.TimePenalties;
import com.nimble.model.server.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.time.Duration;

public class SetPenaltiesHandler extends MethodHandler {

	private final WebSocketSession session;

	private final SetPenaltiesRequest payload;

	private final NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(SetPenaltiesHandler.class);

	private final Messenger messenger;

	public SetPenaltiesHandler(
		WebSocketSession session,
		SetPenaltiesRequest payload,
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
		User user = super.forceGetUser(nimbleRepository, payload.getSessionId(), messenger, session);
		if (user == null) {
			logger.warn("Alguien que no existe quiere modificar los penalties!");
			return;
		}
		Lobby lobby = super.forceGetLobby(nimbleRepository, user.getLobbyId(), messenger, session);
		if (lobby == null) {
			logger.warn(
				String.format(
					"%s quiere modificar los penalties en el lobby %s que no existe!",
					user.getName(),
					user.getLobbyId()
				)
			);
			return;
		}
		if (!lobby.isOwner(user.getId())) {
			logger.warn(
				String.format(
					"%s quiere cambiar los penalties del lobby %s, pero no es el owner!",
					user.getName(),
					lobby.getId()
				)
			);
			messenger.send(
				session,
				new SetPenaltiesErrorResponse(String.format("No sos el owner del lobby %s!", user.getLobbyId()))
			);
		}
		if (lobby.isRunning()) {
			logger.warn(
				String.format(
					"%s quiere cambiar los penalties del lobby %s que ya está corriendo!",
					user.getName(),
					lobby.getId()
				)
			);
			messenger.send(
				session,
				new SetPenaltiesErrorResponse(String.format("El lobby %s está corriendo!", user.getLobbyId()))
			);
			return;
		}
		lobby.setPenalties(
			TimePenalties
				.builder()
				.successfulPlayPenalty(Duration.ofMillis(payload.getSuccessfulPlayPenalty()))
				.wrongPlayPenalty(Duration.ofMillis(payload.getWrongPlayPenalty()))
				.discardPenalty(Duration.ofMillis(payload.getDiscardPenalty()))
				.recoverPenalty(Duration.ofMillis(payload.getRecoverPenalty()))
				.build()
		);

		lobby
			.getUsersIds()
			.forEach(userId -> {
				messenger.send(
					userId,
					new LobbyInfoResponse(
						lobby.isOwner(userId),
						nimbleRepository.usersDtoAtLobby(lobby.getId()),
						lobby.getId(),
						lobby.getPenalties()
					)
				);
			});
	}
}
