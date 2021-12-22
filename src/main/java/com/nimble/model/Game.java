package com.nimble.model;

import java.util.ArrayList;

public class Game {

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Deck> decksBorad = new ArrayList<>();

    public Game(){
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public void init(){
        for (Player player : players){
            Deck deckBoard = new Deck();
            decksBorad.add(deckBoard);
            player.play(deckBoard);
        }
    }

    public Deck getDeckBoard(int index){
        return  decksBorad.get(index);
    }
}
