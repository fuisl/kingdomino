package dev.kingdomino.game;

/**
 * Represents the game board for Kingdomino.
 * Manages the placement and validation of tiles and dominos.
 */
public class Board {

    private final int CENTER;
    private Tile[][] land;
    private final TileValidator validator;
    private final ScoringSystem score;
    // private King king; // TODO: associate player with board

    /**
     * Initializes the game board with a castle at the center.
     */
    public Board(int size) {
        this.CENTER = size - 1;
        this.land = new Tile[(size * 2) - 1][(size * 2) - 1];
        this.land[CENTER][CENTER] = new Tile(TerrainType.CASTLE, 0);
        this.validator = new TileValidator(land, size);
        this.score = new ScoringSystem(land, size);
    }

    public Board() {
        this(5); // default size is 5x5 (but data stored in 9x9 array)
    }

    /**
     * Returns a deep copy of the land tiles.
     * 
     * @return a 2D array of tiles representing the land.
     * @see Tile
     */
    public Tile[][] getLand() {
        // deep copy

        Tile[][] land = new Tile[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                land[i][j] = this.land[i][j];
            }
        }

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

    public Board copy() {
        Board board = new Board();
        board.land = getLand();
        return board;
    }

    public ScoringSystem getScoringSystem() {
        return score;
    }
}