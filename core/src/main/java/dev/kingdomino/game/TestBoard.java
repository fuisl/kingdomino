package dev.kingdomino.game;

public class TestBoard {
    public static void main(String[] args) {
        Board board = new Board();
        Tile tile = new Tile(TerrainType.WHEATFIELD, 1);
        System.out.println(board.isTilePlaceable(tile, 4, 3));
        System.out.println(board.isTilePlaceable(tile, 4, 4));
        System.out.println(board.isTilePlaceable(tile, 4, 5));
        
        board.setTile(tile, 4, 5);
        System.out.println(board.isTilePlaceable(tile, 4, 5));
        
        printBoard(board);
        // System.out.println(board.getTile(4, 5).getTerrain());
    }

    public static void printBoard(Board board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.getTile(i, j) != null) {
                    System.out.print(board.getTile(i, j).getTerrain() + " ");
                } else {
                    System.out.print("null ");
                }
            }
            System.out.println();
        }
    }
}
