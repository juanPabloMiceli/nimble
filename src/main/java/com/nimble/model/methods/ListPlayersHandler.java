package com.nimble.model.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.requests.ListPlayersRequest;
import com.nimble.model.Lobby;
import com.nimble.model.User;
import com.nimble.dtos.responses.ListPlayersResponse;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.stream.Collectors;

public class ListPlayersHandler extends MethodHandler {

	private WebSocketSession session;

	private ListPlayersRequest payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(ListPlayersHandler.class);

	private ObjectMapper mapper;

	public ListPlayersHandler(WebSocketSession session, ListPlayersRequest payload, NimbleRepository nimbleRepository,
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
			user.send(mapper.writeValueAsString(new ListPlayersResponse(lobby.getUsersIds().stream()
					.map(userId -> nimbleRepository.getUser(userId).getName()).collect(Collectors.toList()))));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
