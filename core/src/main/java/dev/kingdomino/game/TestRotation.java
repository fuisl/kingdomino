package dev.kingdomino.game;

public class TestRotation {
    public static void main(String[] args) {
        // get Domino from DominoDeck
        Domino domino = DominoDeck.DOMINO_1.getDomino();
        System.out.println(domino.getPosTileA());
        System.out.println(domino.getPosTileB());

        domino.rotate(true);
        domino.rotate(true);
        System.out.println(domino.getPosTileA());
        System.out.println(domino.getPosTileB());
        
        domino.rotate(false);
        System.out.println(domino.getPosTileA());
        System.out.println(domino.getPosTileB());
    }
}
