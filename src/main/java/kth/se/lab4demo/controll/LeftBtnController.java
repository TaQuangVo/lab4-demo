package kth.se.lab4demo.controll;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import kth.se.lab4demo.model.Model;
import kth.se.lab4demo.view.View;

public class LeftBtnController implements EventHandler<ActionEvent> {

    private String btnType;

    public LeftBtnController(String btnType) {
        this.btnType = btnType;
    }



    @Override
    public void handle(ActionEvent actionEvent) {
        Model model = Model.getInstance();
        View view = View.getInstance();

        if (btnType=="Hint"){
            if(model.checkGameState())
                view.viewCheckResult(true);
            model.hint();
        }else if(btnType == "Check"){
            boolean isCorrect = model.checkGameState();
            view.viewCheckResult(isCorrect);
        }
    }
}
