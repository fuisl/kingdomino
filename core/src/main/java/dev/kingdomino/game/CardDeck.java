package dev.kingdomino.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;  // TODO: use tools from LibGDX instead of Java Collections

public abstract class CardDeck {
    // Common properties and methods for all card decks
    protected List<Domino> deck;

    public CardDeck() {
        this.deck = new ArrayList<>();
        initializeDeck();
    }

    protected abstract void initializeDeck();  // Initialize the deck with the appropriate cards

    public void shuffle() {
        Collections.shuffle(deck);
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
