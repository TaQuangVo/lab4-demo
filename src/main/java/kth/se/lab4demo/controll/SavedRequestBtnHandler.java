package kth.se.lab4demo.controll;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import kth.se.lab4demo.model.Model;

public class SavedRequestBtnHandler implements EventHandler<ActionEvent> {
    private final String btnType;
    private final Stage primaryStage;

    public SavedRequestBtnHandler(Stage primaryStage, String btnType) {
        this.primaryStage = primaryStage;
        this.btnType = btnType;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Button source = (Button) actionEvent.getSource();
        Stage s = (Stage)source.getScene().getWindow();
        if (this.btnType.equals("EXIT")) {
            s.close();
            primaryStage.close();
        }else if(this.btnType.equals("SAVE")) {
            Model model = Model.getInstance();
            model.saveGame();
            s.close();
            primaryStage.close();
        }

    }

}
