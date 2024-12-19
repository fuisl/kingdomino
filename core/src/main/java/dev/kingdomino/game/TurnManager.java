package dev.kingdomino.game;

public class TurnManager {
    private Turn[] turns;
    private int currentTurn;

    public TurnManager(Turn[] turns) {
        this.turns = turns;
        this.currentTurn = 0;
    }

    public Turn getCurrentTurn() {
        return turns[currentTurn];
    }

    public Turn getNextTurn() {
        if (hasNextTurn()) {
            return turns[currentTurn + 1];
        }
        return null;
    }

    public void nextTurn() {
        currentTurn++;
    }

    public boolean hasNextTurn() {
        return currentTurn < turns.length;
    }
}
