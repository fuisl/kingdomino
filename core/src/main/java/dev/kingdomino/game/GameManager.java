package dev.kingdomino.game;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.controllers.Controllers;

import dev.kingdomino.effects.BackgroundManager;

// import dev.kingdomino.game.Event.TriggerType;

public class GameManager {
    private int kingCount = 4;
    private final Deck deck;
    private final Board[] boards;
    private Board currentBoard;
    private Domino currentDomino;
    private Turn currentTurn;
    private final King[] kings;
    private King currentKing;
    private Turn nextTurn;
    private boolean placingDomino = false;
    private boolean choosingDomino = false;
    private Map<King, int[]> scores;
    private boolean finalTurn = false; // set true for debugging

    private final GameTimer gameTimer = GameTimer.getInstance();
    public static GameState currentState = GameState.SETUP;

    private final EventManager eventManager;
    private final BoardInputHandler boardInputHandler;
    private final BoardInputProcessor boardInputProcessor;
    private final BoardInputController boardInputController;

    private final DraftInputHandler draftInputHandler;
    private final DraftInputProcessor draftInputProcessor;
    private final DraftInputController draftInputController;

    public static InputDevice currentInputDevice; // use for displaying accordingly

    public enum GameState {
        INIT,
        SETUP,
        TURN_START, // turn manager state
        TURN_PLACING, // placing domino
        TURN_CHOOSING, // choosing next domino
        TURN_END,
        GAME_OVER,
        RESULTS
    }

    public enum InputDevice {
        KEYBOARD,
        CONTROLLER
    }

    public static void setInputDevice(InputDevice device) {
        switch (device) {
            case KEYBOARD:
                currentInputDevice = InputDevice.KEYBOARD;
                break;
            case CONTROLLER:
                currentInputDevice = InputDevice.CONTROLLER;
                break;
            default:
                break;
        }
    }

    public GameManager() {
        // initialize game manager
        eventManager = EventManager.getInstance();
        boardInputHandler = new BoardInputHandler(this);
        boardInputProcessor = new BoardInputProcessor(boardInputHandler);
        boardInputController = new BoardInputController(boardInputHandler);

        draftInputHandler = new DraftInputHandler(this);
        draftInputProcessor = new DraftInputProcessor(draftInputHandler);
        draftInputController = new DraftInputController(draftInputHandler);

        Controllers.addListener(boardInputController);
        Controllers.addListener(draftInputController);
        Gdx.input.setInputProcessor(new InputMultiplexer(boardInputProcessor, draftInputProcessor));

        // initialize game components
        deck = new Deck(0);
        kings = new King[kingCount];
        boards = new Board[kingCount];
        currentTurn = null;
        nextTurn = null;

        for (int i = 0; i < kingCount; i++) {
            boards[i] = new Board();
            kings[i] = new King(i, boards[i]);
        }

        // initialize game state
        currentState = GameState.INIT;
    }

    public void update(float dt) {
        // Core game loop -- must update every frame
        gameTimer.update(dt);
        eventManager.update(dt, true);

        // if (boardInputProcessor.exit) {
        // Gdx.app.exit();
        // }

        switch (currentState) {
            case INIT:
                init();
                break;
            case SETUP:
                setup();
                break;
            case TURN_START:
                // Event autoContinue = new Event(
                // TriggerType.AFTER, false, true, 2.0f, () -> {
                // updateTurn();
                // draftInputProcessor.show = false;
                // }, null, null, null);

                // eventManager.addEvent(autoContinue.copy(), "base", false);

                // if (draftInputHandler.show == false) {
                // draftInputHandler.show = true;
                    updateTurn();
                // }
                break;
            case TURN_PLACING:
                placeDomino();
                break;
            case TURN_CHOOSING:
                chooseDomino();
                break;
            case TURN_END:
                turnEnd();
                break;
            case GAME_OVER:
                game_over();
                break;
            case RESULTS:
                results();
                break;
            default:
                break;
        }
    }

    // ---------------------GAME STATES---------------------

    private void init() {

        // // test end game
        // for (int i = 0; i < 44; i++) {
        // deck.drawCard();
        // }

        // game init for 3 kings (removing 12 dominos)
        if (kingCount == 3) {
            for (int i = 0; i < 12; i++)
                deck.drawCard();
        }

        Domino[] draft_current = new Domino[kingCount];

        // draw dominos for each king
        for (int i = 0; i < kingCount; i++) {
            draft_current[i] = deck.drawCard();
        }
        currentTurn = new Turn(draft_current);

        // setup the kings for first turn. TODO: add random later.
        for (int i = 0; i < kingCount; i++) {
            currentTurn.selectDomino(kings[i], i);
        }

        // set game state to SETUP
        currentState = GameState.SETUP;
    }

    private void setup() {
        Domino[] draft_next = new Domino[kingCount];
        // setup next turn
        if (!deck.isEmpty()) {
            for (int i = 0; i < kingCount; i++) {
                draft_next[i] = deck.drawCard();
            }
            nextTurn = new Turn(draft_next);
        } else {
            finalTurn = true;
        }

        // set game state to TURN
        currentState = GameState.TURN_START;
    }

    private void updateTurn() {
        if (!placingDomino && !choosingDomino) {
            currentDomino = currentTurn.getCurrentDomino();
            currentKing = currentTurn.getCurrentKing();
            currentBoard = currentKing.getBoard();
            currentState = GameState.TURN_PLACING;

            draftInputHandler.reset();
            boardInputHandler.reset();

            // start spinning background
            BackgroundManager.startSpin();
        }
    }

    private void placeDomino() {
        placingDomino = true;
        if (boardInputHandler.exit && boardInputHandler.valid) {
            currentBoard.getScoringSystem().calculateLandScore();
            // update score var after placing domino
            results();
            if (finalTurn) {
                currentState = GameState.TURN_END;
            } else {
                currentState = GameState.TURN_CHOOSING;
            }
            placingDomino = false;
        }
    }

    private void chooseDomino() {
        // if (nextTurn == null) {
        // currentState = GameState.TURN_END;
        // }

        choosingDomino = true;
        if (draftInputHandler.exit) {
            currentState = GameState.TURN_END;
            choosingDomino = false;
        }
    }

    private void turnEnd() {
        currentTurn.next();
        currentState = GameState.TURN_START;

        if (currentTurn.isOver()) {
            if (nextTurn == null) {
                currentState = GameState.GAME_OVER;
            } else {
                currentTurn = nextTurn.copy(); // weird
                nextTurn = null;
                currentState = GameState.SETUP;

                // rewind and forward spinning background
                BackgroundManager.rewindSpin();
            }
        }

    }

    private void game_over() {
        // recalculate scores for every kings
        for (King k : kings) {
            k.getBoard().getScoringSystem().calculateScore();
        }

        // TODO: clean up

        currentState = GameState.RESULTS;
    }

    private void results() {
        scores = new LinkedHashMap<King, int[]>();

        // get scores for render
        for (King k : kings) {
            int landScore = k.getBoard().getScoringSystem().getLandTotal();
            int bonusScore = k.getBoard().getScoringSystem().getLandBonus();
            int totalScore = k.getBoard().getScoringSystem().getBoardTotal();
            scores.put(k, new int[] { landScore, bonusScore, totalScore });
        }

        scores = sortScores(scores, 2); // sort by total
    }

    private Map<King, int[]> sortScores(Map<King, int[]> scores, int sortIndex) {
        LinkedHashMap<King, int[]> sortedScores = scores.entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue()[sortIndex], entry1.getValue()[sortIndex]))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return sortedScores;
    }

    public void dispose() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'dispose'");
    }

    public Board getBoard() {
        return this.currentBoard;
    }

    public Domino getCurrentDomino() {
        return this.currentDomino;
    }

    public void setCurrentDomino(Domino domino) {
        this.currentDomino = domino;
    }

    public King getCurrentKing() {
        return this.currentKing;
    }

    public void setCurrentKing(King king) {
        this.currentKing = king;
    }

    public void selectDomino(int index) {
        if (currentState == GameState.TURN_CHOOSING) {
            nextTurn.selectDomino(currentKing, index);
        }
    }

    public King[] getAllKing() {
        return this.kings;
    }

    public int getKingCount() {
        return this.kingCount;
    }

    public Turn getCurrentTurn() {
        return this.currentTurn;
    }

    public Turn getNextTurn() {
        return this.nextTurn;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public DraftInputHandler getDraftInputHandler() {
        return this.draftInputHandler;
    }

    public Map<King, int[]> getScores() {
        return scores;
    }

    public BoardInputHandler getBoardInputHandler() {
        return this.boardInputHandler;
    }
}
