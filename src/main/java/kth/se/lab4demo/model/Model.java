package kth.se.lab4demo.model;


import kth.se.lab4demo.dataTypes.SudokuLevel;
import kth.se.lab4demo.view.View;

import java.io.Serializable;
import java.util.Random;

public class Model implements Serializable {
    private static Model instance = null;
    private int [][][] stage;// [y][x][0]: originalen;  [y][x][1]: lösninigen; [y][x][2]: currentStage
    private boolean isSaved = false;
    private int currentActiveBtn = 0;
    private SudokuLevel sudokuLevel;

    private Model(){
        this.initNewGame(SudokuLevel.EASY);
    }

    public static Model getInstance(){
        if(instance == null)
            instance = new Model();
        return instance;
    }

    public void initNewGame(){
        initNewGame(this.sudokuLevel);
    }

    public void initNewGame(SudokuLevel level){
        this.sudokuLevel = level;
        int[][][] map = SudokuUtilities.generateSudokuMatrix(level);

        int GRID_SIZE = SudokuUtilities.GRID_SIZE;
        stage = new int[GRID_SIZE][GRID_SIZE][3];
        isSaved = false;

        for (int y = 0; y < GRID_SIZE; y++) {
            for(int x = 0; x < GRID_SIZE; x++){
                stage[y][x][0] = map[y][x][0];
                stage[y][x][1] = map[y][x][1];
                stage[y][x][2] = map[y][x][0];
            }
        }

        shuffleStage();

        View view = View.getInstance();
        view.view(this.getStage());
    }

    public int[][][] getStage() {
        int GRID_SIZE = SudokuUtilities.GRID_SIZE;
        int[][][] temp = new int[GRID_SIZE][GRID_SIZE][2];
        for (int y = 0; y < GRID_SIZE; y++) {
            for(int x = 0; x < GRID_SIZE; x++){
                temp[y][x][0] = stage[y][x][0];
                temp[y][x][1] = stage[y][x][2];
            }
        }
        return temp;
    }

    public void changeStage(int x, int y, int val){
        if (stage[y][x][0] != 0){
            return;
        }
        this.stage[y][x][2] = val;
        isSaved=false;
        View view = View.getInstance();
        view.view(this.getStage());

        if(this.isComplete())
        {
            System.out.println("tiiitf");
            view.showResultStage(this.isCorrect());
        }

    }

    public boolean isComplete(){
        for (int y = 0; y < 9; y++) {
            for(int x = 0; x < 9; x++){
                if(stage[y][x][2] == 0)
                    return false;
            }
        }
        return true;
    }

    public void loadGame(){

        Model loadedModel = FileUtils.loadGame();
        if(loadedModel == null) return;

        this.stage = loadedModel.stage;
        this.isSaved = true;

        View view = View.getInstance();
        view.view(this.getStage());
    }

    public void saveGame(){
        FileUtils.saveGameModel(this);
        isSaved=true;
    }

    public void exitGame(){
        View view = View.getInstance();
        if (this.isSaved) System.exit(0);
        else  view.showSavedRequestStage("Exit");
    }

    private boolean isCorrect(){
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

    public void checkGameState() {
        View view = View.getInstance();
        view.showResultStage(isCorrect());
    }

    public void setCurrentActiveBtn(int currentActiveBtn) {
        this.currentActiveBtn = currentActiveBtn+1;
        View.getInstance().updateActiveBtn(currentActiveBtn);
    }

    public int getCurrentActiveBtn(){
        return this.currentActiveBtn;
    }
    public void hint() {
        Random random = new Random();
        while (!isCorrect()) {
            int x = random.nextInt(9);
            int y = random.nextInt(9);

            if (this.stage[y][x][2] != 0 && this.stage[y][x][2] == this.stage[y][x][1])
                continue;
            this.changeStage(x,y,this.stage[y][x][1]);
            break;
        }

        if(isCorrect()){
            View view = View.getInstance();
            view.showResultStage(true);
        }
    }

    public void clearAll(){
        for (int y = 0; y < 9; y++){
            for (int x = 0; x < 9; x++){
                this.stage[y][x][2] = this.stage[y][x][0];
            }
        }
            View.getInstance().view(getStage());
    }

    public void shuffleStage(){
        Random r = new Random();

        for(int i = 0; i < r.nextInt(4); i++)
            this.stage = SudokuUtilities.turnStage(this.stage);

        if(r.nextInt(2) == 0)
            this.stage = SudokuUtilities.hFlipStage(this.stage);

        if(r.nextInt(2) == 0)
            this.stage = SudokuUtilities.vFlipStage(this.stage);

        for(int i = 0; i < r.nextInt(3); i++)
            SudokuUtilities.switchStage(this.stage);

        View.getInstance().view(getStage());
    }
}
