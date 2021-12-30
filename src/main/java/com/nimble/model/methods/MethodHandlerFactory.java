package com.nimble.model.methods;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.protocols.*;
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
			return new CreateHandler(session, mapper.readValue(payload, CreatePayload.class), nimbleRepository, mapper);
		case "join":
			return new JoinHandler(session, mapper.readValue(payload, JoinPayload.class), nimbleRepository, mapper);
		case "quit":
			return new QuitHandler(session, mapper.readValue(payload, QuitPayload.class), nimbleRepository, mapper);
		case "start":
			return new StartHandler(session, mapper.readValue(payload, StartPayload.class), nimbleRepository, mapper);
		case "discard":
			return new DiscardHandler(session, mapper.readValue(payload, DiscardPayload.class), nimbleRepository,
					mapper);
		case "play":
			return new PlayHandler(session, mapper.readValue(payload, PlayPayload.class), nimbleRepository, mapper);
		case "reconnect":
			return new ReconnectHandler(session, mapper.readValue(payload, ReconnectPayload.class), nimbleRepository,
					mapper);
		default:
			throw new InvalidMethodException(method);
		}
	}

}
