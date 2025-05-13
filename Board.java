package main.java;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private int height, width, size, blank;
    public ArrayList<Character> moves;
    private int hammingHeuristic;
    private int manhattanHeuristic;
    private int g;
    private short[] board;

    public Board(){
        height = 0;
        width = 0;
        hammingHeuristic = 0;
        manhattanHeuristic = 0;
        g = 0;
        size = height * width;
        board = new short[size];
        moves = new ArrayList<Character>();
    }
    public Board(String inputFileName) throws IOException{
        BufferedReader inputFile = new BufferedReader (new FileReader(inputFileName));

    String SizeOfBoard = inputFile.readLine();
    String[] dimentions = SizeOfBoard.split(" ");
    height = Integer.parseInt(dimentions[0]);
    width = Integer.parseInt(dimentions[1]);
    size = height * width;
    board = new short[size];
    moves = new ArrayList<Character>();

    //read board data and find 0 - blank
    for (int i=0; i<height ; i++){
        String line = inputFile.readLine();
        String[] numbers = line.split(" ");
        for (int j=0;j<width;j++){
            short singleNumber = Short.parseShort(numbers[j]);
            board[i * width + j] = singleNumber;
            if(singleNumber == 0)
                blank = i * width + j;
        }
    }
    inputFile.close();
    hammingHeuristic = calculateHammingHeuristic();
    manhattanHeuristic = calculateManhattanHeuristic();
    g = 0;
    }
    public Board(Board cBoard){
        this.height = cBoard.height;
        this.width = cBoard.width;
        this.blank = cBoard.blank;
        this.size = cBoard.size;
        this.board = Arrays.copyOf(cBoard.board, cBoard.board.length);
        moves = new ArrayList<Character>(cBoard.moves);
        hammingHeuristic = calculateHammingHeuristic();
        manhattanHeuristic = calculateManhattanHeuristic();
        g = cBoard.getG();
    }

    public String getMovesString(){
        String movesString = "";
        for(Character c : moves){
            movesString = movesString.concat(c.toString());
        }
        return movesString;
    }
    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getSize() {
        return size;
    }

    public int getG(){ 
        return g; }

    public short getValAt(int i) {
        return board[i];
    }

    public boolean moveTo(char place) {
        int targetMove = blank;
        switch(place) {
            case 'U':
                targetMove -= width;
                break;
            case 'D':
                targetMove += width;
                break;
            case 'L':
                targetMove -= 1;
                break;
            case 'R':
                targetMove += 1;
                break;
        }

        boolean linesChecked = (blank % width == targetMove % width);
        boolean columnsChecked = (blank / width == targetMove / width);
        boolean centerChecked = (targetMove >= 0 && targetMove < size);

        if ((linesChecked || columnsChecked) && centerChecked) {
            board[blank] = board[targetMove];
            board[targetMove] = 0;
            blank = targetMove;
            moves.add(place);
            g++;
            return true;
        } else {
            return false;
        }

    }

    public int f(String heurestics){
        if(heurestics.equals("manh"))
            return g + manhattanHeuristic;
        else if(heurestics.equals("hamm"))
            return g + hammingHeuristic;
        else return 0;
    }

    public void display() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < height; j++) {
                System.out.print(board[i * width + j] + " ");
            }
            System.out.println();
        }
        System.out.println("\n");
    }

    private int calculateHammingHeuristic(){
        int val = 0;
        for (int i = 0; i < getSize(); i++) {
            if (getValAt(i) != (i + 1) % getSize() && getValAt(i) != 0) {
                val++;
            }
        }
        return val;
    }
    private int calculateManhattanHeuristic(){
        int val = 0;
        for (int i = 1; i < getSize(); i++){
            Point p = getValPos((short) i);
            val += Math.abs(p.x - Math.floor((i-1)/height)) + Math.abs(p.y - ((i-1) % height));
        }
        return val;
    }
    private Point getValPos(short val){
        Point p = new Point();
        for (int i = 0; i < getSize(); i++){
            if(getValAt(i) == val){
                p.setLocation(Math.floor(i/height), i % height);
                return p;
            }
        }
        return p;
    }
    public int getHammingHeuristic(){
        return hammingHeuristic;
    }
    public int getManhattanHeuristic(){
        return manhattanHeuristic;
    }
    public void overrideManhattanHeuristic(int val){
        manhattanHeuristic = val;
    }
    public void overrideHammingHeuristic(int val){
        hammingHeuristic = val;
    }

    public String toString() {
        StringBuilder str = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            str.append((char)('0' + board[i]));
        }
        return str.toString();
    }
    @Override
    public int hashCode(){
        return Arrays.hashCode(board);
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Board)) return false;
        Board other = (Board) o;
        if(other.getSize() != this.getSize()) return false;
        for (int i = 0; i < this.getSize(); i++){
            if(this.getValAt(i) != other.getValAt(i))
                return false;
        }
        return true;
    }
}



