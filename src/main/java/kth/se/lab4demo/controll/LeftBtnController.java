package kth.se.lab4demo.controll;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import kth.se.lab4demo.model.Model;
import kth.se.lab4demo.view.View;

public class LeftBtnController implements EventHandler<ActionEvent> {
    private final String btnType;
    public LeftBtnController(String btnType) {
        this.btnType = btnType;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Model model = Model.getInstance();

        switch (btnType) {
            case "Hint" -> model.hint();
            case "Check" -> model.checkGameState();
        }
    }
}
