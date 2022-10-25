module kth.se.lab4demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens kth.se.lab4demo to javafx.fxml;
    exports kth.se.lab4demo;
}