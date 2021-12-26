package com.nimble.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.dtos.LobbyDto;
import com.nimble.exceptions.lobby.UserAlreadyInLobbyException;
import com.nimble.exceptions.lobby.UserDoesNotBelongToLobbyException;
import com.nimble.model.game.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lobby {

	private final int DECKS = 3;

	private String id;

	private Game game;

	private List<User> users = new ArrayList<>();

	private ObjectMapper mapper;//TODO: sacar porque ya lo tiene el service

	public Lobby(String id, User user) {
		this.id = id;
		add(user);
		this.mapper = new ObjectMapper();

	}

	public Lobby(Lobby lobby) {
		if (lobby == null) {
			return;
		}

		id = lobby.id;
		game = lobby.game;
		users = lobby.users;
		mapper = lobby.mapper;
	}

	public void add(User user) {
		if (users.contains(user)) {
			throw new UserAlreadyInLobbyException(user.getName(), id);
		}
		users.add(user);
	}

	public void remove(User user) {
		if (!users.contains(user)) {
			throw new UserDoesNotBelongToLobbyException(user.getName(), id);
		}
		users.remove(user);
	}

	public String getId() {
		return id;
	}

	public void start() {
		game = new Game(totalPlayers(), DECKS);
	}

	private int totalPlayers() {
		return users.size();
	}

	public Game getGame() {
		if (game == null) {
			// TODO: Horrible es poco
			return null;
		}
		return new Game(game);
	}

	public List<User> getUsers() {
		if (users == null) {
			// TODO: Horrible es poco
			return null;
		}
		return new ArrayList<>(users);
	}

	public void draw(User user) {
		game.draw(getPlayerNumber(user));
	}

	public Boolean playFromHand(User user, int playTo) {
		return game.playOnHandCard(getPlayerNumber(user), playTo);
	}

	public Boolean playFromDiscard(User user, int playTo) {
		return game.playDiscardCard(getPlayerNumber(user), playTo);
	}

	private int getPlayerNumber(User user) {
		return users.indexOf(user);
	}

}
