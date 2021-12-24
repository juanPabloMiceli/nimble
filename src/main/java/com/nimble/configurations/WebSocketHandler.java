package com.nimble.configurations;

import com.nimble.services.NimbleService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		System.out.printf("%s sended:\t%s\n", session.getRemoteAddress(), message.getPayload());
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		System.out.printf("%s connected\n", session.getRemoteAddress());
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, @NotNull CloseStatus status) {

		System.out.printf("%s left\n", session.getRemoteAddress());
	}

}
