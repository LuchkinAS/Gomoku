package server.parts;

import java.util.Vector;

public class Bot {
    private int deskID;

    private int[][] orDesk = new int[10][10];
    private final int BOT = 1;
    private final int PLAYER = 0;
    private int prevTern;


    public Bot(int deskID) { }

    public Bot() {

    }


    public void setBotPreMove(int index) { prevTern = index;}



    public Vector<Integer> getEmptyCells(int [][] desk) {
        Vector<Integer> emptyCells = new Vector<Integer>();
        for (int i = 0; i < 10; ++i)
            for (int j = 0; j < 10; ++j)
                if(desk[i][j] == -1) {
                    emptyCells.add(i * 10 + j);
                }
        return emptyCells;
    }
    // downgrade minimax
    public Vector<Integer> getEmptyCells(int [][] desk, int currentIndex)
    {
        Vector<Integer> freeCells = new Vector<>();
        int x = currentIndex / 10;
        int y = currentIndex % 10;

        if(x - 1 >= 0 && desk[x - 1][y] == -1)
            freeCells.add((x - 1) * 10 + y);
        if(y - 1 >= 0 && desk[x][y - 1] == -1)
            freeCells.add((x) * 10 + y - 1);
        if( x - 1 >= 0 && y - 1 >= 0 && desk[x - 1][y - 1] == -1)
            freeCells.add((x-1)*10 + y - 1);
        if(x + 1 < 10 && desk[x + 1][y] == -1)
            freeCells.add((x + 1) * 10 + y);
        if(y + 1 < 10 && desk[x][y + 1] == -1)
            freeCells.add((x) * 10 + y + 1);
        if(x + 1 < 10 && y + 1 < 10 && desk[x+ 1 ][y + 1] == -1)
            freeCells.add((x+ 1) * 10 + y + 1);
        if(x - 1 >= 0 && y + 1 < 10 && desk[x - 1][y + 1] == -1)
            freeCells.add((x - 1) * 10 + y + 1);
        if(x + 1 < 10 && y - 1 >= 0 && desk[x + 1][y - 1] == -1)
            freeCells.add((x + 1) * 10 + y - 1);
        return freeCells;
    }

    

    public int doMove(Desk desk) {
        int[][] orDesk = desk.getDeskArray();
        if(getEmptyCells(orDesk).size() == 100) {
            prevTern = 55;
            return 55;
        }
        int newMove = minimax(orDesk, BOT, prevTern, 0).getIndex();
        if(newMove == -1)
        {
            Vector<Integer> nextTern =  getEmptyCells(orDesk);
            return nextTern.get((int) (Math.random() * nextTern.size() - 1));
        }
        prevTern = newMove;
        return prevTern;
    }


    Move minimax(int [][] desk, int player, int preT, int depth){
        //available spots
        Vector<Integer> availSpots = getEmptyCells(desk, preT);

        // checks for the terminal states such as win, lose, and tie and returning a value accordingly
        if (winning(desk, PLAYER))
            return new Move(-1, -10);
        else if (winning(desk, BOT))
            return new Move(-1, 10);
        else if (availSpots.size() == 0 || depth > 6)
            return new Move(-1, 0);


        Vector<Move> moves = new Vector<>();
        // loop through available spots
        for (int i = 0; i < availSpots.size(); i++){
            int index = availSpots.get(i);
            int nX = index / 10;
            int nY = index % 10;
            // set the empty spot to the current player
            desk[nX][nY] = player;

            int score = 0;

            Move move;
            if (player == BOT){
                move = minimax(desk, PLAYER, availSpots.get(i), depth + 1);
                score = move.getScore();
            }
            else{
                move = minimax(desk, BOT, availSpots.get(i), depth + 1);
                score = move.getScore();
            }
            //reset the spot to empty
            desk[nX][nY] = -1;

            // push the object to the array
            moves.add(new Move(index, score));
        }

        int bestMoveIndex = 0;
        if(player == BOT){
            int bestScore = -10000;
            for(int i = 0; i < moves.size(); i++){
                if(moves.get(i).getScore() > bestScore){
                    bestScore = moves.get(i).getScore();
                    bestMoveIndex = i;
                }
            }
        }else{
            int bestScore = 10000;
            for(int i = 0; i < moves.size(); i++){
                if(moves.get(i).getScore() < bestScore){
                    bestScore = moves.get(i).getScore();
                    bestMoveIndex = i;
                }
            }
        }

        return moves.get(bestMoveIndex);
    }






    // player - обозначение игрок -- 1 или бот -- 0
    public boolean winning(int [][] desk, int player) {
        //rows: done
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 5; j++) {
                if(desk[i][j] == player && desk[i][j + 1] == player
                        && desk[i][j + 2] == player && desk[i][j + 3] == player && desk[i][j + 4] == player)
                    return true;

            }
        }

        //cols: done
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 10; j++) {
                if(desk[i][j] == player && desk[i + 1][j] == player && desk[i + 2][j] == player
                        && desk[i + 3][j] == player && desk[i + 4][j] == player)
                    return true;
            }
        }

        // check main diag and below: done
        int win = 0;
        for(int k = 0; k < 6; k++) {
            for(int j = 0; k + j < 10; j++) {
                if(desk[k + j][j]==player)
                    win++;
                if(win > 4)
                    return true;
                if(desk[k + j][j]!=player)
                    win = 0;
            }
        }

        // main diag and above: done
        win = 0;
        for(int k = 1; k < 6; k++) {
            for(int i = 0; k + i < 10; i++) {
                if(desk[i][k + i] == player)
                    win++;
                if(win > 4)
                    return true;
                if(desk[i][k + i] != player)
                    win = 0;
            }
        }


        // sub diag and below: done
        win = 0;
        for(int k = 1; k < 7; k++) {
            for (int i = 0; 10 - k - i >= 0; i++) {
                if(desk[i][10 - k - i] == player)
                    win++;
                if(win > 4)
                    return true;
                if(desk[i][10 - k - i] != player)
                    win = 0;
            }
        }

        // sub diag and above
        win = 0;
        for(int k = 0; k < 5; k++) {
            for (int j = 9; 10 - j + k < 10; j--)
            {
                if(desk[10 - j + k][j] == player)
                    win++;
                if(win > 4)
                    return true;
                if(desk[10 - j + k][j] != player)
                    win = 0;

            }
        }
        return false;
    }

    public void setPrevTern(int index)
    {
        prevTern = index;
    }


    class Move {
        private int index;
        private int score;

        Move(int ind, int sc) {
            index = ind;
            score = sc;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }


}
