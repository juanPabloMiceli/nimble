package com.nimble.model.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.game.UserDto;
import com.nimble.dtos.requests.JoinRequest;
import com.nimble.dtos.responses.status.LobbyNotFoundResponse;
import com.nimble.dtos.responses.status.SuccessfulResponse;
import com.nimble.dtos.responses.status.UnexpectedErrorResponse;
import com.nimble.model.Lobby;
import com.nimble.model.User;
import com.nimble.dtos.responses.LobbyInfoResponse;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.stream.Collectors;

public class JoinHandler extends MethodHandler {

	private WebSocketSession session;

	private JoinRequest payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(JoinHandler.class);

	private ObjectMapper mapper;

	public JoinHandler(WebSocketSession session, JoinRequest payload, NimbleRepository nimbleRepository,
			ObjectMapper mapper) {
		this.session = session;
		this.payload = payload;
		this.nimbleRepository = nimbleRepository;
		this.mapper = mapper;
	}

	@Override
	public void run() {
		// TODO: Chequear que no este iniciado
		if (!nimbleRepository.containsUserKey(payload.getSessionId())) {
			try {
				session.sendMessage(new TextMessage(mapper.writeValueAsString(new UnexpectedErrorResponse())));
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			logger.error("Alguien que no existe se quiere joinear a un lobby!");
		}
		User user = nimbleRepository.getUser(payload.getSessionId());

		if (!nimbleRepository.containsLobbyKey(payload.getLobbyId())) {
			try {
				session.sendMessage(
						new TextMessage(mapper.writeValueAsString(new LobbyNotFoundResponse(payload.getLobbyId()))));
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			logger.info(String.format("%s se quiere unir a un lobby que no existe!", user.getName()));
		}

		Lobby lobby = nimbleRepository.getLobby(payload.getLobbyId());

		if (nimbleRepository.namesAtLobby(payload.getLobbyId()).contains(user.getName())) {
			try {
				session.sendMessage(
						new TextMessage(mapper.writeValueAsString(new LobbyNotFoundResponse(payload.getLobbyId()))));
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			logger.info(String.format("%s se quiere unir a un lobby en el que ya existe su nombre", user.getName()));
		}

		user.setLobbyId(payload.getLobbyId());
		user.setName(payload.getName());
		lobby.add(payload.getSessionId());

		try {
			user.send(mapper.writeValueAsString(new SuccessfulResponse()));
			nimbleRepository.usersAtLobby(lobby.getId()).forEach(_user -> {
				try {// TODO: revisar por que hay 2 try catch.
					_user.send(
							mapper.writeValueAsString(new LobbyInfoResponse(nimbleRepository.usersAtLobby(lobby.getId())
									.stream().map(UserDto::new).collect(Collectors.toList()), lobby.getId())));
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		logger.info(String.format("%s creó el lobby \"%s\"", payload.getName(), payload.getLobbyId()));
	}

}
