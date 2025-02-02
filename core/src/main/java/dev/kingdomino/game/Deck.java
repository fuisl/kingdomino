package dev.kingdomino.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List; // TODO: use tools from LibGDX instead of Java Collections
import java.util.Random;

/**
 * Represents a deck of cards (dominos) in the game. This class should be using
 * to get dominos for the game.
 */
public class Deck {
    // Common properties and methods for all card decks
    protected List<Domino> deck;
    protected int seed;
    protected Random random;

    /**
     * Constructs a CardDeck with a default seed.
     */
    public Deck() {
        this.random = new Random();
        this.deck = new ArrayList<>();
        this.initializeDeck();
    }

    /**
     * Constructs a CardDeck with the specified seed.
     *
     * @param seed the seed for shuffling the deck
     */
    public Deck(int seed) {
        this.seed = seed;
        this.random = new Random(seed);
        this.deck = new ArrayList<>();
        this.initializeDeck();
    }

    /**
     * Initializes the deck with all dominos.
     */
    protected void initializeDeck() {
        deck.clear();
        deck = DominoDeck.getAllDominos();
        this.shuffle();
    };

    /**
     * Shuffles the deck using the specified seed.
     */
    public void shuffle() {
        Collections.shuffle(deck, random);
    }

    /**
     * Draws a card (domino) from the deck.
     *
     * @return the drawn domino, or null if the deck is empty
     */
    public Domino drawCard() {
        if (!deck.isEmpty()) {
            return deck.remove(deck.size() - 1);
        }
        return null;
    }

    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return deck.isEmpty();
    }
}
