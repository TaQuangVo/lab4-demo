package kth.se.lab4demo.controll;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import kth.se.lab4demo.model.Model;

public class SavedRequestBtnHandler implements EventHandler<ActionEvent> {
    private final String btnType;

    public SavedRequestBtnHandler(String btnType) {
        this.btnType = btnType;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Model model = Model.getInstance();
        switch (this.btnType) {
            case "EXIT" -> System.exit(0);
            case "SAVE" -> {
                Button source = (Button) (actionEvent.getSource());
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
                model.saveGame();
                System.exit(0);
            }
            case "LOAD" -> {
                Button source = (Button) (actionEvent.getSource());
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();

                model.loadGame();
                break;
            }
            default -> System.out.println(this.btnType);
        }

    }

}
