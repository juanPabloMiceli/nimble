package com.nimble.model.methods;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.configurations.Messenger;
import com.nimble.dtos.requests.*;
import com.nimble.exceptions.websocketHandler.InvalidMethodException;
import com.nimble.repositories.NimbleRepository;
import org.json.JSONObject;
import org.springframework.web.socket.WebSocketSession;

public class MethodHandlerFactory {

	private static final ObjectMapper mapper = new ObjectMapper();

	public static MethodHandler create(
		NimbleRepository nimbleRepository,
		String payload,
		WebSocketSession session,
		Messenger messenger
	) throws JsonProcessingException {
		JSONObject jsonObject = new JSONObject(payload);
		String method = (String) jsonObject.get("method");

		switch (method) {
			case "create":
				return new CreateHandler(session, mapper.readValue(payload, CreateRequest.class), nimbleRepository, messenger);
			case "join":
				return new JoinHandler(session, mapper.readValue(payload, JoinRequest.class), nimbleRepository, messenger);
			case "quit":
				return new QuitHandler(session, mapper.readValue(payload, QuitRequest.class), nimbleRepository, messenger);
			case "start":
				return new StartHandler(session, mapper.readValue(payload, StartRequest.class), nimbleRepository, messenger);
			case "discard":
				return new DiscardHandler(
					session,
					mapper.readValue(payload, DiscardRequest.class),
					nimbleRepository,
					messenger
				);
			case "play":
				return new PlayHandler(session, mapper.readValue(payload, PlayRequest.class), nimbleRepository, messenger);
			case "reconnect":
				return new ReconnectHandler(
					session,
					mapper.readValue(payload, ReconnectRequest.class),
					nimbleRepository,
					messenger
				);
			case "lobby_info":
				return new LobbyInfoHandler(
					session,
					mapper.readValue(payload, LobbyInfoRequest.class),
					nimbleRepository,
					messenger
				);
			case "game_state":
				return new GameStateHandler(
					session,
					mapper.readValue(payload, GameStateRequest.class),
					nimbleRepository,
					messenger
				);
			case "recover":
				return new RecoverHandler(
					session,
					mapper.readValue(payload, RecoverRequest.class),
					nimbleRepository,
					messenger
				);
			case "uuid":
				return new UuidHandler(session, mapper.readValue(payload, UuidRequest.class), nimbleRepository, messenger);
			case "set_penalties":
				return new SetPenaltiesHandler(
					session,
					mapper.readValue(payload, SetPenaltiesRequest.class),
					nimbleRepository,
					messenger
				);
			case "kick":
				return new KickHandler(session, mapper.readValue(payload, KickRequest.class), nimbleRepository, messenger);
			default:
				throw new InvalidMethodException(method);
		}
	}
}
