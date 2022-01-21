package com.nimble.model.server;

import com.nimble.exceptions.NoAvailableColorException;
import com.nimble.exceptions.lobby.UserAlreadyInLobbyException;
import com.nimble.exceptions.lobby.UserDoesNotBelongToLobbyException;
import com.nimble.model.enums.LobbyState;
import com.nimble.model.game.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Lobby {

	private final int DECKS = 3;

	private String id;

	private LobbyState lobbyState;

	private Game game;

	private List<String> usersIds = new ArrayList<>();

	private final ColorManager colorManager = new ColorManager();

	public Lobby(String id, String userId) {
		this.id = id;
		this.lobbyState = LobbyState.READY;
		add(userId);
	}

	public Lobby(Lobby lobby) {
		if (lobby == null) {
			return;
		}

		id = lobby.id;
		game = lobby.game;
		usersIds = lobby.usersIds;
		lobbyState = lobby.lobbyState;
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
		if (lobbyState.equals(LobbyState.RUNNING)) {
			game.remove(getPlayerNumber(userId));
		}
		usersIds.remove(userId);
	}

	public String getId() {
		return id;
	}

	public void start() {
		game = new Game(totalPlayers(), DECKS);
		lobbyState = LobbyState.RUNNING;
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

	public void recover(String userId) {
		game.recover(getPlayerNumber(userId));
	}

	public Boolean playFromHand(String userId, int playTo) {
		if (!game.playOnHandCard(getPlayerNumber(userId), playTo)) {
			return false;
		}

		if (game.isOver()) {
			lobbyState = LobbyState.FINISHED;
		}
		if (game.isStuck()) {
			lobbyState = LobbyState.STUCK;
		}
		return true;
	}

	public int getPlayerNumber(String userId) {
		return usersIds.indexOf(userId);
	}

	public String getWinnerId() {
		if (!game.isOver()) {
			return null;
		}
		return usersIds.get(game.winner());
	}

	public Boolean isFinished() {
		return lobbyState.equals(LobbyState.FINISHED);
	}

	public Boolean isStuck() {
		return lobbyState.equals(LobbyState.STUCK);
	}

	public boolean isRunning() {
		return lobbyState.equals(LobbyState.RUNNING);
	}

	public boolean isReady() {
		return lobbyState.equals(LobbyState.READY);
	}

	public String getUser(Integer playerNumber) {
		if (playerNumber >= usersIds.size()) {
			throw new RuntimeException("Querias acceder a un user por indice pero tiraste un indice muy alto!");
		}
		return usersIds.get(playerNumber);
	}

	public boolean isEmpty() {
		return usersIds.isEmpty();
	}

	public boolean isFull() {
		return usersIds.size() >= 4;
	}

	public boolean isOwner(String userId) {
		return usersIds.indexOf(userId) == 0;
	}

	public int players() {
		return usersIds.size();
	}

	public void setLobbyState(LobbyState lobbyState) {
		this.lobbyState = lobbyState;
	}

	public Color getColor() throws NoAvailableColorException {
		return colorManager.getColor();
	}

	public void restoreColor(Color color) {
		colorManager.restoreColor(color);
	}
}
