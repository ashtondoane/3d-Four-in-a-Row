import java.util.*;

public class engine{
    public static int botSide = 2;
    public static int depth = 5;
    public static int turn = 1;
    public static int[][][] board = new int[4][4][4];

    public engine(){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                for(int k = 0; k < 4; k++){
                    board[i][j][k] = 0;
                }
            }
        }
    }

    public static void playMove(int x, int y){
        board[getLowest(x, y)][x][y] = turn;
        turn = 3 - turn;
    }

    public static void undo(int x, int y){
        board[getLowest(x, y)-1][x][y] = 0;
        turn = 3 - turn;
    }

    public static int getLowest(int x, int y){
        for(int i = 0; i < 4; i++){
            if(board[i][x][y] == 0){
                return i;
            }
        }
        return 4;
    }

    public static boolean isValidMove(int x, int y){
        return (getLowest(x, y) < 4);
    }

    public static ArrayList<int[][]> getSlices(int[][][] currentBoard){ //returns all 2D slices of the board
        ArrayList<int[][]> slices = new ArrayList<int[][]>();
        // horizontal
        for (int d = 0; d < 4; d++) {
            int[][] slice = new int[4][4];
            for (int h = 0; h < 4; h++) {
                for (int w = 0; w < 4; w++) {
                    slice[h][w] = board[h][d][w];
                }
            }
            slices.add(slice);
        }

        // other
        for (int w = 0; w < 4; w++) {
            int[][] slice = new int[4][4];
            for (int h = 0; h < 4; h++) {
                for (int d = 0; d < 4; d++) {
                    slice[h][d] = board[h][d][w];
                }
            }
            slices.add(slice);
        }

        // diagonal
        for (int s = 0; s < 2; s++) {
            int[][] slice = new int[4][4];
            for (int h = 0; h < 4; h++) {
                for (int i = 0; i < 4; i++) {
                    slice[h][i] = board[h][i][(s == 0) ? i : 3 - i];
                }
            }
            slices.add(slice);
        }

        return slices;
    }

    public static int colRowStreaks(int[][] slice, int length, int side){ //returns the number of unblocked streaks of length n on rows or columns
        int count = 0;
        for(int i = 0; i < 4; i++){
            boolean col = true;
            int colC = 0;
            boolean row = true;
            int rowC = 0;
            for(int j = 0; j < 4; j++){
                // System.out.print(slice[i][j] + " "); //check
                if(slice[i][j] == side){
                    colC += 1;
                }
                else if(slice[i][j] == 3 - side){
                    col = false;
                }
                if(slice[j][i] == side){
                    rowC += 1;
                }
                else if(slice[j][i] == 3 - side){
                    row = false;
                }
            }
            if(row && rowC == length){
                count += 1;
            }
            if(col && colC == length){
                count += 1;
            }
            rowC = 0;
            colC = 0;
        }
        return count; 
    }

    public static int diagStreaks(int[][] slice, int length, int side){ //returns the number of unblocked streaks of length n on diagonals
        int count = 0;
        boolean d1 = true;
        int d1c = 0;
        boolean d2 = true;
        int d2c = 0;
        for(int i = 0; i < 4; i ++){
            if(slice[i][i] == side){
                d1c += 1;
            }
            else if(slice[i][i] == 3 - side){
                d1 = false;
            }
            if(slice[i][3-i] == side){
                d2c += 1;
            }
            else if(slice[i][3-i] == 3 - side){
                d2 = false;
            }
        }
            if(d1 && d1c == length){
                count += 1;
            }
            if(d2 && d2c == length){
                count += 1;
            }
            return count; //some double counting going on here potentially
        }

    public static int numImportant(int[][][] currentBoard, int side){
        int count = 0;
        int[][] locations = new int[][]{new int[]{0, 0, 0}, new int[]{0, 0, 3}, new int[]{0, 3, 0}, new int[]{0, 3, 3}};
        for(int i = 0; i < locations.length; i++){
            if(currentBoard[locations[i][0]][locations[i][1]][locations[i][2]] == side){
                count++;
            }
        }
        return count;
    }

    public static double scoreBoard(){ //Scoring created through personal experience playing the game
        double scoreUs = 0;
        double scoreThem = 0;
        ArrayList<int[][]> slices = getSlices(board);

        for(int[][] slice : slices){
            if(diagStreaks(slice, 4, botSide) > 0 || colRowStreaks(slice, 4, botSide) > 0){
                return Double.POSITIVE_INFINITY;
            }
            if(diagStreaks(slice, 4, 3-botSide) > 0 || colRowStreaks(slice, 4, 3-botSide) > 0){
                return Double.NEGATIVE_INFINITY;
            }
        }
        for(int[][] slice : slices)
            for(int i = 2; i <= 4; i++){
                scoreUs += Math.pow(colRowStreaks(slice, i, botSide), i);
                scoreUs += Math.pow(diagStreaks(slice, i, botSide), i);
                scoreThem += Math.pow(colRowStreaks(slice, i, 3 - botSide), i);
                scoreThem += Math.pow(diagStreaks(slice, i, 3 - botSide), i);
            }

        //Important locations should be given a bias
        scoreUs += 10*numImportant(board, botSide);
        scoreThem += 10*numImportant(board, 3- botSide);

        return scoreUs - scoreThem;
    }
    
    // calculate best move using minimax
    public static double[] bestMove(boolean maximizingPlayer, int currentDepth, int maxDepth){
        double score = scoreBoard();
        if(score == Double.POSITIVE_INFINITY){
            return new double[]{score};
        }
        else if(score == Double.NEGATIVE_INFINITY){
            return new double[]{score};
        }
        if(currentDepth == maxDepth){
            return new double[]{score};
        }

        if(maximizingPlayer){
            double bestValue = Double.NEGATIVE_INFINITY;
            double[] bestMove = new double[3];
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    if(isValidMove(i, j)){
                        playMove(i, j);
                        double val = bestMove(false, currentDepth + 1, maxDepth)[0];
                        if(val >= bestValue){
                            bestValue = val;
                            bestMove[0] = val;
                            bestMove[1] = i;
                            bestMove[2] = j;
                        }
                        undo(i, j);
                    }
                } 
            }
            return bestMove;
        }
        else{
            double bestValue = Double.POSITIVE_INFINITY;
            double[] bestMove1 = new double[3];
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    if(isValidMove(i, j)){
                        playMove(i, j);
                        double val = bestMove(true, currentDepth + 1, maxDepth)[0];
                        if(val <= bestValue){
                            bestValue = val;
                            bestMove1[0] = val;
                            bestMove1[1] = i;
                            bestMove1[2] = j;
                        }
                        undo(i, j);
                    } 
                }
            }
            return bestMove1;
        }
    }
}