package main.java;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args){
        String algorithm;
        String order;
        String nameFile;
        String solutionFile;
        String statsFile;

        if(args.length<5){
            System.out.println("Za malo argumentow");
        }
            algorithm = args[0];
            order = args[1];
            nameFile = args[2];
            solutionFile = args[3];
            statsFile = args[4];

        Set<Board> dfsVisitedStates = new HashSet<Board>();
        Solver solver = new Solver();
        Board board = new Board();
        try{
            board = new Board(nameFile);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

        Puzzle puzzle = new Puzzle("RDUL".toCharArray());
        if(!order.equals("manh") && !order.equals("hamm")){
            puzzle = new Puzzle(order.toCharArray());
        }

        Solution solution = new Solution();
        switch(algorithm){
            case "bfs":
                solution = solver.bfs(puzzle, board);
                break;
            case "dfs":
                solution = solver.dfs(puzzle, board, dfsVisitedStates, 0, System.nanoTime());
                break;
            case "astr":
                solution = solver.Astar(puzzle, board, order);
                break;
        }


        savingResults(solutionFile, solution.solutionLength(), solution.board.getMovesString());

        additionalInfo(solution.success,statsFile, solution.solutionLength(),solution.visitedStates,
                solution.processedStates, solution.maxRecursion, solution.time);

    }

    public static void savingResults(String solutionFile, int lengthOfSol, String movesOfSol){
        try(FileWriter writer = new FileWriter(solutionFile)){
            writer.write(String.valueOf(lengthOfSol));
            writer.write('\n');
            writer.write(movesOfSol);
        } catch(IOException e){
            e.printStackTrace();
        }

    }

    public static double roundTo(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    public static void additionalInfo(boolean isSolved, String statsFile, int lengthOfSol,
                               int visitedStates, int processedStates, int maxRecursion,
                               double timeSeconds){
        try(FileWriter writer = new FileWriter(statsFile)){

            if(isSolved){
                writer.write(String.valueOf(lengthOfSol) + '\n');
            } else {
                writer.write("-1" + '\n');
            }
            writer.write(String.valueOf(visitedStates) + '\n');
            writer.write(String.valueOf(processedStates) + '\n');
            writer.write(String.valueOf(maxRecursion) + '\n');
            writer.write(String.valueOf(roundTo(timeSeconds/1000000000d,3)) + '\n');

        } catch(IOException e){
            e.printStackTrace();
        }

    }
}