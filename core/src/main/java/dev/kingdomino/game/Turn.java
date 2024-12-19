package dev.kingdomino.game;

import java.util.ArrayList;

public class Turn {
    private ArrayList<Player> players;
    private ArrayList<Domino> dominos;
    private int currentPlayerIndex;

    public Turn(ArrayList<Player> players, ArrayList<Domino> dominos) {
        this.players = players;
        this.dominos = dominos;
        this.currentPlayerIndex = 0;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Domino> getDominos() {
        return dominos;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void setNextPlayer() {
        currentPlayerIndex = currentPlayerIndex < players.size() ? (currentPlayerIndex + 1) : null;
    }

    public void setPreviousPlayer() {
        currentPlayerIndex = currentPlayerIndex > 0 ? (currentPlayerIndex - 1) : null;
    }
}
