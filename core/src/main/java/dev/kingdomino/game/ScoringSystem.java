package dev.kingdomino.game;

// import java.util.Arrays;

public class ScoringSystem {
    private Tile[][] land;
    private int landTotal; // total points from land tiles
    private int landBonus; // bonus points (if any)
    private int totalCrown;
    private int size; // (5x5 or 7x7)

    // for bonus calculation
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;

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

    public void calculateLandScore() {
        boolean visited[][] = new boolean[land.length][land.length];

        // for (boolean[] row : visited) {
        // Arrays.fill(row, false);
        // }

        // flood fill
        int landTotalTemp = 0;

        for (int i = 0; i < land.length; i++) {
            for (int j = 0; j < land.length; j++) {
                if (land[i][j] != null && !visited[i][j]) {
                    totalCrown = 0;
                    int score = floodFill(i, j, land[i][j].getTerrain(), visited);
                    landTotalTemp += score * totalCrown;
                }
            }
        }

        landTotal = landTotalTemp;
    }

    private int floodFill(int x, int y, TerrainType terrain, boolean[][] visited) {
        // out of bounds
        if (x < 0 || x >= land.length || y < 0 || y >= land.length) {
            return 0;
        }

        if (land[x][y] == null || visited[x][y] || land[x][y].getTerrain() != terrain) {
            return 0;
        }

        totalCrown += land[x][y].getCrown();
        visited[x][y] = true;

        return 1 +
                floodFill(x + 1, y, terrain, visited) +
                floodFill(x - 1, y, terrain, visited) +
                floodFill(x, y + 1, terrain, visited) +
                floodFill(x, y - 1, terrain, visited);
    }

    public void calculateBonusScore() {
        int tempBonus = 0;

        int countNotNull = 0;
        for (int i = 0; i < land.length; i++) {
            for (int j = 0; j < land.length; j++) {
                if (land[i][j] != null) {
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
        if (land[(maxY + minY) / 2][(maxX + minX) / 2].getTerrain() == TerrainType.CASTLE) {
            tempBonus += 10;
        }

        landBonus = tempBonus;
    }

    public void calculateScore() {
        calculateLandScore();
        calculateBonusScore();
    }

    public int getBoardTotal() {
        return landTotal + landBonus;
    }

    public int getLandTotal() {
        return landTotal;
    }

    public int getLandBonus() {
        return landBonus;
    }
}
