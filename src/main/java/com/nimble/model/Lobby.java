package com.nimble.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.exceptions.lobby.UserAlreadyInLobbyException;
import com.nimble.exceptions.lobby.UserDoesNotBelongToLobbyException;
import com.nimble.model.game.Game;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

	private final int DECKS = 3;

	private String id;

	private Game game;

	private List<String> usersIds = new ArrayList<>();

	private ObjectMapper mapper;// TODO: sacar porque ya lo tiene el service

	public Lobby(String id, String userId) {
		this.id = id;
		add(userId);
		this.mapper = new ObjectMapper();

	}

	public Lobby(Lobby lobby) {
		if (lobby == null) {
			return;
		}

		id = lobby.id;
		game = lobby.game;
		usersIds = lobby.usersIds;
		mapper = lobby.mapper;
	}

	public void add(String userId) {
		if (usersIds.contains(userId)) {
			throw new UserAlreadyInLobbyException(userId, id);
		}
		usersIds.add(userId);
	}

	public void remove(String userId) {
		if (!usersIds.contains(userId)) {
			throw new UserDoesNotBelongToLobbyException(userId, id);
		}
		usersIds.remove(userId);
	}

	public String getId() {
		return id;
	}

	public void start() {
		game = new Game(totalPlayers(), DECKS);
	}

	private int totalPlayers() {
		return usersIds.size();
	}

	public Game getGame() {
		if (game == null) {
			// TODO: Horrible es poco
			return null;
		}
		return new Game(game);
	}

	public List<String> getUsersIds() {
		if (usersIds == null) {
			// TODO: Horrible es poco
			return null;
		}
		return new ArrayList<>(usersIds);
	}

	public void discard(String userId) {
		game.discard(getPlayerNumber(userId));
	}

	public Boolean playFromHand(String userId, int playTo) {
		return game.playOnHandCard(getPlayerNumber(userId), playTo);
	}

	// TODO: playTo renombrar por algo mas declarativo
	public Boolean playFromDiscard(String userId, int playTo) {
		return game.playDiscardCard(getPlayerNumber(userId), playTo);
	}

	private int getPlayerNumber(String userId) {
		return usersIds.indexOf(userId);
	}

}
