package kth.se.lab4demo.controll;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import kth.se.lab4demo.model.Model;
import kth.se.lab4demo.view.View;

public class MainBoardController implements EventHandler<MouseEvent> {
    private final int x, y;

    public MainBoardController(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        Model model = Model.getInstance();
        int currentBtn = model.getCurrentActiveBtn();
        if (currentBtn==10)
            currentBtn = 0;
        model.changeStage(x,y, currentBtn);
    }
}
