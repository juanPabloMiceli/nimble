package com.nimble.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.SessionDto;
import com.nimble.model.User;
import com.nimble.model.methods.MethodHandlerFactory;
import com.nimble.repositories.NimbleRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

	private final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

	private final NimbleRepository nimbleRepository;

	private final ObjectMapper mapper = new ObjectMapper();

	private final Messenger messenger;

	public WebSocketHandler(NimbleRepository nimbleRepository, Messenger messenger) {
		this.nimbleRepository = nimbleRepository;
		this.messenger = messenger;
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
		MethodHandlerFactory.create(nimbleRepository, message.getPayload(), session, messenger).run();
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws IOException {
		logger.info(String.format("%s connected ID: %s", session.getRemoteAddress(), session.getId()));
		if (nimbleRepository.containsUserKey(session.getId())) {
			throw new RuntimeException("WHAT, LA SESIONES SE MANTIENEN!!");
		}
		nimbleRepository.putUser(session.getId(), new User(session));
		session.sendMessage(new TextMessage(mapper.writeValueAsString(new SessionDto(session.getId()))));
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, @NotNull CloseStatus status) {
		logger.info(String.format("%s left", session.getRemoteAddress()));
	}
}
