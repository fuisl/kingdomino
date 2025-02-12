package dev.kingdomino.game;

/**
 * Represents the game board for Kingdomino.
 * Manages the placement and validation of tiles and dominos.
 * 
 * @author @fuisl
 * @version 1.0
 */
public class Board {

    private final int CENTER;
    private Tile[][] land;
    private final TileValidator validator;
    private final ScoringSystem score;
    // private King king; // TODO: associate player with board

    /**
     * Initializes the game board with a castle at the center.
     * 
     * @param size the size of the board.
     */
    public Board(int size) {
        this.CENTER = size - 1;
        this.land = new Tile[(size * 2) - 1][(size * 2) - 1];
        this.land[CENTER][CENTER] = new Tile(TerrainType.CASTLE, 0);
        this.validator = new TileValidator(land, size);
        this.score = new ScoringSystem(land, size);
    }

    /**
     * Initializes the game board with a default size of 5x5.
     */
    public Board() {
        this(5); // default size is 5x5 (but data stored in 9x9 array)
    }

    /**
     * Retrieves the land of the board.
     * 
     * @return the land of the board.
     * @see Tiles
     */
    public Tile[][] getLand() {
        return land;
    }

    /**
     * Checks if a tile can be placed at the specified coordinates. Placeable means
     * the tile is within bounds and not occupied.
     * 
     * @param tile the tile to place.
     * @param x    the x-coordinate.
     * @param y    the y-coordinate.
     * @return true if the tile can be placed, false otherwise.
     */
    public boolean isTilePlaceable(Tile tile, int x, int y) {
        // TODO: change x, y to Position
        return validator.isTilePlaceable(tile, x, y);
    }

    /**
     * Places a tile at the specified coordinates.
     * 
     * @param tile the tile to place.
     * @param x    the x-coordinate.
     * @param y    the y-coordinate.
     */
    public void setTile(Tile tile, int x, int y) {
        if (!isTilePlaceable(tile, x, y)) {
            throw new IllegalArgumentException("Invalid tile placement.");
        }

        // TODO: change x, y to Position
        land[y][x] = tile;

        // update spanning tiles if the tile is placed
        validator.updateSpanningTiles(x, y);
    }

    /**
     * Retrieves the tile at the specified coordinates.
     * 
     * @param x the x-coordinate (column).
     * @param y the y-coordinate (row).
     * @return the tile at the specified coordinates, or null if out of bounds.
     */
    public Tile getTile(int x, int y) {
        if (validator.isTileWithinLand(x, y)) {
            return land[y][x];
        }
        return null;
    }

    /**
     * Checks if a domino can be placed on the board.
     * 
     * @param domino the domino to place.
     * @return true if the domino can be placed, false otherwise.
     */
    public boolean isDominoPlaceable(Domino domino) {
        return validator.isTilePlaceable(domino.getTileA(), domino.getPosTileA().x(), domino.getPosTileA().y()) &&
                validator.isTilePlaceable(domino.getTileB(), domino.getPosTileB().x(), domino.getPosTileB().y()) &&
                (validator.isTileConnectable(domino.getTileA(), domino.getPosTileA().x(), domino.getPosTileA().y()) ||
                        validator.isTileConnectable(domino.getTileB(), domino.getPosTileB().x(),
                                domino.getPosTileB().y()));
    }

    /**
     * Places a domino on the board if it is placeable.
     * 
     * @param domino the domino to place.
     * @return 0 if the domino was successfully placed. Otherwise, return a non-zero
     */
    public int setDomino(Domino domino) {
        if (isDominoPlaceable(domino)) {
            setTile(domino.getTileA(), domino.getPosTileA().x(), domino.getPosTileA().y());
            setTile(domino.getTileB(), domino.getPosTileB().x(), domino.getPosTileB().y());
            return 0;
        }

        return -1; // may use other return code to indicate the reason
    }

    /**
     * Creates a copy of the current board.
     * 
     * @return a new Board object that is a copy of the current board.
     */
    public Board copy() {
        Board board = new Board();
        board.land = getLand();
        return board;
    }

    /**
     * Retrieves the scoring system associated with the board.
     * 
     * @return the scoring system.
     */
    public ScoringSystem getScoringSystem() {
        return score;
    }
}