package dev.kingdomino.game;

public class TestDomino {
    public static void main(String[] args) {
        IBoard board = new Board();
        Domino domino = DominoDeck.DOMINO_1.getDomino();


        System.out.println(domino.getPosTileA());
        System.out.println(domino.getPosTileB());
        System.out.println("moving DOWN");
        domino.moveDomino(Offset.DOWN);
        System.out.println(domino.getPosTileA());
        System.out.println(domino.getPosTileB());
        // System.out.println(board.isDominoPlaceable(domino));

        System.out.println("rotating clockwise");
        domino.rotate(true);
        System.out.println(domino.getPosTileA());
        System.out.println(domino.getPosTileB());

        System.out.println("rotating clockwise");
        domino.rotate(true);
        System.out.println(domino.getPosTileA());
        System.out.println(domino.getPosTileB());
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
