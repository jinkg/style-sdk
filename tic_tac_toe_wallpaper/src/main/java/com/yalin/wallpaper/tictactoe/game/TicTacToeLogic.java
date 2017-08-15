package com.yalin.wallpaper.tictactoe.game;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Paul on 9/9/2014.
 */
public class TicTacToeLogic {

    private static int[][] groups = new int[][]{new int[]{0, 3, 6}, new int[]{1, 4, 7}, new int[]{2, 5, 8}, // vertical lines
            new int[]{0, 1, 2}, new int[]{3, 4, 5}, new int[]{6, 7, 8}, // horizontal lines
            new int[]{0, 4, 8}, new int[]{2, 4, 6}
    };

    private static int getAIWinMove(Box[] boxes,ArrayList<Integer> freeBoxes){
        for(int i=0; i<freeBoxes.size(); i++){
            int move=freeBoxes.get(i);
            boxes[move].value='o';
            if(someOneWon('o',boxes)!=null){
                boxes[move].value=' ';
                return move;
            }
            boxes[move].value=' ';
        }
        return -1;
    }
    private static int getAISaveMove(Box[] boxes,ArrayList<Integer> freeBoxes){
        for(int i=0; i<freeBoxes.size(); i++){
            int move=freeBoxes.get(i);
            boxes[move].value='x';
            if(someOneWon('x',boxes)!=null){
                boxes[move].value=' ';
                return move;
            }
            boxes[move].value=' ';
        }
        return -1;
    }
    public static int getAIMove(Box[] boxes) {
        ArrayList<Integer> freeBoxes = new ArrayList<Integer>();
        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i].value == ' ') {
                freeBoxes.add(i);
            }
        }
        if (freeBoxes.size() == 0) {
            return -1;
        } else {
            Collections.shuffle(freeBoxes);
            int winMove=getAIWinMove(boxes,freeBoxes);
            if(winMove!=-1){
                return winMove;
            }
            int saveMove=getAISaveMove(boxes,freeBoxes);
            if(saveMove!=-1){
                return saveMove;
            }
            return freeBoxes.get(0);
        }
    }

    private static int[] someOneWon(char character, Box[] boxes) {

        for (int[] grp : groups) {
            if (boxes[grp[0]].value == character && boxes[grp[1]].value == character && boxes[grp[2]].value == character) {
                return grp;
            }

        }
        return null;
    }

    public static int getGameState(Box[] boxes) {
        int playing = 0;
        int player1won = 1;
        int player2won = 2;
        int draw = 3;


        int[] result = someOneWon('x', boxes);
        if (result != null) {
            boxes[result[0]].red = true;
            boxes[result[1]].red = true;
            boxes[result[2]].red = true;
            return player1won;
        }
        result = someOneWon('o', boxes);
        if (result != null) {
            boxes[result[0]].red = true;
            boxes[result[1]].red = true;
            boxes[result[2]].red = true;
            return player2won;
        }
        for (Box box : boxes) {
            if (box.value == ' ') {
                return playing;
            }
        }
        return draw;
    }
}
