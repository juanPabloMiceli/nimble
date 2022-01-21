package com.nimble.configurations;

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
	public void afterConnectionEstablished(WebSocketSession session) {
		logger.info(String.format("%s established connection ID: %s", session.getRemoteAddress(), session.getId()));
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, @NotNull CloseStatus status) {
		logger.info(String.format("%s left", session.getRemoteAddress()));
	}
}
