package com.nimble.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimble.dtos.protocols.*;
import com.nimble.model.User;
import com.nimble.services.NimbleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

@Controller
public class NimbleController {

	private final NimbleService nimbleService;

	public NimbleController(NimbleService nimbleService) {
		this.nimbleService = nimbleService;
	}

	public void create(WebSocketSession session, CreatePayload payload) {
		nimbleService.create(payload.getLobbyId(), new User(session, payload.getName()));
	}

	public void join(WebSocketSession session, JoinPayload payload) {
		nimbleService.join(payload.getLobbyId(), new User(session, payload.getName()));
	}

	public void quit(WebSocketSession session, QuitPayload payload) {
		nimbleService.quit(payload.getLobbyId(), new User(session, payload.getName()));
	}

	public void start(WebSocketSession session, StartPayload payload) {
		nimbleService.start(payload.getLobbyId(), new User(session, payload.getName()));
	}

	public void draw(WebSocketSession session, DrawPayload payload) {
		nimbleService.draw(payload.getLobbyId(), new User(session, payload.getName()));
	}

	public void play(WebSocketSession session, PlayPayload payload) {
		nimbleService.play(payload.getLobbyId(), new User(session, payload.getName()), payload.getPlayFrom(),
				payload.getPlayTo());
	}

}
