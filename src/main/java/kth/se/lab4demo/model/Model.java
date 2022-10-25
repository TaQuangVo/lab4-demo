package kth.se.lab4demo.model;


import kth.se.lab4demo.dataTypes.SudokuLevel;
import kth.se.lab4demo.view.View;

import java.util.Random;

public class Model {
    private static Model instance = null;
    private int [][][] stage;// [y][x][0]: originalen;  [y][x][1]: lösninigen; [y][x][2]: currentStage

    private Model(){
        this.initNewGamge(SudokuLevel.EASY);
    }

    public int[][][] getStage() {
        int GRID_SIZE = SudokuUtilities.GRID_SIZE;
        int temp[][][] = new int[GRID_SIZE][GRID_SIZE][2];
        for (int y = 0; y < GRID_SIZE; y++) {
            for(int x = 0; x < GRID_SIZE; x++){
                temp[y][x][0] = stage[y][x][0];
                temp[y][x][1] = stage[y][x][2];
            }
        }
        return temp;
    }

    public void changeStage(int x, int y, int val){
        if (stage[y][x][0] == 0)
            this.stage[y][x][2] = val;

        View view = View.getInstance();
        view.view(this.getStage());
    }

    public void initNewGamge(SudokuLevel level){
        int[][][] map = SudokuUtilities.generateSudokuMatrix(level);

        int GRID_SIZE = SudokuUtilities.GRID_SIZE;
        stage = new int[GRID_SIZE][GRID_SIZE][3];

        for (int y = 0; y < GRID_SIZE; y++) {
            for(int x = 0; x < GRID_SIZE; x++){
                stage[y][x][0] = map[y][x][0];
                stage[y][x][1] = map[y][x][1];
                stage[y][x][2] = map[y][x][0];
            }
        }
    }

    public boolean checkGameState() {
       int GRID_SIZE = SudokuUtilities.GRID_SIZE;
        for (int x = 0; x < GRID_SIZE; x++ ) {
            for (int y = 0; y < GRID_SIZE; y++) {
                if (stage[y][x][1]!=stage[y][x][2]) {
                    return false;
                }
            }
        }
        return true;
    }

    public void hint() {
        Random random = new Random();
        while (!checkGameState()) {
            int x = random.nextInt(9);
            int y = random.nextInt(9);

            if (stage[y][x][2] != 0 && stage[y][x][2] == stage[y][x][1])
                continue;
            this.changeStage(x,y,stage[y][x][1]);
            break;
        }
    }

    public static Model getInstance(){
        if(instance == null)
            instance = new Model();
        return instance;
    }

}
