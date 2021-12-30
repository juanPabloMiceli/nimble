package com.nimble.repositories;

import com.nimble.model.Lobby;
import com.nimble.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

	public void putUser(String id, User user) {
		users.put(id, user);
	}

	public User getUser(String id) {
		return users.get(id);
	}

	public User removeUser(String id) {
		return users.remove(id);
	}

	public Boolean containsUserKey(String id) {
		return users.containsKey(id);
	}

	public void putLobby(String id, Lobby user) {
		lobbies.put(id, user);
	}

	public Lobby getLobby(String id) {
		return lobbies.get(id);
	}

	public Lobby removeLobby(String id) {
		return lobbies.remove(id);
	}

	public Boolean containsLobbyKey(String id) {
		return lobbies.containsKey(id);
	}

}
