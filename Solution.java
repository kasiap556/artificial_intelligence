package main.java;

public class Solution {
    public Board board;
    public boolean success;
    public long time;
    public int visitedStates;
    public int processedStates;
    public int maxRecursion;

    public int solutionLength(){
        if(success){
            return board.moves.size();
        }
        else{
            return -1;
        }
    }
    public Solution(){

    }
    public Solution(Board board, boolean success, long time, int visitedStates, int processedStates, int maxRecursion){
        this.board = board;
        this.success = success;
        this.time = time;
        this.visitedStates = visitedStates;
        this.processedStates = processedStates;
        this.maxRecursion = maxRecursion;
    }

}
