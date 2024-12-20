package dev.kingdomino.game;

import java.util.Arrays;
import java.util.List;

public enum DominoDeck {
    DOMINO_1(1, new Tile(TerrainType.WHEATFIELD, 0), new Tile(TerrainType.WHEATFIELD, 0)),
    DOMINO_2(2, new Tile(TerrainType.WHEATFIELD, 0), new Tile(TerrainType.WHEATFIELD, 0)),
    DOMINO_3(3, new Tile(TerrainType.FOREST, 0), new Tile(TerrainType.FOREST, 0)),
    DOMINO_4(4, new Tile(TerrainType.FOREST, 0), new Tile(TerrainType.FOREST, 0)),
    DOMINO_5(5, new Tile(TerrainType.FOREST, 0), new Tile(TerrainType.FOREST, 0)),
    DOMINO_6(6, new Tile(TerrainType.FOREST, 0), new Tile(TerrainType.FOREST, 0)),
    DOMINO_7(7, new Tile(TerrainType.LAKE, 0), new Tile(TerrainType.LAKE, 0)),
    DOMINO_8(8, new Tile(TerrainType.LAKE, 0), new Tile(TerrainType.LAKE, 0)),
    DOMINO_9(9, new Tile(TerrainType.LAKE, 0), new Tile(TerrainType.LAKE, 0)),
    DOMINO_10(10, new Tile(TerrainType.GRASSLAND, 0), new Tile(TerrainType.GRASSLAND, 0)),
    DOMINO_11(11, new Tile(TerrainType.GRASSLAND, 0), new Tile(TerrainType.GRASSLAND, 0)),
    DOMINO_12(12, new Tile(TerrainType.SWAMP, 0), new Tile(TerrainType.SWAMP, 0)),
    DOMINO_13(13, new Tile(TerrainType.WHEATFIELD, 0), new Tile(TerrainType.FOREST, 0)),
    DOMINO_14(14, new Tile(TerrainType.WHEATFIELD, 0), new Tile(TerrainType.LAKE, 0)),
    DOMINO_15(15, new Tile(TerrainType.WHEATFIELD, 0), new Tile(TerrainType.GRASSLAND, 0)),
    DOMINO_16(16, new Tile(TerrainType.WHEATFIELD, 0), new Tile(TerrainType.SWAMP, 0)),
    DOMINO_17(17, new Tile(TerrainType.FOREST, 0), new Tile(TerrainType.LAKE, 0)),
    DOMINO_18(18, new Tile(TerrainType.FOREST, 0), new Tile(TerrainType.GRASSLAND, 0)),
    DOMINO_19(19, new Tile(TerrainType.WHEATFIELD, 1), new Tile(TerrainType.FOREST, 0)),
    DOMINO_20(20, new Tile(TerrainType.WHEATFIELD, 1), new Tile(TerrainType.LAKE, 0)),
    DOMINO_21(21, new Tile(TerrainType.WHEATFIELD, 1), new Tile(TerrainType.GRASSLAND, 0)),
    DOMINO_22(22, new Tile(TerrainType.WHEATFIELD, 1), new Tile(TerrainType.SWAMP, 0)),
    DOMINO_23(23, new Tile(TerrainType.WHEATFIELD, 1), new Tile(TerrainType.MINE, 0)),
    DOMINO_24(24, new Tile(TerrainType.FOREST, 1), new Tile(TerrainType.WHEATFIELD, 0)),
    DOMINO_25(25, new Tile(TerrainType.FOREST, 1), new Tile(TerrainType.WHEATFIELD, 0)),
    DOMINO_26(26, new Tile(TerrainType.FOREST, 1), new Tile(TerrainType.WHEATFIELD, 0)),
    DOMINO_27(27, new Tile(TerrainType.FOREST, 1), new Tile(TerrainType.WHEATFIELD, 0)),
    DOMINO_28(28, new Tile(TerrainType.FOREST, 1), new Tile(TerrainType.LAKE, 0)),
    DOMINO_29(29, new Tile(TerrainType.FOREST, 1), new Tile(TerrainType.GRASSLAND, 0)),
    DOMINO_30(30, new Tile(TerrainType.LAKE, 1), new Tile(TerrainType.WHEATFIELD, 0)),
    DOMINO_31(31, new Tile(TerrainType.LAKE, 1), new Tile(TerrainType.WHEATFIELD, 0)),
    DOMINO_32(32, new Tile(TerrainType.LAKE, 1), new Tile(TerrainType.FOREST, 0)),
    DOMINO_33(33, new Tile(TerrainType.LAKE, 1), new Tile(TerrainType.FOREST, 0)),
    DOMINO_34(34, new Tile(TerrainType.LAKE, 1), new Tile(TerrainType.FOREST, 0)),
    DOMINO_35(35, new Tile(TerrainType.LAKE, 1), new Tile(TerrainType.FOREST, 0)),
    DOMINO_36(36, new Tile(TerrainType.WHEATFIELD, 0), new Tile(TerrainType.GRASSLAND, 1)),
    DOMINO_37(37, new Tile(TerrainType.LAKE, 0), new Tile(TerrainType.GRASSLAND, 1)),
    DOMINO_38(38, new Tile(TerrainType.WHEATFIELD, 0), new Tile(TerrainType.SWAMP, 1)),
    DOMINO_39(39, new Tile(TerrainType.GRASSLAND, 0), new Tile(TerrainType.SWAMP, 1)),
    DOMINO_40(40, new Tile(TerrainType.MINE, 1), new Tile(TerrainType.WHEATFIELD, 0)),
    DOMINO_41(41, new Tile(TerrainType.WHEATFIELD, 0), new Tile(TerrainType.GRASSLAND, 2)),
    DOMINO_42(42, new Tile(TerrainType.LAKE, 0), new Tile(TerrainType.GRASSLAND, 2)),
    DOMINO_43(43, new Tile(TerrainType.WHEATFIELD, 1), new Tile(TerrainType.SWAMP, 2)),
    DOMINO_44(44, new Tile(TerrainType.GRASSLAND, 0), new Tile(TerrainType.SWAMP, 2)),
    DOMINO_45(45, new Tile(TerrainType.MINE, 2), new Tile(TerrainType.WHEATFIELD, 0)),
    DOMINO_46(46, new Tile(TerrainType.SWAMP, 0), new Tile(TerrainType.MINE, 2)),
    DOMINO_47(47, new Tile(TerrainType.SWAMP, 0), new Tile(TerrainType.MINE, 2)),
    DOMINO_48(48, new Tile(TerrainType.WHEATFIELD, 0), new Tile(TerrainType.MINE, 3));

    private final Domino domino;

    DominoDeck(int id, Tile tileA, Tile tileB) {
        this.domino = new Domino(id, tileA, tileB);
    }

    public Domino getDomino() {
        return domino;
    }

    public static List<Domino> getAllDominos() {
        return Arrays.stream(values())
                .map(DominoDeck::getDomino)
                .toList();
    }
}