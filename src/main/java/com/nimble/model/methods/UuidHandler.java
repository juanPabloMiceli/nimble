package com.nimble.model.methods;

import com.nimble.configurations.Messenger;
import com.nimble.dtos.UuidResponse;
import com.nimble.dtos.requests.UuidRequest;
import com.nimble.model.server.User;
import com.nimble.repositories.NimbleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class UuidHandler extends MethodHandler {

	private WebSocketSession session;

	private UuidRequest payload;

	private NimbleRepository nimbleRepository;

	private final Logger logger = LoggerFactory.getLogger(UuidHandler.class);

	private Messenger messenger;

	public UuidHandler(
		WebSocketSession session,
		UuidRequest payload,
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
		logger.info(String.format("%s connected ID: %s", session.getRemoteAddress(), session.getId()));
		if (nimbleRepository.containsUserKey(session.getId())) {
			throw new RuntimeException("WHAT, LA SESIONES SE MANTIENEN!!");
		}
		nimbleRepository.putUser(session.getId(), new User(session));
		messenger.send(session, new UuidResponse(session.getId()));
	}
}
