package com.nimble.model.methods;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.requests.*;
import com.nimble.exceptions.websocketHandler.InvalidMethodException;
import com.nimble.repositories.NimbleRepository;
import org.json.JSONObject;
import org.springframework.web.socket.WebSocketSession;

public class MethodHandlerFactory {

	private static final ObjectMapper mapper = new ObjectMapper();

	public static MethodHandler create(NimbleRepository nimbleRepository, String payload, WebSocketSession session)
			throws JsonProcessingException {
		JSONObject jsonObject = new JSONObject(payload);
		String method = (String) jsonObject.get("method");

		switch (method) {
		case "create":
			return new CreateHandler(session, mapper.readValue(payload, CreateRequest.class), nimbleRepository, mapper);
		case "join":
			return new JoinHandler(session, mapper.readValue(payload, JoinRequest.class), nimbleRepository, mapper);
		case "quit":
			return new QuitHandler(session, mapper.readValue(payload, QuitRequest.class), nimbleRepository, mapper);
		case "start":
			return new StartHandler(session, mapper.readValue(payload, StartRequest.class), nimbleRepository, mapper);
		case "discard":
			return new DiscardHandler(session, mapper.readValue(payload, DiscardRequest.class), nimbleRepository,
					mapper);
		case "play":
			return new PlayHandler(session, mapper.readValue(payload, PlayRequest.class), nimbleRepository, mapper);
		case "reconnect":
			return new ReconnectHandler(session, mapper.readValue(payload, ReconnectRequest.class), nimbleRepository,
					mapper);
		case "list_players":
			return new ListPlayersHandler(session, mapper.readValue(payload, ListPlayersRequest.class),
					nimbleRepository, mapper);
		default:
			throw new InvalidMethodException(method);
		}
	}

}
