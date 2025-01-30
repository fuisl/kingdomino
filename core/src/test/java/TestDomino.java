

import dev.kingdomino.game.Board;
import dev.kingdomino.game.Direction;
import dev.kingdomino.game.Domino;
import dev.kingdomino.game.DominoDeck;

public class TestDomino {
    public static void main(String[] args) {
        Board board = new Board();
        Domino domino = DominoDeck.DOMINO_1.getDomino();

        domino.moveDomino(Direction.RIGHT);
        domino.moveDomino(Direction.RIGHT);
        domino.moveDomino(Direction.RIGHT);
        domino.moveDomino(Direction.DOWN);
        domino.moveDomino(Direction.DOWN);
        domino.moveDomino(Direction.DOWN);
        System.out.println(domino.getPosTileA());
        System.out.println(domino.getPosTileB());

        System.out.println(board.isDominoPlaceable(domino));
        board.setDomino(domino);
        printBoard(board);

        Domino domino2 = DominoDeck.DOMINO_2.getDomino();
        System.out.println(domino.getPosTileA());
        System.out.println(domino.getPosTileB());
        domino2.moveDomino(Direction.RIGHT);
        domino2.moveDomino(Direction.RIGHT);
        // domino2.moveDomino(Offset.RIGHT);
        domino2.moveDomino(Direction.DOWN);
        // domino2.moveDomino(Offset.DOWN);
        domino2.rotateDomino(true);
        board.setDomino(domino2);


        // domino2.moveDomino(Offset.DOWN);

        System.out.println(board.isDominoPlaceable(domino2));

        // System.out.println("moving DOWN");
        // domino.moveDomino(Offset.DOWN);
        // System.out.println(domino.getPosTileA());
        // System.out.println(domino.getPosTileB());

        // System.out.println("rotating clockwise");
        // domino.rotate(true);
        // System.out.println(domino.getPosTileA());
        // System.out.println(domino.getPosTileB());

        // System.out.println("rotating clockwise");
        // domino.rotate(true);
        // System.out.println(domino.getPosTileA());
        // System.out.println(domino.getPosTileB());
        printBoard(board);
    }

    public static void printBoard(Board board) {
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
