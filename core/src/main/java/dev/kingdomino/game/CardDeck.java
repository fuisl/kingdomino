package dev.kingdomino.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List; // TODO: use tools from LibGDX instead of Java Collections

public class CardDeck {
    // Common properties and methods for all card decks
    protected List<Domino> deck;
    protected int seed;

    public CardDeck() {
        this.deck = new ArrayList<>();
        initializeDeck();
    }
    
    public CardDeck(int seed) {
        this.seed = seed;
        this.deck = new ArrayList<>();
        initializeDeck();
    }

    protected void initializeDeck() {
        for (int i = 0; i < 48; i++) {
            // TODO: init the deck with the appropriate cards
        }
    }; // Initialize the deck with the appropriate cards

    public void shuffle() {
        Collections.shuffle(deck, new java.util.Random(seed));
    }

    public Domino drawCard() {
        if (!deck.isEmpty()) {
            return deck.remove(deck.size() - 1);
        }
        return null;
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }
}
