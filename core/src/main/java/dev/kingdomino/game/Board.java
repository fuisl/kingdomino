package dev.kingdomino.game;

/**
 * Represents the game board for Kingdomino.
 * Manages the placement and validation of tiles and dominos.
 */
public class Board implements IBoard {

    private final int CENTER = 4; // TODO: change this later for other game modes
    private Tile[][] land;
    private Point point; // scores
    private final TileValidator validator;

    /**
     * Initializes the game board with a castle at the center.
     */
    public Board() {
        land = new Tile[9][9];
        land[CENTER][CENTER] = new Tile(TerrainType.CASTLE, 0);
        validator = new TileValidator(land);
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
    @Override
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
    @Override
    public void setTile(Tile tile, int x, int y) {
        // TODO: change x, y to Position
        land[x][y] = tile;

        // update spanning tiles if the tile is placed
        validator.updateSpanningTiles(x, y);
    }

    /**
     * Retrieves the tile at the specified coordinates.
     * 
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @return the tile at the specified coordinates, or null if out of bounds.
     */
    @Override
    public Tile getTile(int x, int y) {
        if (validator.isTileWithinLand(x, y)) {
            return land[x][y];
        }
        return null;
    }

    /**
     * Checks if a domino can be placed on the board.
     * 
     * @param domino the domino to place.
     * @return true if the domino can be placed, false otherwise.
     */
    @Override
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
     */
    @Override
    public void setDomino(Domino domino) {
        if (isDominoPlaceable(domino)) {
            setTile(domino.getTileA(), domino.getPosTileA().x(), domino.getPosTileA().y());
            setTile(domino.getTileB(), domino.getPosTileB().x(), domino.getPosTileB().y());
        }
    }

    // Point related methods

    /**
     * Gets the final point score.
     * 
     * @return the final point score.
     */
    public int getFinalPoint() {
        return point.getFinalPoint();
    }

    /**
     * Gets the total point score.
     * 
     * @return the total point score.
     */
    public int getTotalPoint() {
        return point.getTotalPoint();
    }

    /**
     * Gets the additional point score.
     * 
     * @return the additional point score.
     */
    public int getAdditionalPoint() {
        return point.getAdditionalPoint();
    }

    /**
     * Adds to the total point score.
     * 
     * @param totalPoint the points to add.
     */
    public void addTotalPoint(int totalPoint) {
        point.addTotalPoint(totalPoint);
    }

    /**
     * Adds bonus points.
     * 
     * @param bonusPoint the bonus points to add.
     */
    public void addBonusPoint(int bonusPoint) {
        point.addAdditionalPoint(bonusPoint);
    }

    /**
     * Resets the point scores.
     */
    public void reset() {
        point.reset();
    }

}