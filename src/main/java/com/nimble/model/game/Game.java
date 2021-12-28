package com.nimble.model.game;

import com.nimble.exceptions.game.InvalidDeckNumberException;
import com.nimble.exceptions.game.InvalidPlayerNumberException;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Game {

	private ArrayList<Player> players;

	private ArrayList<Deck> decksBoard;

	public Game(int nPlayers, int nDecks) {
		players = new ArrayList<>();
		decksBoard = new ArrayList<>();
		for (int i = 0; i < nPlayers; i++) {
			players.add(new Player());
		}
		for (int i = 0; i < nDecks; i++) {
			decksBoard.add(new Deck(List.of(Card.random())));
		}
	}

	public Game(Game game) {
		// TODO: investigar factory pattern
		if (game == null) {
			return;
		}

		players = game.players;
		decksBoard = game.decksBoard;
	}

	public void discard(int playerNumber) {
		if (playerNumber >= players.size()) {
			throw new InvalidPlayerNumberException(playerNumber);
		}
		players.get(playerNumber).discard();
	}

	public Boolean playOnHandCard(int playerNumber, int deckNumber) {
		if (playerNumber >= players.size()) {
			throw new InvalidPlayerNumberException(playerNumber);
		}
		if (deckNumber >= decksBoard.size()) {
			throw new InvalidDeckNumberException(deckNumber);
		}
		return players.get(playerNumber).playHandCard(decksBoard.get(deckNumber));
	}

	public Boolean playDiscardCard(int playerNumber, int deckNumber) {
		if (playerNumber >= players.size()) {
			throw new InvalidPlayerNumberException(playerNumber);
		}
		if (deckNumber >= decksBoard.size()) {
			throw new InvalidDeckNumberException(deckNumber);
		}
		return players.get(playerNumber).playDiscardCard(decksBoard.get(deckNumber));
	}

	public Card getDeckBoardTopCard(int index) {
		return decksBoard.get(index).peek();
	}

	public Boolean isOver() {
		return players.stream().anyMatch(Player::hasEnded);
	}

	public int winner() {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).hasEnded()) {
				return i;
			}
		}
		return -1;
	}

	public Card getHandCard(int index) {
		return new Card(players.get(index).getHandCard());
	}

	public Card getDiscardTop(int index) {
		return new Card(players.get(index).peekDiscardDeck());
	}

	public int getOnHandsDeckSize(int index) {
		return players.get(index).getOnHandsDeckSize();
	}

	public int getDiscardDeckSize(int index) {
		return players.get(index).getDiscardDeckSize();
	}

	public int getTotalCards(int index) {
		return players.get(index).totalCards();
	}

	public int totalPlayers() {
		return players.size();
	}

	public int totalDecks() {
		return decksBoard.size();
	}

}
