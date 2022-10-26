package kth.se.lab4demo.controll;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import kth.se.lab4demo.model.Model;
import kth.se.lab4demo.view.View;

public class FileController implements EventHandler<ActionEvent> {
    private final String btnType;

    public FileController(String btnType) {
        this.btnType = btnType;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Model model = Model.getInstance();

        switch (this.btnType) {
            case "SAVE_GAME":
                model.saveGame();
                break;
            case "LOAD_GAME":
                model.loadGame();
                break;
            case "EXIT_GAME":
                if (model.isSaved()) Platform.exit();
                else {
                    View view = View.getInstance();
                    view.showSavedRequest();
                }
                break;
        }
    }
}

