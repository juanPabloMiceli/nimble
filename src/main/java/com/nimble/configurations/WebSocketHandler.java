package com.nimble.configurations;

import com.nimble.model.methods.MethodHandlerFactory;
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

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
		MethodHandlerFactory.create(message.getPayload(), session).run();
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		logger.info(String.format("%s connected", session.getRemoteAddress()));
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, @NotNull CloseStatus status) {
		logger.info(String.format("%s left", session.getRemoteAddress()));
	}

}
