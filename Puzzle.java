package main.java;

import java.util.ArrayList;
import java.util.Arrays;

public class Puzzle {
    private char[] neighbourSearchOrder;

    Puzzle(char[] neighbourSearchOrder){
        this.neighbourSearchOrder = Arrays.copyOf(neighbourSearchOrder, 4);
    }

    public boolean isGoal(Board board) {
        for (int i = 0; i < board.getSize(); i++) {
            if (board.getValAt(i) != (i + 1) % board.getSize()) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Board> neighbours(Board board){
        ArrayList<Board> neighboursToReturn = new ArrayList<Board>();
        Board copyBoard = new Board(board);
        for(int i=0;i<4;i++){
            if(copyBoard.moveTo(neighbourSearchOrder[i])){
                neighboursToReturn.add(copyBoard);
                copyBoard = new Board(board);
            }
        }
        return neighboursToReturn;
    }
}
