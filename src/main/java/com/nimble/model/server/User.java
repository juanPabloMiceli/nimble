package com.nimble.model.server;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class User {

	// TODO: Agregar color?

	private String id;

	private String name;

	private String lobbyId;

	private WebSocketSession session;

	private Color color;

	public User(WebSocketSession session) {
		this.session = session;
		this.name = "";
		this.id = session.getId();
	}

	public User(User user) {
		id = user.id;
		name = user.name;
		lobbyId = user.getLobbyId();
		session = user.session;
	}

	public void send(String payload) throws IOException {
		session.sendMessage(new TextMessage(payload));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public WebSocketSession getSession() {
		return session;
	}

	public void setSession(WebSocketSession session) {
		this.session = session;
	}

	public String getLobbyId() {
		return lobbyId;
	}

	public void setLobbyId(String lobbyId) {
		this.lobbyId = lobbyId;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof User)) return false;
		User user = (User) o;
		return getName().equals(user.getName()) && session.equals(user.session);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName(), session);
	}
}
