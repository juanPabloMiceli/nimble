package com.nimble.model;

import com.nimble.exceptions.game.InvalidDeckNumberException;
import com.nimble.exceptions.game.InvalidPlayerNumberException;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Deck> decksBoard = new ArrayList<>();

    public Game(int nPlayers, int nDecks){
        for(int i = 0; i < nPlayers; i++){
            players.add(new Player());
        }
        for(int i = 0; i < nDecks; i++){
            decksBoard.add(new Deck(List.of(Card.random())));
        }
    }

    public void draw(int playerNumber){
        if(playerNumber >= players.size()){
            throw new InvalidPlayerNumberException(playerNumber);
        }
        players.get(playerNumber).draw();
    }

    public Boolean playOnHandCard(int playerNumber, int deckNumber){
        if(playerNumber >= players.size()){
            throw new InvalidPlayerNumberException(playerNumber);
        }
        if(deckNumber >= decksBoard.size()){
            throw new InvalidDeckNumberException(deckNumber);
        }
        return players.get(playerNumber).playHandCard(decksBoard.get(deckNumber));
    }

    public Boolean playDiscardCard(int playerNumber, int deckNumber){
        if(playerNumber >= players.size()){
            throw new InvalidPlayerNumberException(playerNumber);
        }
        if(deckNumber >= decksBoard.size()){
            throw new InvalidDeckNumberException(deckNumber);
        }
        return players.get(playerNumber).playDiscardCard(decksBoard.get(deckNumber));
    }

    public Card getTopCard(int index){
        return  decksBoard.get(index).peek();
    }

    public Boolean isOver(){
        return players.stream().anyMatch(Player::hasEnded);
    }

    public int winner(){
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).hasEnded()){
                return i;
            }
        }
        return -1;
    }
}
