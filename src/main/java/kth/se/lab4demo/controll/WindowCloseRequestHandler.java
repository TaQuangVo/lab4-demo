package kth.se.lab4demo.controll;

import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import kth.se.lab4demo.model.Model;

public class WindowCloseRequestHandler implements EventHandler<WindowEvent> {
    @Override
    public void handle(WindowEvent windowEvent) {
        Model.getInstance().exitGame();
    }
}
