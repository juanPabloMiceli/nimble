package com.nimble.model.server;

import com.nimble.exceptions.NoAvailableColorException;
import com.nimble.exceptions.PlayedWhenPenalizedException;
import com.nimble.exceptions.game.InvalidPlayerNumberException;
import com.nimble.exceptions.lobby.UserAlreadyInLobbyException;
import com.nimble.exceptions.lobby.UserDoesNotBelongToLobbyException;
import com.nimble.model.enums.LobbyState;
import com.nimble.model.game.Game;

import java.awt.*;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

public class Lobby {

	private final int DECKS = 3;

	private String id;

	private LobbyState lobbyState;

	private Game game;

	private List<String> usersIds = new ArrayList<>();

	private final ColorManager colorManager = new ColorManager();

	private TimeReferee timeReferee;

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
		timeReferee = lobby.timeReferee;
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
		timeReferee = new TimeReferee(Clock.systemUTC(), this.usersIds);
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

	public void discard(String userId) throws PlayedWhenPenalizedException {
		if (!timeReferee.isAvailable(userId)) {
			throw new PlayedWhenPenalizedException(userId);
		}
		game.discard(getPlayerNumber(userId));
		timeReferee.discardPenalize(userId);
	}

	public void recover(String userId) throws PlayedWhenPenalizedException {
		if (!timeReferee.isAvailable(userId)) {
			throw new PlayedWhenPenalizedException(userId);
		}
		game.recover(getPlayerNumber(userId));
		timeReferee.recoverPenalize(userId);
	}

	public Boolean playFromHand(String userId, int playTo) throws PlayedWhenPenalizedException {
		if (!timeReferee.isAvailable(userId)) {
			throw new PlayedWhenPenalizedException(userId);
		}
		if (!game.playOnHandCard(getPlayerNumber(userId), playTo)) {
			timeReferee.wrongPlayPenalize(userId);
			return false;
		}
		timeReferee.successfulPlayPenalize(userId);
		if (game.isOver()) {
			lobbyState = LobbyState.FINISHED;
			return true;
		}
		if (game.isStuck()) {
			lobbyState = LobbyState.STUCK;
			return true;
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

	public String getUserId(Integer playerNumber) throws InvalidPlayerNumberException {
		if (playerNumber >= usersIds.size()) {
			throw new InvalidPlayerNumberException(playerNumber);
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

	public void setPenalties(TimePenalties penalties) {
		timeReferee.setPenalties(penalties);
	}

	public TimePenalties getPenalties() {
		if (timeReferee == null) {
			return null;
		}
		return timeReferee.getPenalties();
	}
}
