package kth.se.lab4demo.controll;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import kth.se.lab4demo.dataTypes.SudokuLevel;
import kth.se.lab4demo.model.Model;

public class NewGameBtnController implements EventHandler<ActionEvent> {
    private String btn;

    public NewGameBtnController(String btn){
        this.btn = btn;
    }


    @Override
    public void handle(ActionEvent actionEvent) {
        Model model = Model.getInstance();
        switch (this.btn) {
            case "NEW_EASY_GAME" -> model.initNewGame(SudokuLevel.EASY);
            case "NEW_MEDIUM_GAME" -> model.initNewGame(SudokuLevel.MEDIUM);
            case "NEW_HARD_GAME" -> model.initNewGame(SudokuLevel.HARD);
        }
    }
}
