package dev.kingdomino.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameManager {
    private Board board;
    private Deck deck;
    private Domino currentDomino;
    private GameTimer gameTimer = GameTimer.getInstance();

    private EventManager eventManager;
    private BoardInputProcessor inputProcessor;

    public GameManager() {
        board = new Board();
        deck = new Deck();

        eventManager = EventManager.getInstance();
        inputProcessor = new BoardInputProcessor(this);
        Gdx.input.setInputProcessor(new InputMultiplexer(inputProcessor));

        Event initDomino = new Event(
                Event.TriggerType.IMMEDIATE,
                false, false,
                null,
                () -> {
                    currentDomino = deck.drawCard();
                    inputProcessor.updated = true;
                },
                gameTimer,
                null,
                null);

        eventManager.addEvent(initDomino, "base", false);
    }

    public void update(float dt) {
        gameTimer.update(dt);
        eventManager.update(dt, true);

        if (inputProcessor.exit) {
            Gdx.app.exit();
        }

        if (inputProcessor.updated && inputProcessor.valid) {
            inputProcessor.valid = false;
            currentDomino = deck.drawCard();
        }
    }

    public void render(SpriteBatch batch) {
        if (inputProcessor.updated) {
            inputProcessor.updated = false;
            clearScreen();
            renderBoard(board, currentDomino);
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
        return this.board;
    }

    public Domino getCurrentDomino() {
        return this.currentDomino;
    }

    public void setCurrentDomino(Domino domino) {
        this.currentDomino = domino;
    }
}
