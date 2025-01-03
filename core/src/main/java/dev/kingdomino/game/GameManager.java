package dev.kingdomino.game;

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
                break;
            default:
                break;
        }
    }

    // ---------------------GAME STATES---------------------

    private void init() {

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
            currentState = GameState.TURN_CHOOSING;
            placingDomino = false;
        }
    }

    private void chooseDomino() {
        if (nextTurn == null) {
            currentState = GameState.TURN_END;
        }

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
        currentState = GameState.RESULTS;
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
                    renderKingQueueWithSelection(currentTurn.getKings(), currentTurn.getCurrentKing());
                    System.out.println();
                    System.out.println("CURRENT TURN. (press any key to continue)");
                }
                break;
            case TURN_PLACING:
                if (boardInputProcessor.updated) {
                    boardInputProcessor.updated = false;
                    clearScreen();
                    renderBoard(currentBoard, currentDomino);
                    System.out.println();
                    System.out.printf("KING %d's LAND", currentTurn.getCurrentKing().getId());
                }
                break;
            case TURN_CHOOSING:
                if (draftInputProcessor.updated) {
                    draftInputProcessor.updated = false;
                    clearScreen();

                    Domino[] nextRemainingDraft = nextTurn.getRemainingDraft();
                    draftInputProcessor.remainingDrafts = nextRemainingDraft.length;
                    renderQueueWithSelection(nextRemainingDraft, draftInputProcessor.selectionIndex); // TODO: optimize
                    System.out.println();
                    System.out.println("KING " + currentTurn.getCurrentKing().getId() + " NEXT DOMINO: "
                            + getCharType(
                                    nextTurn.getDomino(draftInputProcessor.selectionIndex).getTileA().getTerrain())
                            + "|" + getCharType(
                                    nextTurn.getDomino(draftInputProcessor.selectionIndex).getTileB().getTerrain()));
                }
                break;
            case TURN_END:
                clearScreen();
                break;
            case GAME_OVER:
                break;
            case RESULTS:
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

    private static void renderQueueWithSelection(Domino[] draft, int index) {
        for (int i = 0; i < draft.length; i++) {
            // prinf not working.
            if (i == index) {
                System.out.println(getCharType(draft[i].getTileA().getTerrain()) + "|"
                        + getCharType(draft[i].getTileB().getTerrain()) + " <");
            } else {
                System.out.println(getCharType(draft[i].getTileA().getTerrain()) + "|"
                        + getCharType(draft[i].getTileB().getTerrain()));
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
            land[(pos1.x())][(pos1.y())] = tile1;
            land[(pos2.x())][(pos2.y())] = tile2;
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (land[j][i] != null) {
                    System.out.print(getCharType(land[j][i].getTerrain()) + " ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }

        // land[pos1.x()][pos1.y()] = null;
        // land[pos2.x()][pos2.y()] = null;
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

    public GameState getCurrentState() {
        return currentState;
    }
}
