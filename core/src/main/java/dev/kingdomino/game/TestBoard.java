package dev.kingdomino.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestBoard {
    public static void main(String[] args) throws IOException, InterruptedException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Board board = new Board();
        Domino domino = DominoDeck.DOMINO_1.getDomino();
        boolean flag = false;

        while (true) {
            if (reader.ready()) {
                char key = (char) reader.read();

                switch (key) {
                    case 'e':
                        domino.rotate(true);
                        break;
                    case 'q':
                        domino.rotate(false);
                        break;
                    case 'w':
                        domino.moveDomino(Offset.UP);
                        break;
                    case 's':
                        domino.moveDomino(Offset.DOWN);
                        break;
                    case 'a':
                        domino.moveDomino(Offset.LEFT);
                        break;
                    case 'd':
                        domino.moveDomino(Offset.RIGHT);
                        break;
                    case 'x':
                        board.setDomino(domino);
                        domino = DominoDeck.DOMINO_2.getDomino();
                        break;
                    case 'c':
                        System.out.println("Exiting...");
                        flag = true;
                    default:
                        break;
                }
            }
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
            case WHEATFIELD:
                return 'W';
            case FOREST:
                return 'F';
            case LAKE:
                return 'L';
            case GRASSLAND:
                return 'G';
            case SWAMP:
                return 'S';
            case MINE:
                return 'M';
            case MOUNTAIN:
                return 'T';
            case DESERT:
                return 'D';
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
