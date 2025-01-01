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
    }

    public void setKing(King king, int index) {
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
}
