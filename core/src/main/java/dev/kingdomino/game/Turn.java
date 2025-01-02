package dev.kingdomino.game;

import java.util.Arrays;
import java.util.Comparator;

public class Turn {
    static int turnId = 0;
    private King[] kings;
    private Domino[] draft;

    private int currentIndex;

    public Turn(Domino[] draft) {
        this.draft = draft;
        this.currentIndex = 0;
        this.kings = new King[draft.length];
        this.sortDraft();
        turnId++;
    }

    public void selectDomino(King king, int index) {
        kings[index] = king;
    }

    public void next() {
        currentIndex++;
    }

    public Domino getCurrentDomino() {
        return draft[currentIndex];
    }

    public King getCurrentKing() {
        return kings[currentIndex];
    }

    public boolean isOver() {
        return currentIndex == draft.length;
    }

    public void sortDraft() {
        Arrays.sort(draft, new Comparator<Domino>() {
            @Override
            public int compare(Domino d1, Domino d2) {
                return d1.getId() - d2.getId();
            }
        });
    }

    public Domino getDomino(int index) {
        return draft[index];
    }

    public Domino[] getDraft() {
        return draft;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getTurnId() {
        return turnId;
    }

    public King[] getKings() {
        return kings;
    }

    public Turn(Turn turn) {
        this.draft = turn.draft;
        this.currentIndex = turn.currentIndex;
        this.kings = turn.kings;
    }

    public Turn copy() {
        return new Turn(this);
    }
}
