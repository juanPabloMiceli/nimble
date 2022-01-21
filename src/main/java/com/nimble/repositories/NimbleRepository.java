package com.nimble.repositories;

import com.nimble.dtos.game.UserDto;
import com.nimble.model.server.Lobby;
import com.nimble.model.server.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NimbleRepository {

	// TODO: users es solo para chequear que se mantenga la session abierta aunque se
	// refresque el navegador,
	// el modelo exacto de storage se va a ver mas adelante.
	private Map<String, User> users = new HashMap<>();

	private Map<String, Lobby> lobbies = new HashMap<>();

	public void putUser(String userId, User user) {
		users.put(userId, user);
	}

	public User getUser(String userId) {
		return users.get(userId);
	}

	public User removeUser(String userId) {
		return users.remove(userId);
	}

	public Boolean containsUserKey(String userId) {
		return users.containsKey(userId);
	}

	public void putLobby(String lobbyId, Lobby lobby) {
		lobbies.put(lobbyId, lobby);
	}

	public Lobby getLobby(String lobbyId) {
		return lobbies.get(lobbyId);
	}

	public Lobby removeLobby(String lobbyId) {
		return lobbies.remove(lobbyId);
	}

	public Boolean containsLobbyKey(String lobbyId) {
		return lobbies.containsKey(lobbyId);
	}

	public List<User> usersAtLobby(String lobbyId) {
		return getLobby(lobbyId).getUsersIds().stream().map(this::getUser).collect(Collectors.toList());
	}

	public List<User> usersAtLobbyOf(String usersId) {
		return lobbyOf(usersId).getUsersIds().stream().map(this::getUser).collect(Collectors.toList());
	}

	public List<String> namesAtLobby(String lobbyId) {
		return usersAtLobby(lobbyId).stream().map(User::getName).collect(Collectors.toList());
	}

	public WebSocketSession getSession(String userId) {
		return getUser(userId).getSession();
	}

	public Lobby lobbyOf(String userId) {
		return getLobby(getUser(userId).getLobbyId());
	}

	public List<WebSocketSession> sessionsAtLobby(String lobbyId) {
		return usersAtLobby(lobbyId).stream().map(User::getSession).collect(Collectors.toList());
	}

	public List<WebSocketSession> sessionsAtUserLobby(String userId) {
		return sessionsAtLobby(lobbyOf(userId).getId());
	}

	public List<UserDto> usersDtoAtLobby(String lobbyId) {
		return usersAtLobby(lobbyId).stream().map(UserDto::new).collect(Collectors.toList());
	}

	public List<UserDto> usersDtoAtLobbyOf(String userId) {
		return usersAtLobbyOf(userId).stream().map(UserDto::new).collect(Collectors.toList());
	}
}
