package kth.se.lab4demo.controll;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import kth.se.lab4demo.dataTypes.SudokuLevel;
import kth.se.lab4demo.model.Model;

public class ResultBtnController implements EventHandler<ActionEvent> {

    private String btn;

    public ResultBtnController(String btn){
        this.btn = btn;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Model model = Model.getInstance();
        switch (this.btn) {
            case "CLEAR_ALL" -> model.clearAll();
            case "NEW_GAME" -> model.initNewGame();
            case "EXIT_GAME" -> model.exitGame();
        }

        Button source = (Button) (actionEvent.getSource());
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
