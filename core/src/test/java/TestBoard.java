

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import dev.kingdomino.game.Board;
import dev.kingdomino.game.Deck;
import dev.kingdomino.game.Direction;
import dev.kingdomino.game.Domino;
import dev.kingdomino.game.DominoController;
import dev.kingdomino.game.Position;
import dev.kingdomino.game.TerrainType;
import dev.kingdomino.game.Tile;

public class TestBoard {
    public static void main(String[] args) throws IOException, InterruptedException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Board board = new Board();
        Deck deck = new Deck();
        boolean flag = false;
        boolean update = true;
        boolean invalid = false;
        Domino domino = deck.drawCard();

        while (true) {
            if (reader.ready()) {
                char key = (char) reader.read();

                switch (key) {
                    case 'e':    
                        domino.rotateDomino(true);
                        update = true;
                        break;
                    case 'q':
                        domino.rotateDomino(false);
                        update = true;
                        break;
                    case 'w':
                        domino.moveDomino(Direction.UP);
                        update = true;
                        break;
                    case 's':
                        domino.moveDomino(Direction.DOWN);
                        update = true;
                        break;
                    case 'a':
                        domino.moveDomino(Direction.LEFT);
                        update = true;
                        break;
                    case 'd':
                        domino.moveDomino(Direction.RIGHT);
                        update = true;
                        break;
                    case 'x':
                        if (board.setDomino(domino) != 0) {
                            invalid = true;
                            break;
                        }
                        domino = deck.drawCard();
                        update = true;
                        break;
                    case 'c':
                        System.out.println("Exiting...");
                        flag = true;
                        break;
                    default:
                        break;
                }
            }

            if (update) {
                update = false;
                clearScreen();

                if (flag) {
                    break;
                }

                render(board, domino);
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (invalid) {
                invalid = false;
                Domino temp = new Domino(0, new Tile(TerrainType.CASTLE, 0), new Tile(TerrainType.CASTLE, 0),
                        new DominoController());
                System.out.println(domino.getPosTileA().x() + " " + domino.getPosTileA().y());
                temp.setPosTileA(domino.getPosTileA());
                temp.setPosTileB(domino.getPosTileB());
                System.out.println(temp.getPosTileA().x() + " " + temp.getPosTileA().y());
                clearScreen();
                render(board, temp);
            }
        }
    }

    public static void render(Board board, Domino domino) {
        Tile[][] land = board.getLand(); // getLand() returns a clone of the land
        Tile tile1 = domino.getTileA();
        Tile tile2 = domino.getTileB();
        Position pos1 = domino.getPosTileA();
        Position pos2 = domino.getPosTileB();

        land[(pos1.x())][(pos1.y())] = tile1;
        land[(pos2.x())][(pos2.y())] = tile2;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (land[j][i] != null) {
                    System.out.print(getCharType(land[j][i].getTerrain()) + " ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }

        // land[pos1.x()][pos1.y()] = null;
        // land[pos2.x()][pos2.y()] = null;
    }

    public static char getCharType(TerrainType type) {
        switch (type) {
            case FIELD:
                return 'W';
            case FOREST:
                return 'F';
            case LAKE:
                return 'L';
            case PLAINS:
                return 'G';
            case SWAMPS:
                return 'S';
            case MINE:
                return 'M';
            case CASTLE:
                return 'C';
            default:
                return ' ';
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
