package dev.kingdomino.game;

public class Kingdomino {
    private int numberOfPlayers; // 2 - 4 players
    private int numberOfTiles; // number of tiles in the game (4 players = 48, 3 players = 36, 2 players = 24)

    // setup
    public Kingdomino() {
    }

    Deck deck = new Deck(0); // create a new deck of cards
}
