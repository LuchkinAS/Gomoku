package server.parts;

public class Desk {
    private int[][] desk = new int[10][10];//"-1" - empty; "0" - is o; "1" - is x

    public Desk() {
        for (int i = 0; i < 10; ++i)
            for (int j = 0; j < 10; ++j)
                desk[i][j] = -1;
    }

    public void setMove(int x, int y, int value) {
        this.desk[x][y] = value;
    }

    public int[][] getDeskArray() {
        return this.desk;
    }
}
