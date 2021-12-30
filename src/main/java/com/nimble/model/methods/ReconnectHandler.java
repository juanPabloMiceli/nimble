package com.nimble.model.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.game.UserDto;
import com.nimble.dtos.protocols.ReconnectPayload;
import com.nimble.model.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class ReconnectHandler extends MethodHandler {

	private WebSocketSession session;

	private ReconnectPayload payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(PlayHandler.class);

	private ObjectMapper mapper;

	public ReconnectHandler(WebSocketSession session, ReconnectPayload payload, NimbleRepository nimbleRepository,
			ObjectMapper mapper) {
		this.session = session;
		this.payload = payload;
		this.nimbleRepository = nimbleRepository;
		this.mapper = mapper;
	}

	@Override
	public void run() {
		if (!nimbleRepository.containsUserKey(payload.getId())) {
			logger.error("Un boludo quiere recuperar algo que no es suyo...");
			return;
		}
		User user = nimbleRepository.getUser(payload.getId());
		logger.info(String.format("Bienvenido de nuevo %s", user.getName()));
		user.setSession(session);
		nimbleRepository.putUser(payload.getId(), user);
		try {
			user.send(mapper.writeValueAsString(new ReturnReconnectDtoXX("reconnected", new UserDto(user))));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}