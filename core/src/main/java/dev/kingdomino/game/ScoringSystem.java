package dev.kingdomino.game;

// import java.util.Arrays;

/**
 * Represents the scoring system of the game. Algorithm: flood fill
 * 
 * @see Tile
 * @see TerrainType
 * @see Board
 * 
 * @author @fuisl
 * @version 1.0
 */
public class ScoringSystem {
    private final Tile[][] land;
    private int landTotal; // total points from land tiles
    private int landBonus; // bonus points (if any)
    private int totalCrown;
    private final int size; // (5x5 or 7x7)

    // for bonus calculation
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;

    /**
     * Constructs a ScoringSystem with the given land tiles and board size.
     *
     * @param land the 2D array of land tiles
     * @param size the size of the board (5x5 or 7x7)
     * 
     * @precondition land != null
     * @precondition size == 5 || size == 7
     * 
     */
    public ScoringSystem(Tile[][] land, int size) {
        this.landTotal = 0;
        this.landBonus = 0;
        this.land = land;
        this.size = size;

        minX = land.length;
        maxX = 0;
        minY = land.length;
        maxY = 0;
    }

    /**
     * Calculates the total score from land tiles.
     */
    public void calculateLandScore() {
        boolean visited[][] = new boolean[land.length][land.length];

        // for (boolean[] row : visited) {
        // Arrays.fill(row, false);
        // }

        // flood fill
        int landTotalTemp = 0;

        for (int i = 0; i < land.length; i++) {
            for (int j = 0; j < land.length; j++) {
                if (land[j][i] != null && !visited[j][i]) {
                    totalCrown = 0;
                    int score = floodFill(i, j, land[j][i].getTerrain(), visited);
                    landTotalTemp += score * totalCrown;
                }
            }
        }

        landTotal = landTotalTemp;
    }

    /**
     * Performs a flood fill algorithm to calculate the score for a connected region
     * of the same terrain type.
     *
     * @param x       the x-coordinate of the starting tile
     * @param y       the y-coordinate of the starting tile
     * @param terrain the terrain type to match
     * @param visited the 2D array to track visited tiles
     * @return the size of the connected region
     */
    private int floodFill(int x, int y, TerrainType terrain, boolean[][] visited) {
        // out of bounds
        if (x < 0 || x >= land.length || y < 0 || y >= land.length) {
            return 0;
        }

        if (land[y][x] == null || visited[y][x] || land[y][x].getTerrain() != terrain) {
            return 0;
        }

        totalCrown += land[y][x].getCrown();
        visited[y][x] = true;

        return 1 +
                floodFill(x + 1, y, terrain, visited) +
                floodFill(x - 1, y, terrain, visited) +
                floodFill(x, y + 1, terrain, visited) +
                floodFill(x, y - 1, terrain, visited);
    }

    /**
     * Calculates the bonus score based on harmony rules and middle kingdom rules.
     */
    public void calculateBonusScore() {
        int tempBonus = 0;

        int countNotNull = 0;
        for (int i = 0; i < land.length; i++) {
            for (int j = 0; j < land.length; j++) {
                if (land[j][i] != null) {
                    countNotNull++;
                    minX = Math.min(minX, i);
                    minY = Math.min(minY, j);
                    maxX = Math.max(maxX, i);
                    maxY = Math.max(maxY, j);
                }
            }
        }

        // harmony rules
        if (countNotNull == size * size) {
            tempBonus += 5;
        }

        // middle kingdom
        try {
            if (land[(maxY + minY) / 2][(maxX + minX) / 2].getTerrain() == TerrainType.CASTLE) {
                tempBonus += 10;
            }
        } catch (Exception e) {
            // do nothing
            // System.out.println("null tile");
        }

        landBonus = tempBonus;
    }

    /**
     * Calculates the total score including land and bonus scores.
     */
    public void calculateScore() {
        calculateLandScore();
        calculateBonusScore();
    }

    /**
     * Returns the total score of the board.
     *
     * @return the total score
     */
    public int getBoardTotal() {
        return landTotal + landBonus;
    }

    /**
     * Returns the total score from land tiles.
     *
     * @return the land score
     */
    public int getLandTotal() {
        return landTotal;
    }

    /**
     * Returns the bonus score.
     *
     * @return the bonus score
     */
    public int getLandBonus() {
        return landBonus;
    }
}
