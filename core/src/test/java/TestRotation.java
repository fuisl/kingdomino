

import dev.kingdomino.game.Domino;
import dev.kingdomino.game.DominoDeck;

public class TestRotation {
    public static void main(String[] args) {
        // get Domino from DominoDeck
        Domino domino = DominoDeck.DOMINO_1.getDomino();
        System.out.println(domino.getPosTileA());
        System.out.println(domino.getPosTileB());

        domino.rotateDomino(true);
        domino.rotateDomino(true);
        System.out.println(domino.getPosTileA());
        System.out.println(domino.getPosTileB());
        
        domino.rotateDomino(false);
        System.out.println(domino.getPosTileA());
        System.out.println(domino.getPosTileB());
    }
}
