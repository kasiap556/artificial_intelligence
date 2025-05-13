package main.java;

import java.util.*;

public class Solver {

    int maxRecursions = 20;
    public int dfsProcessedStates = 0;
    public int dfsMaxRecursion = 0;
    public Solver(){
    }

    public Solution bfs(Puzzle G, Board state){
        int maxRecursion = 0;
        long start = System.nanoTime();
        int processedStates = 0;
        if(G.isGoal(state)){
            long end = System.nanoTime();
            return new Solution(state, true, end-start, 1, processedStates, maxRecursion); //0 czy 1 w visitedStates?
        }
        Queue<Board> queue = new LinkedList<Board>();
        Set<Board> visitedStates = new HashSet<>();
        queue.add(state);
        visitedStates.add(state);
        while(!queue.isEmpty()){
            Board v = new Board(queue.poll());
            processedStates++;
            for(Board n : G.neighbours(v)){
                if(!visitedStates.contains(n)){
                    if(n.moves.size() > maxRecursion){
                        maxRecursion = n.moves.size();
                    }
                    if(G.isGoal(n)){
                        long end = System.nanoTime();
                        return new Solution(n, true, end-start, visitedStates.size(), processedStates, n.moves.size());
                    }
                    queue.add(n); //bo trzeba od niego wziac sasiadow
                    visitedStates.add(n);
                }
            }
        }
        long end = System.nanoTime();
        return new Solution(state, false, end-start, visitedStates.size(), processedStates, maxRecursion);
    }

    public Solution dfs(Puzzle G, Board state, Set<Board> dfsVisitedStates, int recursion, long start){
        if(recursion > maxRecursions){
            long end = System.nanoTime();
            return new Solution(state, false, end-start, dfsVisitedStates.size(), dfsProcessedStates, maxRecursions);
        }
        if(state.moves.size() > dfsMaxRecursion){
            dfsMaxRecursion = state.moves.size();
        }
        if(G.isGoal(state)){
            long end = System.nanoTime();
            return new Solution(state, true, end-start, dfsVisitedStates.size(), dfsProcessedStates, dfsMaxRecursion);
        }
        dfsVisitedStates.add(state);
        for (Board n : G.neighbours(state)){
            if(!dfsVisitedStates.contains(n)){
                Solution solution = dfs(G, n, dfsVisitedStates, recursion + 1, start);

                if(solution.success){
//                    long end = System.nanoTime();
                    return solution;
                }
            }
        }
        dfsProcessedStates++;

        long end = System.nanoTime();
        return new Solution(state, false, end-start, dfsVisitedStates.size(), dfsProcessedStates, dfsMaxRecursion);
    }

    public Solution Astar(Puzzle G, Board state, String heurestics){
        int processedStates = 0;
        int maxRecursion = 0;
        long start = System.nanoTime();
        PriorityQueue<Board> priorityQueue = new PriorityQueue<>(new Comparator<Board>() {
            @Override
            public int compare(Board o1, Board o2) {
                return Integer.compare(o1.f(heurestics), o2.f(heurestics));
            }
        });

        Set<Board> visitedStates = new HashSet<>();
        if(heurestics.equals("manh")){
            state.overrideManhattanHeuristic(0);
        }else if(heurestics.equals("hamm")){
            state.overrideHammingHeuristic(0);
        }
        else{
            long end = System.nanoTime();
            return new Solution(state, false, end-start, 0, 0, 0);
        }

        priorityQueue.add(state);

            while(!priorityQueue.isEmpty()){
                Board v = priorityQueue.poll();
                if(!visitedStates.contains(v)){
                    if(v.moves.size() > maxRecursion){
                        maxRecursion = v.moves.size();
                    }
                    if(G.isGoal(v)){
                        long end = System.nanoTime();
                        return new Solution(v, true, end-start, visitedStates.size(), processedStates, maxRecursion);
                    }
                    visitedStates.add(v);
                    for(Board n : G.neighbours(v)){
                        if(!visitedStates.contains(n)){
                            priorityQueue.add(n);
                        }
                    }
                    processedStates++;
                }
            }
        long end = System.nanoTime();
        return new Solution(state, false, end-start, visitedStates.size(), processedStates, maxRecursion);
    }
}
