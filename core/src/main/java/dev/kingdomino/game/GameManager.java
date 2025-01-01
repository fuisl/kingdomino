package dev.kingdomino.game;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameManager {
    private int kingCount = 4;
    private Deck deck;
    private Board currentBoard;
    private Domino currentDomino;
    private Turn currentTurn;
    private King currentKing;
    private Turn nextTurn;

    private GameTimer gameTimer = GameTimer.getInstance();
    private GameState currentState = GameState.SETUP;

    private EventManager eventManager;
    private BoardInputProcessor inputProcessor;

    public enum GameState {
        SETUP,
        TURN, // placing domino
        // TURN_CHOOSING, // choosing next domino
        ROUND_END,
        GAME_OVER,
        RESULTS
    }

    public GameManager() {
        // initialize game manager
        eventManager = EventManager.getInstance();
        inputProcessor = new BoardInputProcessor(this);
        Gdx.input.setInputProcessor(new InputMultiplexer(inputProcessor));

        // initialize game components
        deck = new Deck(0);
        currentState = GameState.SETUP;
    }

    public void update(float dt) {
        // Core game loop -- must update every frame
        gameTimer.update(dt);
        eventManager.update(dt, true);

        if (inputProcessor.exit) {
            Gdx.app.exit();
        }

        switch (currentState) {
            case SETUP:
                setup();
                break;
            case TURN:
                turnPlacing();
                break;
            case ROUND_END:
                round_end();
                break;
            case GAME_OVER:
                System.out.println("Game Over");
                game_over();
                break;
            case RESULTS:
                System.out.println("Results");
                System.out.println("Press \"c\" to exit.");
                break;
            default:
                break;
        }
    }

    private void setup() {
        Domino[] draft = new Domino[kingCount];

        // draw dominos for each king
        for (int i = 0; i < kingCount; i++) {
            draft[i] = deck.drawCard();
        }
        currentTurn = new Turn(draft);

        // setup next turn
        if (deck.isEmpty()) {
            nextTurn = null;
        } else {
            for (int i = 0; i < kingCount; i++) {
                draft[i] = deck.drawCard();
            }
            nextTurn = new Turn(draft);
        }

        // setup the kings for the turn (without shuffling)
        King[] kings = new King[kingCount];
        for (int i = 0; i < kingCount; i++) {
            kings[i] = new King(i);
            currentTurn.setKing(kings[i], i);
        }

        // set game state to TURN
        currentState = GameState.TURN;
    }

    private void turnPlacing() {
        if (currentTurn.isOver()) {
            currentState = GameState.ROUND_END;
        } else {
            currentDomino = currentTurn.getCurrentDomino();
            currentKing = currentTurn.getCurrentKing();
            currentTurn.next();
        }
    }

    private void round_end() {
        if (nextTurn == null) {
            currentState = GameState.GAME_OVER;
        } else {
            currentTurn = nextTurn;
            nextTurn = null;
            currentState = GameState.TURN;
        }
    }

    private void game_over() {
        currentState = GameState.RESULTS;
    }

    public void render(SpriteBatch batch) {
        if (inputProcessor.updated) {
            inputProcessor.updated = false;
            clearScreen();
            renderBoard(currentBoard, currentDomino);
        }
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
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
}
