

import dev.kingdomino.game.ScoringSystem;
import dev.kingdomino.game.TerrainType;
import dev.kingdomino.game.Tile;

public class TestScoring {
    final static int SIZE = 9;

    public static void main(String[] args) {
        // create a mock board
        Tile[][] land = new Tile[SIZE][SIZE];

        int CENTER = SIZE / 2;

        // tile with the castle (this represented the demo in rule book)
        land[CENTER][CENTER] = new Tile(TerrainType.CASTLE, 0);

        land[CENTER][CENTER + 1] = new Tile(TerrainType.FIELD, 1);
        land[CENTER + 1][CENTER] = new Tile(TerrainType.FIELD, 0);
        land[CENTER + 1][CENTER + 1] = new Tile(TerrainType.FIELD, 0);

        land[CENTER][CENTER + 2] = new Tile(TerrainType.PLAINS, 0);
        land[CENTER][CENTER + 3] = new Tile(TerrainType.PLAINS, 0);
        land[CENTER][CENTER + 4] = new Tile(TerrainType.PLAINS, 0);
        land[CENTER + 1][CENTER + 2] = new Tile(TerrainType.PLAINS, 2);

        land[CENTER + 2][CENTER + 1] = new Tile(TerrainType.LAKE, 1);
        land[CENTER + 3][CENTER + 1] = new Tile(TerrainType.LAKE, 0);
        land[CENTER + 2][CENTER + 2] = new Tile(TerrainType.LAKE, 0);
        land[CENTER + 3][CENTER + 2] = new Tile(TerrainType.LAKE, 0);
        land[CENTER + 3][CENTER + 3] = new Tile(TerrainType.LAKE, 1);

        land[CENTER + 2][CENTER + 3] = new Tile(TerrainType.FOREST, 0);

        land[CENTER + 4][CENTER] = new Tile(TerrainType.MINE, 2);

        // init scoringSystem
        ScoringSystem scoringSystem = new ScoringSystem(land, CENTER + 1);
        scoringSystem.calculateScore();
        System.out.println("Total score: " + scoringSystem.getBoardTotal());

        // test center
        land[CENTER + 2][CENTER + 2] = new Tile(TerrainType.CASTLE, 0);
        scoringSystem.calculateScore();
        System.out.println("Total score: " + scoringSystem.getBoardTotal());
    }
}
