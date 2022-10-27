package kth.se.lab4demo;

import javafx.application.Application;
import javafx.stage.Stage;
import kth.se.lab4demo.dataTypes.SudokuLevel;
import kth.se.lab4demo.model.Model;
import kth.se.lab4demo.view.View;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {

        Model model = Model.getInstance();

        model.initNewGame(SudokuLevel.HARD);
        int[][][] gameStage = model.getStage();

        View view = View.getInstance();
        view.updateView(gameStage);
    }

    public static void main(String[] args) {
        launch();
    }
}