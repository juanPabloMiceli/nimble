package com.nimble.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.repositories.NimbleRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Component
public class Messenger {

	private ObjectMapper mapper = new ObjectMapper();

	private NimbleRepository nimbleRepository;

	public Messenger(NimbleRepository nimbleRepository) {
		this.nimbleRepository = nimbleRepository;
	}

	public void send(String userId, Object messageObject) {
		send(nimbleRepository.getSession(userId), messageObject);
	}

	public void send(WebSocketSession session, Object messageObject) {
		try {
			session.sendMessage(new TextMessage(mapper.writeValueAsString(messageObject)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void broadcastToLobbyOf(String userId, Object messageObject) {
		nimbleRepository
			.sessionsAtUserLobby(userId)
			.forEach(session -> {
				send(session, messageObject);
			});
	}
}
