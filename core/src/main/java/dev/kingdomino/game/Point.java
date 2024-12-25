package dev.kingdomino.game;

public class Point {
    private int totalPoint;
    private int additionalPoint;

    public Point() {
        totalPoint = 0;
        additionalPoint = 0;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

    public void addTotalPoint(int totalPoint) {
        this.totalPoint += totalPoint;
    }

    public int getAdditionalPoint() {
        return additionalPoint;
    }

    public void setAdditionalPoint(int additionalPoint) {
        this.additionalPoint = additionalPoint;
    }

    public void addAdditionalPoint(int additionalPoint) {
        assert additionalPoint >= 0;
        this.additionalPoint += additionalPoint;
    }

    public void reset() {
        totalPoint = 0;
        additionalPoint = 0;
    }

    public int getFinalPoint() {
        return totalPoint + additionalPoint;
    }
}
