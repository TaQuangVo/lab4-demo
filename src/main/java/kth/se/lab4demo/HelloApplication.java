package kth.se.lab4demo;

import javafx.application.Application;
import javafx.stage.Stage;
import kth.se.lab4demo.dataTypes.SudokuLevel;
import kth.se.lab4demo.model.Model;
import kth.se.lab4demo.view.View;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Model model = Model.getInstance();

        model.initNewGamge(SudokuLevel.HARD);
        int[][][] gameStage = model.getStage();

        View view = View.getInstance();
        view.view(gameStage);
    }

    public static void main(String[] args) {
        launch();
    }
}