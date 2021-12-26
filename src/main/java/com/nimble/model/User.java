package com.nimble.model;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Objects;

public class User {//TODO: Agregar color?

	private final String name;

	private final WebSocketSession session;

	public User(WebSocketSession session, String name) {
		this.session = session;
		this.name = name;
	}

	public User(User user) {
		name = user.name;
		session = user.session;
	}

	public void send(String payload) throws IOException {
		session.sendMessage(new TextMessage(payload));
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof User))
			return false;
		User user = (User) o;
		return getName().equals(user.getName()) && session.equals(user.session);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName(), session);
	}

}
