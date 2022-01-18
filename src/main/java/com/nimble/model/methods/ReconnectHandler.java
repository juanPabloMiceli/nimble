package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.game.UserDto;
import com.nimble.dtos.requests.ReconnectRequest;
import com.nimble.dtos.responses.ReconnectResponse;
import com.nimble.model.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class ReconnectHandler extends MethodHandler {

	private WebSocketSession session;

	private ReconnectRequest payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(PlayHandler.class);

	private Messenger messenger;

	public ReconnectHandler(
		WebSocketSession session,
		ReconnectRequest payload,
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
			logger.error("Un boludo quiere recuperar algo que no es suyo...");
			return;
		}
		User user = nimbleRepository.getUser(payload.getSessionId());
		logger.info(String.format("Bienvenido de nuevo %s", user.getName()));
		user.setSession(session);
		nimbleRepository.putUser(payload.getSessionId(), user);
		messenger.send(user.getId(), new ReconnectResponse(new UserDto(user)));
	}
}
