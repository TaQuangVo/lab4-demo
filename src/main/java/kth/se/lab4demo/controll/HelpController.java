package kth.se.lab4demo.controll;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import kth.se.lab4demo.model.Model;
import kth.se.lab4demo.view.View;

public class HelpController implements EventHandler<ActionEvent> {

    private String btn;

    public HelpController(String btn){
        this.btn = btn;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Model model = Model.getInstance();
        if (this.btn.equals("CLEAR_ALL")){
            model.clearAll();
        }else if(this.btn.equals("RULE")){
            View.getInstance().showPopup("Just Apply Jungle Rules");
        }


    }
}
