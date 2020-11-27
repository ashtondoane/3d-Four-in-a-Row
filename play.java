import java.util.*;
import java.io.*;

public class play {
    public static void main(String args[]){
        engine thisGame = new engine();
        playGame(thisGame);
    }

    public static String[][][] formatBoard(int[][][] thisBoard){
        String[][][] formattedBoard = new String[4][4][4];
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                for(int k = 0; k < 4; k++){
                    if(thisBoard[i][j][k] == 0){
                        formattedBoard[i][j][k] = "-";
                    }
                    else if(thisBoard[i][j][k] == 1){
                        formattedBoard[i][j][k] = "●";
                    }
                    else{
                        formattedBoard[i][j][k] = "○";
                    }
                }
            }
        }
        return formattedBoard;
    }

    public static void playGame(engine game){
        Scanner kb = new Scanner(System.in);
        boolean playersMove = true;
        boolean gameEnded = false;
        int[] lastMoveBot = new int[2];
        int[] lastMove = new int[2];
        outerloop:
        while(!gameEnded){
            if(playersMove){
                System.out.println("Enter your move");
                String[] move = kb.nextLine().split(" ");
                if(move[0].equalsIgnoreCase("UNDO")){
                    game.undo(lastMove[0], lastMove[1]);
                    game.undo(lastMoveBot[0], lastMoveBot[1]);
                    continue outerloop;
                }
                lastMove[0] = Integer.parseInt(move[0]);
                lastMove[1] = Integer.parseInt(move[1]);
                game.playMove(Integer.parseInt(move[0]), Integer.parseInt(move[1]));
                System.out.println(game.scoreBoard());
                playersMove = false;
            }
            else{
                System.out.println("Processing...");
                double[] currentMove = game.bestMove(true, 0, game.depth);
                lastMoveBot[0] = (int)currentMove[1];
                lastMoveBot[1] = (int)currentMove[2];
                game.playMove((int)currentMove[1], (int)currentMove[2]);
                System.out.println((int)currentMove[1] + " " + (int)currentMove[2]);
                System.out.println(currentMove[0]);
                System.out.println(game.scoreBoard());
                playersMove = true;
            }

            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    for(int k = 0; k < 4; k++){
                        System.out.print(formatBoard(game.board)[i][j][k] + " ");
                    }
                    System.out.println();
                }
                System.out.println();
            }
            System.out.println("_____________________________ ");
        }
    }
}