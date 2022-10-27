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
    private SudokuLevel CurrentLevel;

    private Model(){
        this.initNewGame(SudokuLevel.EASY);
    }

    /**
     *
     * @return the same instance of Model through out the program
     */
    public static Model getInstance(){
        if(instance == null)
            instance = new Model();
        return instance;
    }

    /**
     * init new game with the current level
     */
    public void initNewGame(){
        initNewGame(this.CurrentLevel);
    }

    /**
     * init new game with the given level;
     * @param level level to be initialize
     */
    public void initNewGame(SudokuLevel level){
        this.CurrentLevel = level;
        //get the sudokumatix form sudokuutilities
        int[][][] map = SudokuUtilities.generateSudokuMatrix(level);

        int GRID_SIZE = SudokuUtilities.GRID_SIZE;
        stage = new int[GRID_SIZE][GRID_SIZE][3];
        isSaved = false;

        //map array with original and solution -> original, solution and current stage
        for (int y = 0; y < GRID_SIZE; y++) {
            for(int x = 0; x < GRID_SIZE; x++){
                stage[y][x][0] = map[y][x][0]; // original
                stage[y][x][1] = map[y][x][1];  // the solution
                stage[y][x][2] = map[y][x][0];  // original to current stage
            }
        }

        // shuffle the stage
        shuffleStage();

        //view
        View view = View.getInstance();
        view.updateView(this.getStage());
    }

    /**
     * Get the current stage of the game
     * @return stage with 2 layers(Current stage and original)
     */
    public int[][][] getStage() {
        int GRID_SIZE = SudokuUtilities.GRID_SIZE;
        int[][][] temp = new int[GRID_SIZE][GRID_SIZE][2];

        for (int y = 0; y < GRID_SIZE; y++) {
            for(int x = 0; x < GRID_SIZE; x++){
                temp[y][x][0] = stage[y][x][0]; // original for the purpuse of indentifing the lable that can not be changed
                temp[y][x][1] = stage[y][x][2]; // current stage;
            }
        }
        return temp;
    }

    /**
     * Change the values of specified tile by the given index(x,y) to val. and then update the view;
     * @param x x-coord of the tile to change
     * @param y y-coord of the tile to change
     * @param val the new value
     */
    public void changeStage(int x, int y, int val){
        if (stage[y][x][0] != 0){ //check if the tile at (x,y) is original,
            return;
        }
        this.stage[y][x][2] = val;
        isSaved=false;

        //update view
        View view = View.getInstance();
        view.updateView(this.getStage());

        //check all tiles is filled, show the result
        if(this.isComplete())
        {
            System.out.println("tiiitf");
            view.showResultStage(this.isCorrect());
        }

    }

    /**
     * to check if all tiles are filed
     * @return true if all tiles are filed, else false
     */
    public boolean isComplete(){
        for (int y = 0; y < 9; y++) {
            for(int x = 0; x < 9; x++){
                if(stage[y][x][2] == 0)
                    return false;
            }
        }
        return true;
    }

    /**
     * load game form file
     */
    public void loadGame(){

        Model loadedModel = FileUtils.loadGame();
        if(loadedModel == null) return;

        this.stage = loadedModel.stage;
        this.isSaved = true;

        View view = View.getInstance();
        view.updateView(this.getStage());
    }

    public void saveGame(){
        FileUtils.saveGameModel(this);
        isSaved=true;
    }

    /**
     * handle exite game, ask user to save if not already saved.
     */
    public void exitGame(){
        View view = View.getInstance();
        if (this.isSaved) System.exit(0);
        else  view.showSavedRequestStage("Exit");
    }

    /**
     * check if the current stage is equal solution
     * @return true if equal, else false
     */
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

    /**
     * check result and show the result.
     */
    public void checkGameState() {
        View view = View.getInstance();
        view.showResultStage(isCorrect());
    }

    /**
     * set the current active button and update the view,
     * @param currentActiveBtn current active button index
     */
    public void setCurrentActiveBtn(int currentActiveBtn) {
        this.currentActiveBtn = currentActiveBtn+1;
        View.getInstance().updateActiveBtn(currentActiveBtn);
    }

    /**
     *
     * @return the current active button
     */
    public int getCurrentActiveBtn(){
        return this.currentActiveBtn;
    }

    /**
     * hint user a correct value of an empty cell or a cell with incorrect value
     */
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

        //show result if all tiles are filled.
        if(isCorrect()){
            View view = View.getInstance();
            view.showResultStage(true);
        }
    }

    /**
     * clear all valued filled by user, recover the original stage
     */
    public void clearAll(){
        for (int y = 0; y < 9; y++){
            for (int x = 0; x < 9; x++){
                this.stage[y][x][2] = this.stage[y][x][0];
            }
        }
            View.getInstance().updateView(getStage());
    }

    /**
     * shuffle the stage to create new Sudoku matrix;
     */
    public void shuffleStage(){
        Random r = new Random();

        //turn matrix
        for(int i = 0; i < r.nextInt(4); i++)
            this.stage = SudokuUtilities.turnStage(this.stage);

        //horizontal flip
        if(r.nextInt(2) == 0)
            this.stage = SudokuUtilities.hFlipStage(this.stage);

        //vertical flip
        if(r.nextInt(2) == 0)
            this.stage = SudokuUtilities.vFlipStage(this.stage);

        //switch 2 numbers
        for(int i = 0; i < r.nextInt(3); i++)
            SudokuUtilities.switchStage(this.stage);

        View.getInstance().updateView(getStage());
    }
}
