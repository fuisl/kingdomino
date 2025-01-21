package dev.kingdomino.game;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// import dev.kingdomino.game.Event.TriggerType;

public class GameManager {
    private int kingCount = 4;
    private Deck deck;
    private Board[] boards;
    private Board currentBoard;
    private Domino currentDomino;
    private Turn currentTurn;
    private King[] kings;
    private King currentKing;
    private Turn nextTurn;
    private boolean placingDomino = false;
    private boolean choosingDomino = false;
    private Map<King, int[]> scores;
    private boolean finalTurn = false; // set true for debugging
    private boolean resultsRendered = false;

    private GameTimer gameTimer = GameTimer.getInstance();
    private GameState currentState = GameState.SETUP;

    private EventManager eventManager;
    private BoardInputProcessor boardInputProcessor;
    private DraftInputProcessor draftInputProcessor;

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

    public GameManager() {
        // initialize game manager
        eventManager = EventManager.getInstance();
        boardInputProcessor = new BoardInputProcessor(this);
        draftInputProcessor = new DraftInputProcessor(this);
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

                if (draftInputProcessor.show == false) {
                    draftInputProcessor.show = true;
                    updateTurn();
                }
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
        //     deck.drawCard();
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

            draftInputProcessor.reset();
            boardInputProcessor.reset();
        }
    }

    private void placeDomino() {
        placingDomino = true;
        if (boardInputProcessor.exit && boardInputProcessor.valid) {
            currentBoard.getScoringSystem().calculateLandScore();
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
        if (draftInputProcessor.exit) {
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
                return;
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

    // ---------------------RENDER---------------------

    public void render(SpriteBatch batch) {
        // render game components

        // boolean updated is using for updating the screen only when the input
        // processor is updated (key pressed)
        switch (currentState) {
            case SETUP:
                break;
            case TURN_START:
                if (draftInputProcessor.updated) {
                    draftInputProcessor.updated = false;
                    clearScreen();
                    if (!finalTurn) {
                        System.out.printf("TURN: %d\n\n", currentTurn.getTurnId() - 1);
                    } else {
                        System.out.printf("FINAL TURN! ONLY PLACING DOMINO! \n\n");
                    }
                    renderKingQueueWithSelection(currentTurn.getKings(), currentTurn.getCurrentKing());
                    System.out.println();
                    System.out.println("(press any key to continue)");
                }
                break;
            case TURN_PLACING:
                if (boardInputProcessor.updated) {
                    boardInputProcessor.updated = false;
                    clearScreen();
                    System.out.printf("PLACE DOMINO\n\n");
                    renderBoard(currentBoard, currentDomino);
                    renderStatusPlacing(currentTurn);
                    System.out.println();
                    System.out.println(
                            "([X] confirm | [C] discard | [Q] counter-clockwise | [E] clockwise | [WASD] move)");
                }
                break;
            case TURN_CHOOSING:
                if (draftInputProcessor.updated) {
                    draftInputProcessor.updated = false;
                    clearScreen();

                    System.out.printf("CHOOSE NEXT DOMINO\n\n");
                    Domino[] nextRemainingDraft = nextTurn.getDraft();
                    draftInputProcessor.remainingDrafts = nextRemainingDraft.length;
                    renderQueueWithSelection(nextRemainingDraft, draftInputProcessor.selectionIndex); // TODO: optimize
                    renderStatusChoosing(currentTurn);

                    System.out.println();
                    renderBoard(currentBoard, null);
                }
                break;
            case TURN_END:
                clearScreen();
                break;
            case GAME_OVER:
                break;
            case RESULTS:
                if (scores != null && !resultsRendered) {
                    renderResults(scores);
                    resultsRendered = true;
                }
                break;
            default:
                break;
        }
        // if (boardInputProcessor.updated) {
        // boardInputProcessor.updated = false;
        // clearScreen();
        // renderBoard(currentBoard, currentDomino);
        // }
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void renderStatusChoosing(Turn currentTurn) {
        System.out.println();
        System.out.println("KING " + currentTurn.getCurrentKing().getId() + " PREVIOUS DOMINO: "
                + getCharType(
                        currentTurn.getCurrentDomino().getTileA().getTerrain())
                + "|" + getCharType(
                        currentTurn.getCurrentDomino().getTileB().getTerrain()));
    }

    private static void renderStatusPlacing(Turn currentTurn) {
        System.out.println();
        System.out.printf("KING %d's LAND | CURRENT DOMINO [%c%d|%c%d] | CURRENT SCORE: %d\n",
                currentTurn.getCurrentKing().getId(),
                getCharType(currentTurn.getCurrentDomino().getTileA().getTerrain()),
                currentTurn.getCurrentDomino().getTileA().getCrown(),
                getCharType(currentTurn.getCurrentDomino().getTileB().getTerrain()),
                currentTurn.getCurrentDomino().getTileB().getCrown(),
                currentTurn.getCurrentKing().getBoard().getScoringSystem().getLandTotal());
    }

    private static void renderQueueWithSelection(Domino[] draft, int index) {
        for (int i = 0; i < draft.length; i++) {
            int crownA = draft[i].getTileA().getCrown();
            int crownB = draft[i].getTileB().getCrown();
            char charA = getCharType(draft[i].getTileA().getTerrain());
            char charB = getCharType(draft[i].getTileB().getTerrain());

            if (i == index) {
                System.out.printf("%c%d|%c%d <\n", charA, crownA, charB, crownB);
            } else {
                System.out.printf("%c%d|%c%d\n", charA, crownA, charB, crownB);
            }
        }
    }

    private static void renderKingQueueWithSelection(King[] kings, King currentKing) {
        for (int i = 0; i < kings.length; i++) {
            if (kings[i] == currentKing) {
                System.out.println(kings[i] + " <");
            } else {
                System.out.println(kings[i]);
            }
        }
    }

    private static void renderBoard(Board board, Domino domino) {
        Tile[][] land = board.getLand(); // getLand() returns a clone of the land

        if (domino != null) {
            Tile tile1 = domino.getTileA();
            Tile tile2 = domino.getTileB();
            Position pos1 = domino.getPosTileA();
            Position pos2 = domino.getPosTileB();

            try {
                land[pos1.y()][pos1.x()] = tile1;
                land[pos2.y()][pos2.x()] = tile2;
            } catch (ArrayIndexOutOfBoundsException e) {
                domino.undo();
                tile1 = domino.getTileA();
                tile2 = domino.getTileB();
                pos1 = domino.getPosTileA();
                pos2 = domino.getPosTileB();
                land[pos1.y()][pos1.x()] = tile1;
                land[pos2.y()][pos2.x()] = tile2;
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (land[i][j] != null) {
                    System.out.print(getCharType(land[i][j].getTerrain()) + " ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }

        // land[pos1.y()][pos1.x()] = null;
        // land[pos2.y()][pos2.x()] = null;
    }

    private static char getCharType(TerrainType type) {
        switch (type) {
            case WHEATFIELD:
                return 'W';
            case FOREST:
                return 'F';
            case LAKE:
                return 'L';
            case GRASSLAND:
                return 'G';
            case SWAMP:
                return 'S';
            case MINE:
                return 'M';
            case MOUNTAIN:
                return 'T';
            case DESERT:
                return 'D';
            case CASTLE:
                return 'C';
            case INVALID:
                return 'X';
            default:
                return ' ';
        }
    }

    private static void renderResults(Map<King, int[]> scores) {
        System.out.printf("RESULTS \n(TOTAL | LAND | BONUS)\n\n");
        int rank = 1;
        for (Map.Entry<King, int[]> entry : scores.entrySet()) {
            King king = entry.getKey();
            int[] score = entry.getValue();
            System.out.printf("%d       King %d\n        %d|%d|%d\n\n",
                    rank++, king.getId(), score[2], score[0], score[1]);
        }
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

    public DraftInputProcessor getDraftInputProcessor() {
        return this.draftInputProcessor;
    }
}
