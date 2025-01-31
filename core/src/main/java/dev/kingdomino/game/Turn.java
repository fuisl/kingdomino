package dev.kingdomino.game;

import java.util.Arrays;

/**
 * Represents a turn in the game.
 * 
 * @see Domino
 * @see King
 * @see Turn
 * 
 * @author @fuisl
 * @version 1.0
 * 
 */
public class Turn {
    static int turnId = 0;
    private final King[] kings;
    private final Domino[] draft;
    private boolean[] selected;

    private int countRemaining;
    private int currentIndex;

    /**
     * Constructs a new Turn with the given draft of dominos.
     *
     * @param draft the array of dominos for this turn
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Turn(Domino[] draft) {
        this.draft = draft;
        this.currentIndex = 0;
        this.kings = new King[draft.length];

        this.selected = new boolean[draft.length];
        Arrays.fill(this.selected, false);

        this.countRemaining = selected.length;

        this.sortDraft();
        turnId++;
    }

    /**
     * Selects a domino for the given king at the specified index.
     *
     * @param king  the king selecting the domino
     * @param index the index of the domino to select
     */
    public void selectDomino(King king, int index) {
        selected[index] = true;
        kings[index] = king;
    }

    /**
     * Advances to the next domino in the draft.
     */
    public void next() {
        currentIndex++;
    }

    /**
     * Gets the current domino in the draft.
     *
     * @return the current domino
     */
    public Domino getCurrentDomino() {
        return draft[currentIndex];
    }

    /**
     * Gets the current king selecting a domino.
     *
     * @return the current king
     */
    public King getCurrentKing() {
        return kings[currentIndex];
    }

    /**
     * Checks if the turn is over.
     *
     * @return true if the turn is over, false otherwise
     */
    public boolean isOver() {
        return currentIndex == draft.length;
    }

    /**
     * Sorts the draft of dominos by their IDs.
     */
    public void sortDraft() {
        Arrays.sort(draft, (Domino d1, Domino d2) -> d1.getId() - d2.getId());
    }

    /**
     * Gets the domino at the specified index.
     *
     * @param index the index of the domino
     * @return the domino at the specified index
     */
    public Domino getDomino(int index) {
        return draft[index];
    }

    /**
     * Gets the draft of dominos.
     *
     * @return the draft of dominos
     */
    public Domino[] getDraft() {
        return draft;
    }

    /**
     * Gets the remaining dominos in the draft.
     *
     * @return an array of the remaining dominos
     * @deprecated Use other methods to get the state of the draft.
     */
    @Deprecated
    public Domino[] getRemainingDraft() {
        Domino[] remainingDraft = new Domino[countRemaining];
        for (int i = 0, j = 0; i < draft.length; i++) {
            if (!selected[i]) {
                remainingDraft[j++] = draft[i];
            }
        }

        return remainingDraft;
    }

    /**
     * Gets the current index in the draft.
     *
     * @return the current index
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * Gets the ID of the current turn.
     *
     * @return the turn ID
     */
    public int getTurnId() {
        return turnId;
    }

    /**
     * Gets the array of kings.
     *
     * @return the array of kings
     */
    public King[] getKings() {
        return kings;
    }

    /**
     * Constructs a copy of the given turn.
     *
     * @param turn the turn to copy
     */
    public Turn(Turn turn) {
        this.draft = turn.draft;
        this.currentIndex = turn.currentIndex;
        this.kings = turn.kings;
    }

    /**
     * Creates a copy of this turn.
     *
     * @return a new Turn object that is a copy of this turn
     */
    public Turn copy() {
        return new Turn(this);
    }

    /**
     * Checks if the domino at the specified index has been selected.
     *
     * @param index the index to check
     * @return true if the domino has been selected, false otherwise
     */
    public boolean isSelected(int index) {
        return selected[index];
    }

    /**
     * Gets the count of remaining dominos to be selected.
     *
     * @return the count of remaining dominos
     */
    public int getCountRemaining() {
        return countRemaining;
    }
}
