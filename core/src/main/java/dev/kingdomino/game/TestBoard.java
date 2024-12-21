package dev.kingdomino.game;

public class TestBoard {
    public static void main(String[] args) {
        IBoard board = new Board();
        Tile tile = new Tile(TerrainType.WHEATFIELD, 1);
        System.out.println(board.isTilePlaceable(tile, 4, 3));
        System.out.println(board.isTilePlaceable(tile, 4, 4));
        System.out.println(board.isTilePlaceable(tile, 4, 5));

        board.setTile(tile, 4, 5);
        board.setTile(tile, 4, 6);
        board.setTile(tile, 4, 7);
        board.setTile(tile, 4, 8);


        System.out.println(board.isTilePlaceable(tile, 3, 4));
        board.setTile(tile, 3, 4);
        board.setTile(tile, 2, 4);
        board.setTile(tile, 5, 4);
        board.setTile(tile, 6, 4);
        // board.setTile(tile, 4, 9);
        // board.setTile(tile, 4, 10);
        // board.setTile(tile, 4, 3);

        System.out.println(board.isTilePlaceable(tile, 4, 3));  // out of bound for x
        System.out.println(board.isTilePlaceable(tile, 7, 4));  // out of bound for y
        System.out.println(board.isTilePlaceable(tile, 5, 5));  // true

        printBoard(board);
        // System.out.println(board.getTile(4, 5).getTerrain());
    }

    public static void printBoard(IBoard board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.getTile(j, i) != null) {
                    System.out.print(board.getTile(j, i).getTerrain() + " ");
                } else {
                    System.out.print("null ");
                }
            }
            System.out.println();
        }
    }
}
