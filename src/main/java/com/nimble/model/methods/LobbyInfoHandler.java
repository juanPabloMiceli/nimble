package com.nimble.model.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.game.UserDto;
import com.nimble.dtos.requests.LobbyInfoRequest;
import com.nimble.model.Lobby;
import com.nimble.model.User;
import com.nimble.dtos.responses.LobbyInfoResponse;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.stream.Collectors;

public class LobbyInfoHandler extends MethodHandler {

	private WebSocketSession session;

	private LobbyInfoRequest payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(LobbyInfoHandler.class);

	private ObjectMapper mapper;

	public LobbyInfoHandler(WebSocketSession session, LobbyInfoRequest payload, NimbleRepository nimbleRepository,
			ObjectMapper mapper) {
		this.session = session;
		this.payload = payload;
		this.nimbleRepository = nimbleRepository;
		this.mapper = mapper;
	}

	@Override
	public void run() {
		User user = nimbleRepository.getUser(payload.getSessionId());
		Lobby lobby = nimbleRepository.getLobby(user.getLobbyId());

		logger.info(String.format("Listing players for %s", user.getName()));

		try {
			user.send(mapper.writeValueAsString(new LobbyInfoResponse(nimbleRepository.usersAtLobby(lobby.getId())
					.stream().map(UserDto::new).collect(Collectors.toList()), lobby.getId())));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
