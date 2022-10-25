package kth.se.lab4demo.controll;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import kth.se.lab4demo.model.Model;
import kth.se.lab4demo.view.View;

public class RightBtnController implements EventHandler<ActionEvent> {
    private int btnIndex;
    public RightBtnController(int btnIndex){
        this.btnIndex = btnIndex;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        View view = View.getInstance();
        view.setCurrentActiveBtn(this.btnIndex);
    }
}
