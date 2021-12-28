package com.nimble.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.controllers.NimbleController;
import com.nimble.dtos.protocols.*;
import com.nimble.exceptions.websocketHandler.InvalidMethodException;
import com.nimble.services.NimbleService;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
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

	private final NimbleController nimbleController;

	private final ObjectMapper mapper;

	private final Logger logger = LoggerFactory.getLogger(NimbleService.class);


	public WebSocketHandler(NimbleController nimbleController) {
		this.nimbleController = nimbleController;
		this.mapper = new ObjectMapper();
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {

		String payload = message.getPayload();

		JSONObject jsonObject = new JSONObject(payload);
		String method = (String) jsonObject.get("method");

		logger.info(String.format("%s sended method: %s", session.getRemoteAddress(), method));
		switch (method) {//TODO: Puede mejorar con un factory
		case "create":
			nimbleController.create(session, mapper.readValue(payload, CreatePayload.class));
			break;
		case "join":
			nimbleController.join(session, mapper.readValue(payload, JoinPayload.class));
			break;
		case "quit":
			nimbleController.quit(session, mapper.readValue(payload, QuitPayload.class));
			break;
		case "start":
			nimbleController.start(session, mapper.readValue(payload, StartPayload.class));
			break;
		case "draw":
			nimbleController.draw(session, mapper.readValue(payload, DrawPayload.class));
			break;
		case "play":
			nimbleController.play(session, mapper.readValue(payload, PlayPayload.class));
			break;
		default:
			throw new InvalidMethodException(method);
		}
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
